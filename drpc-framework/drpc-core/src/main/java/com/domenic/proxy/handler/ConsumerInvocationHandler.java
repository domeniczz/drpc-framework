package com.domenic.proxy.handler;

import com.domenic.discovery.Registry;
import com.domenic.drpc.DrpcBootstrap;
import com.domenic.exceptions.NetworkException;
import com.domenic.netty.NettyBootstrap;
import com.domenic.transport.message.RequestMessage;
import com.domenic.transport.message.RequestPayload;
import com.domenic.transport.message.Types;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Domenic
 * @Classname ConsumerInvocationHandler
 * @Description an invocation handler for consumer, encapulates conmmunication logics
 * @Created by Domenic
 */
@Slf4j
public class ConsumerInvocationHandler implements InvocationHandler {

    private final Class<?> serviceInterface;

    private final Registry registry;

    public ConsumerInvocationHandler(Class<?> serviceInterface, Registry registry) {
        this.serviceInterface = serviceInterface;
        this.registry = registry;
    }

    /**
     * Invoke logic of the service proxy
     * @return response from server
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InetSocketAddress serviceAddr = lookupAvailableService();
        Channel channel = establishChannel(serviceAddr);
        RequestMessage message = packageMessage(method, args);
        return sendMessage(channel, message);
    }

    /**
     * Lookup an available service from registry
     * @return
     */
    private InetSocketAddress lookupAvailableService() {
        InetSocketAddress serviceAddr = registry.lookup(serviceInterface.getName());
        if (log.isDebugEnabled()) {
            log.debug("Discover a service \"{}\" with host address \"{}\" ", serviceInterface.getName(), serviceAddr.toString());
        }
        return serviceAddr;
    }

    /**
     * Establish a connection (channel) to the server to communicate
     * @param address server address
     * @return channel to the server
     */
    private Channel establishChannel(InetSocketAddress address) {
        // get the required channel from cache
        Channel cachedChannel = DrpcBootstrap.CHANNELS_CACHE.get(address);
        if (cachedChannel != null && cachedChannel.isOpen()) {
            return cachedChannel;
        }

        // if no required channel is cached, create a new one & cache it
        try {
            CompletableFuture<Channel> channelFuture = new CompletableFuture<>();
            // This Bootstrap is similar to ServerBootstrap, it's one for the client
            // except that it's for non-server channels such as a client-side or connectionless channel.
            ChannelFuture f = NettyBootstrap.getInstance().connect(address.getAddress(), address.getPort());

            // Async: add a listener to the ChannelFuture which will be notified when connection is established
            f.addListener((ChannelFutureListener) promise -> {
                if (promise.isSuccess()) {
                    channelFuture.complete(promise.channel());
                    if (log.isDebugEnabled()) {
                        log.debug("Connect to Server at \"{}\" successfully", address);
                    }
                } else {
                    Throwable cause = promise.cause();
                    channelFuture.completeExceptionally(cause);
                    log.error("Exception when sending message: {}", cause.getMessage());
                    throw new NetworkException("Exception when sending message: " + cause);
                }
            });

            // ChannelFuture represents an I/O operation which has not yet occurred (async).
            Channel channel = channelFuture.get(3, TimeUnit.SECONDS);

            // if the channel is closed, remove it from cache
            channel.closeFuture().addListener((ChannelFutureListener) future -> {
                DrpcBootstrap.CHANNELS_CACHE.remove(address);
                log.info("Channel with address \"{}\" is closed...", address);
            });

            if (log.isDebugEnabled()) {
                log.debug("Connect to Server at: " + address);
            }

            // cache the channel
            DrpcBootstrap.CHANNELS_CACHE.put(address, channel);
            return channel;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            log.error("Exception when establishing channel with \"{}\": {}", address, e);
        } catch (ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * encapsulate the message to be sent
     * @param method method to call
     * @param args arguments of the method
     * @return message to be sent
     */
    private RequestMessage packageMessage(Method method, Object[] args) {
        RequestPayload requestPayload = RequestPayload.builder()
                .interfaceName(serviceInterface.getName())
                .methodName(method.getName())
                .parametersType(method.getParameterTypes())
                .parametersValue(args)
                .returnType(method.getReturnType())
                .build();

        return RequestMessage.builder()
                .requestId(1L)
                .compressType((byte) 1)
                .requestType(Types.RequestType.REQUEST.getId())
                .serializeType((byte) 1)
                .requestPayload(requestPayload)
                .build();
    }

    /**
     * Send message through the channel
     * @param channel channel
     * @param message message
     * @return response
     */
    private Object sendMessage(Channel channel, RequestMessage message) {
        if (!channel.isActive()) {
            log.error("Channel is inactive, can't send message");
            throw new NetworkException("Channel is inactive");
        }

        try {
            CompletableFuture<Object> completableFuture = new CompletableFuture<>();
            // hang and expose the CompletableFuture to get server-side response after sending message
            DrpcBootstrap.PENDING_REQUEST.put(1L, completableFuture);

            // Async: add a listener to the ChannelFuture which will be notified when message is sent
            channel.writeAndFlush(message).addListener((ChannelFutureListener) promise -> {
                // the response of 'promise' is the result of `writeAndFlush()`
                // and once the message has been written out, 'promise' will be completed
                // thus, we can't get server-side response with `complete()` in `CompletableFuture`

                if (!promise.isSuccess()) {
                    Throwable cause = promise.cause();
                    completableFuture.completeExceptionally(promise.cause());
                    log.error("Exception when sending message, {}", cause.getMessage());
                    throw new NetworkException("Exception when sending message: " + cause);
                }
            });

            // block until the response is received or complete method in CompletableFuture is executed
            // the complete method will be called at the end of incoming pipeline (the last inbound handler)
            return completableFuture.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            log.error("Exception when establishing channel with \"{}\": {}", channel.remoteAddress(), e);
        } catch (ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        return null;
    }

}

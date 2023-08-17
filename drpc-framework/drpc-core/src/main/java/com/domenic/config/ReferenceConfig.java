package com.domenic.config;

import com.domenic.discovery.Registry;
import com.domenic.drpc.DrpcBootstrap;
import com.domenic.exceptions.NetworkException;
import com.domenic.netty.NettyBootstrap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Domenic
 * @Classname ReferenceConfig
 * @Description Reference Config
 * @Created by Domenic
 */
@Slf4j
public class ReferenceConfig<T> {

    private Class<T> serviceInterface;

    private Registry registry;

    @SuppressWarnings("unchecked")
    /**
     * Get a proxy instance of the service
     * @return proxy instance
     */
    public T get() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?>[] classes = new Class[] { serviceInterface };

        // Get a proxy instance by dynamic proxy
        return (T) Proxy.newProxyInstance(classLoader, classes, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                InetSocketAddress serviceAddr = registry.lookup(serviceInterface.getName());
                if (log.isDebugEnabled()) {
                    log.debug("Discover a service \"{}\" with host address \"{}\" ", serviceInterface.getName(), serviceAddr.toString());
                }

                // get a channel from cache, if not found, create a new one
                Channel channel = DrpcBootstrap.CHANNELS_CACHE.get(serviceAddr);
                if (channel == null || !channel.isOpen()) {
                    channel = createChannel(serviceAddr);
                }

                // send message through the channel
                sendMessage(channel, "HELLO FROM CLIENT!!!!");

                return null;
            }
        });
    }

    public void setInterface(Class<T> referenceInterface) {
        this.serviceInterface = referenceInterface;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    /**
     * Establish a channel with the server
     * @param host client host
     * @param port client port
     * @return channel
     */
    public Channel createChannel(InetSocketAddress address) {
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
     * Send message through the channel
     * @param channel channel
     * @param message message
     * @return response
     */
    public Object sendMessage(Channel channel, String message) {
        if (!channel.isActive()) {
            log.error("Channel is inactive, can't send message");
            throw new NetworkException("Channel is inactive");
        }

        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        // Async: add a listener to the ChannelFuture which will be notified when message is sent
        channel.writeAndFlush(Unpooled.copiedBuffer(message.getBytes(StandardCharsets.UTF_8)))
                .addListener((ChannelFutureListener) promise -> {
                    // the response of 'promise' is the result of `writeAndFlush()`
                    // and once the message has been written out, 'promise' will be completed
                    // thus, we can't get server-side response with `complete()` in `CompletableFuture`

                    // TODO:
                    // what we actually need to do is, hang and expose `CompletableFuture`, wait for the response
                    // and call `complete()` in `CompletableFuture` when server-side response is arrived
                    if (!promise.isSuccess()) {
                        Throwable cause = promise.cause();
                        completableFuture.completeExceptionally(promise.cause());
                        log.error("Exception when sending message, {}", cause.getMessage());
                        throw new NetworkException("Exception when sending message: " + cause);
                    }
                });
        return null;
    }

}

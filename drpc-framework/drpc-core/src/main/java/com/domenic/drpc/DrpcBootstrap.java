package com.domenic.drpc;

import com.domenic.config.ProtocolConfig;
import com.domenic.config.ReferenceConfig;
import com.domenic.config.RegistryConfig;
import com.domenic.config.ServiceConfig;
import com.domenic.constants.NetworkConstants;
import com.domenic.discovery.Registry;
import com.domenic.discovery.builder.RegistryBuilder;
import com.domenic.netty.handler.inbound.ProviderInboundHandler;
import com.domenic.netty.initializer.ProviderChannelInitializer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Domenic
 * @Classname DrpcBootstrap
 * @Description Drpc Bootstrap
 * @Created by Domenic
 */
@Slf4j
public class DrpcBootstrap {

    /**
     * Singleton instance of DrpcBootstrap
     */
    private static final DrpcBootstrap DRPC_BOOTSTRAP = new DrpcBootstrap();

    private String appName = "default";
    private RegistryConfig registryConfig;
    private ProtocolConfig protocolConfig;

    // registry center
    private Registry registry;

    /**
     * <p>a list for published services</p>
     * <p>key: interface's fully qualified name; value: ServiceConfig</p>
     */
    private static final Map<String, ServiceConfig<?>> SERVICES = new ConcurrentHashMap<>(16);

    public static final Map<InetSocketAddress, Channel> CHANNELS_CACHE = new ConcurrentHashMap<>(16);

    /**
     * hanging exposed CompletableFuture, waiting for response
     * the key is the id of the pending request
     */
    public static final Map<Long, CompletableFuture<Object>> PENDING_REQUEST = new ConcurrentHashMap<>(128);

    private DrpcBootstrap() {
    }

    public static DrpcBootstrap getInstance() {
        return DRPC_BOOTSTRAP;
    }

    /**
     * Set the application name
     * @param appName application name
     * @return current instance
     */
    public DrpcBootstrap application(String appName) {
        this.appName = appName;
        return this;
    }

    /**
     * Config a registry center
     * @param registryConfig registry config
     * @return current instance
     */
    public DrpcBootstrap registry(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
        this.registry = new RegistryBuilder().build(registryConfig);
        return this;
    }

    /**
     * Config the protocol the service uses
     * @param protocolConfig protocol config
     * @return current instance
     */
    public DrpcBootstrap protocol(ProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
        if (log.isDebugEnabled()) {
            log.debug("The project uses protocol: {}", protocolConfig.toString());
        }
        return this;
    }

    /**
     * Publish a service (register implementation of the service to the registry center)
     * @param service service config
     * @return current instance
     */
    public DrpcBootstrap service(ServiceConfig<?> service) {
        registry.register(service);
        SERVICES.put(service.getInterface().getName(), service);
        return this;
    }

    /**
     * Publish a list of services (register implementations of the services to the registry center)
     * @param services service config
     * @return current instance
     */
    public DrpcBootstrap services(List<ServiceConfig<?>> services) {
        for (ServiceConfig<?> service : services) {
            this.service(service);
        }
        return this;
    }

    /**
     * start the service
     */
    public void start() {
        startNettyServer(NetworkConstants.HOST, NetworkConstants.PORT);
    }

    /**
     * Get the reference of the service to be consumed
     * @param reference reference config
     * @return current instance
     */
    public DrpcBootstrap reference(ReferenceConfig<?> reference) {
        reference.setRegistry(registry);
        return this;
    }

    /**
     * Start Netty server
     * @param host server host
     * @param port server port
     */
    public void startNettyServer(String host, int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // ServerBootstrap is a helper class that sets up a server
            ServerBootstrap b = new ServerBootstrap();

            // The first one, often called 'boss', accepts an incoming connection.
            // The second one, often called 'worker', handles the traffic of the accepted connection once the boss accepts the connection and registers the accepted connection to the worker.
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // listen on all available IP addresses (interfaces) for the specified port
                    // this is done by binding to the "wildcard" address (0.0.0.0 in IPv4 or :: in IPv6)
                    .localAddress(new InetSocketAddress(host, port))
                    // ChannelInitializer is a special handler that is purposed to help a user configure a new Channel.
                    .childHandler(new ProviderChannelInitializer());

            // bind and start to accept incoming connections.
            ChannelFuture f = b.bind().sync();

            if (log.isDebugEnabled()) {
                log.debug("Netty starts listening on: " + f.channel().localAddress());
            }

            // wait until the server socket is closed
            // In this example, this does not happen, but you can do that to gracefully shut down your server.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            log.error("Netty server start failed (interrupted): {}", e);
        } finally {
            // shutdown the EventLoopGroup (thread pools), and release all resources
            try {
                workerGroup.shutdownGracefully().sync();
                bossGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                log.error("Netty server shutdown failed (interrupted): {}", e);
            }
        }
    }

}

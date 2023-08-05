package com.domenic.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author Domenic
 * @Classname AppServer
 * @Description TODO
 * @Created by Domenic
 */
public class AppServer {

    private final String host;
    private final int port;

    public AppServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // ServerBootstrap is a helper class that sets up a server
            ServerBootstrap b = new ServerBootstrap();

            // The first one, often called 'boss', accepts an incoming connection.
            // The second one, often called 'worker', handles the traffic of the accepted connection once the boss accepts the connection and registers the accepted connection to the worker.
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(host, port))
                    // ChannelInitializer is a special handler that is purposed to help a user configure a new Channel.
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // ChannelPipeline is a container of ChannelHandlers.
                            // ChannelPipeline provides an API for managing the processing of inbound and outbound data on a Channel.
                            socketChannel.pipeline().addLast(new ServerChannelHandler());
                        }
                    });

            // bind and start to accept incoming connections.
            ChannelFuture f = b.bind().sync();

            System.out.println("Start listening on: " + f.channel().localAddress());

            // wait until the server socket is closed
            // In this example, this does not happen, but you can do that to gracefully shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            // shutdown the EventLoopGroup (thread pools), and release all resources
            workerGroup.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new AppServer("127.0.0.1", 18080).run();
    }

}

package com.domenic.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author Domenic
 * @Classname AppClient
 * @Description TODO
 * @Created by Domenic
 */
public class AppClient {

    private final String host;
    private final int port;

    public AppClient(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // Bootstrap is similar to ServerBootstrap
            // except that it's for non-server channels such as a client-side or connectionless channel.
            Bootstrap b = new Bootstrap();

            // If only one EventLoopGroup is specified, it will be used both as a boss group and as a worker group.
            // The boss worker is not used for the client side though.
            b.group(workerGroup)
                    // initialize a channel
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ClientChannelHandler());
                        }
                    });

            // connect to server
            // ChannelFuture represents an I/O operation which has not yet occurred (async).
            // sync() will block until the operation is complete, if operation fails, an exception will be thrown.
            ChannelFuture f = b.connect(host, port).sync();

            System.out.println("Connect to Server at: " + f.channel().remoteAddress());

            // send something to server
            f.channel().writeAndFlush(Unpooled.copiedBuffer("Hello from Domenic", StandardCharsets.UTF_8));

            // wait until the connection is closed
            f.channel().closeFuture().sync();
        } finally {
            // shutdown the EventLoopGroup (thread pools), and release all resources (including all created threads and internal resources)
            workerGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new AppClient("127.0.0.1", 18080).run();
    }

}

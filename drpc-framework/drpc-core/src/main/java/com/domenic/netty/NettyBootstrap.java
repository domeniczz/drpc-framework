package com.domenic.netty;

import com.domenic.netty.handler.ClientInboundHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Domenic
 * @Classname NettyBootstrap
 * @Description Netty Bootstrap
 * @Created by Domenic
 */
public class NettyBootstrap {

    private static final Bootstrap BOOTSTRAP = new Bootstrap();

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        // If only one EventLoopGroup is specified, it will be used both as a boss group and as a worker group.
        BOOTSTRAP.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ClientInboundHandler());
                    }
                });
    }

    private NettyBootstrap() {
    }

    /**
     * Get a singleton instance of NettyBootstrap
     * @return NettyBootstrap
     */
    public static Bootstrap getInstance() {
        return BOOTSTRAP;
    }

}

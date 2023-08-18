package com.domenic.netty.handler;

import com.domenic.netty.handler.consumer.ConsumerInboundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author Domenic
 * @Classname ConsumerChannelInitializer
 * @Description Consumer Channel Initializer
 * @Created by Domenic
 */
public class ConsumerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new ConsumerInboundHandler());
    }

}

package com.domenic.netty.initializer;

import com.domenic.netty.handler.provider.MessageDecoderHandler;
import com.domenic.netty.handler.provider.MethodCallHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Domenic
 * @Classname ProviderChannelInitializer
 * @Description Provider Channel Initializer
 * @Created by Domenic
 */
public class ProviderChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // ChannelPipeline is a container of ChannelHandlers.
        // ChannelPipeline provides an API for managing the processing of inbound and outbound data on a Channel.
        socketChannel.pipeline()
                .addLast(new LoggingHandler(LogLevel.DEBUG))
                .addLast(new MessageDecoderHandler())
                .addLast(new MethodCallHandler());
        // .addLast(new ProviderInboundHandler());
    }

}

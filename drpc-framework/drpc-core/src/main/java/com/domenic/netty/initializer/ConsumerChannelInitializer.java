package com.domenic.netty.initializer;

import com.domenic.netty.handler.consumer.MessageEncoderHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Domenic
 * @Classname ConsumerChannelInitializer
 * @Description Consumer Channel Initializer
 * @Created by Domenic
 */
public class ConsumerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                // Netty build-in logging handler
                .addLast(new LoggingHandler(LogLevel.DEBUG))
                .addLast(new MessageEncoderHandler());
        // .addLast(new ConsumerInboundHandler());
    }

}

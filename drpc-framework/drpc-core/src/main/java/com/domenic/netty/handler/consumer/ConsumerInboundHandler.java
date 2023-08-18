package com.domenic.netty.handler.consumer;

import com.domenic.drpc.DrpcBootstrap;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Domenic
 * @Classname ClientInboundHandler
 * @Description Client Inbound Handler
 * @Created by Domenic
 */
@Slf4j
public class ConsumerInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf msg) throws Exception {
        String response = msg.toString(StandardCharsets.UTF_8);
        log.info("Client received: {}", response);
        DrpcBootstrap.PENDING_REQUEST.get(1L).complete(response);
    }

}

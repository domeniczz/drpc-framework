package com.domenic.netty.handler;

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
public class ClientInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf msg) throws Exception {
        log.info("Client received: {}", msg.toString(StandardCharsets.UTF_8));
    }

}

package com.domenic.netty.handler;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Domenic
 * @Classname ServerInboundHandler
 * @Description Server Inbound Handler
 * @Created by Domenic
 */
@Slf4j
public class ServerInboundHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext context, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg;
        log.info("Server received: {}", m.toString(StandardCharsets.UTF_8));

        context.channel().writeAndFlush(Unpooled.copiedBuffer("Server Received: " + LocalDateTime.now(), StandardCharsets.UTF_8));
    }

}

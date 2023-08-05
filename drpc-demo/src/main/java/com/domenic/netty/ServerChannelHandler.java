package com.domenic.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author Domenic
 * @Classname ServerChannelHandler
 * @Description TODO
 * @Created by Domenic
 */
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg;
        System.out.println("Server received: " + m.toString(StandardCharsets.UTF_8));
        ChannelFuture f = ctx.writeAndFlush(Unpooled.copiedBuffer("Hello from Server", StandardCharsets.UTF_8));
        // add a listener to close the channel when the write operation is done
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}

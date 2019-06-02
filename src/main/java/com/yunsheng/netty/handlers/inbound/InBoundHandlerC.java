package com.yunsheng.netty.handlers.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class InBoundHandlerC extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("InBoundHandlerC: " + msg);

        // 这里不能再往下传了
        // 处理，写数据。就会调起下一个OutBound的handler
        ctx.channel().writeAndFlush(msg);
    }
}

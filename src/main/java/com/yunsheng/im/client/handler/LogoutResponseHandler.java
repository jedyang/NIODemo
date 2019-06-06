package com.yunsheng.im.client.handler;

import com.yunsheng.im.protocol.command.LogoutResponsePacket;
import com.yunsheng.im.util.SessionUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) {
        System.out.println("退出登录");
        SessionUtil.unBindChannel(ctx.channel());
    }
}

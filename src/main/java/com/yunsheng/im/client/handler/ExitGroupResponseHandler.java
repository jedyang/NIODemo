package com.yunsheng.im.client.handler;

import com.yunsheng.im.protocol.command.ExitGroupResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author uncleY
 * @date 2019/6/5 11:58
 */
public class ExitGroupResponseHandler extends SimpleChannelInboundHandler<ExitGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExitGroupResponsePacket msg) throws Exception {
        System.out.println(msg.getExitUserName() + "退出聊天组：" + msg.getGroupId());
        System.out.println("当前群组成员：" + msg.getUserNames());
    }
}

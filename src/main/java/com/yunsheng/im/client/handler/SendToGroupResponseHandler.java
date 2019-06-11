package com.yunsheng.im.client.handler;

import com.yunsheng.im.protocol.command.ListGroupResponsePacket;
import com.yunsheng.im.protocol.command.SendToGroupResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: 接受群消息
 * @author uncleY
 * @date 2019/6/5 11:58
 */
public class SendToGroupResponseHandler extends SimpleChannelInboundHandler<SendToGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SendToGroupResponsePacket msg) throws Exception {
        if (msg.isResult()) {
            System.out.println("收到群消息:" + msg.getFromGroup() + " || " + msg.getMsg());
        }
    }
}

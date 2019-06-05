package com.yunsheng.im.client.handler;

import com.yunsheng.im.protocol.command.CreateGroupResponsePacket;
import com.yunsheng.im.protocol.command.JoinGroupResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: TODO
 * @author uncleY
 * @date 2019/6/5 11:58
 */
public class JoinGroupResponseHandler extends SimpleChannelInboundHandler<JoinGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponsePacket msg) throws Exception {
        System.out.println(msg.getJoinedUserName() + "加入聊天组：" + msg.getGroupId());
        System.out.println("群组成员：" + msg.getUserNames());
    }
}

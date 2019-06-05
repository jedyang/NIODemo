package com.yunsheng.im.client.handler;

import com.yunsheng.im.protocol.command.CreateGroupResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: TODO
 * @author uncleY
 * @date 2019/6/5 11:58
 */
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket msg) throws Exception {
        System.out.println("创建聊天组：" + msg.getGroupId());
        System.out.println("群组成员：" + msg.getUserNameList());
    }
}

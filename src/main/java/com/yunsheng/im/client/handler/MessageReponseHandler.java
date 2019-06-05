package com.yunsheng.im.client.handler;

import com.yunsheng.im.protocol.command.MessageRequestPacket;
import com.yunsheng.im.protocol.command.MessageResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: 客户端处理接收消息的handler
 * 关键在于这个泛型
 * @author uncleY
 * @date 2019/6/2 17:00
 */
public class MessageReponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket msg) throws Exception {
        System.out.println("[" + msg.getFromUserName() + "] : " + msg.getMessage());
    }
}

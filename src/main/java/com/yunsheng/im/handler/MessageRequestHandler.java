package com.yunsheng.im.handler;

import com.yunsheng.im.protocol.command.Codec;
import com.yunsheng.im.protocol.command.LoginRequestPacket;
import com.yunsheng.im.protocol.command.LoginResponsePacket;
import com.yunsheng.im.protocol.command.MessageRequestPacket;
import com.yunsheng.im.protocol.command.MessageResponsePacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: 处理接收消息的handler
 * 关键在于这个泛型
 * @author uncleY
 * @date 2019/6/2 17:00
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {
        System.out.println("接收到客户端消息");

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage("服务端返回的消息");

        ctx.channel().writeAndFlush(messageResponsePacket);

    }
}

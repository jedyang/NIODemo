package com.yunsheng.im.server.handler;

import com.yunsheng.im.protocol.command.Codec;
import com.yunsheng.im.protocol.command.LoginRequestPacket;
import com.yunsheng.im.protocol.command.LoginResponsePacket;
import com.yunsheng.im.protocol.command.MessageRequestPacket;
import com.yunsheng.im.protocol.command.MessageResponsePacket;
import com.yunsheng.im.util.SessionUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import static com.yunsheng.im.util.SessionUtil.SESSION_KEY;

/**
 * @description: 处理接收消息的handler
 * 关键在于这个泛型
 * @author uncleY
 * @date 2019/6/2 17:00
 */
@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    private MessageRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {
        System.out.println("接收到客户端消息");

        // 从channel上拿到这个链接的用户
        String userName = (String) ctx.channel().attr(SESSION_KEY).get();

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage(msg.getMessage());
        messageResponsePacket.setFromUserName(userName);

        // 拿到消息接收方的channel
        Channel toUserChannel = SessionUtil.getChannel(msg.getToUserName());
        toUserChannel.writeAndFlush(messageResponsePacket);

    }
}

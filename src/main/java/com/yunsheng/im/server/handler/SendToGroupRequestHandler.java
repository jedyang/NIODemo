package com.yunsheng.im.server.handler;

import com.yunsheng.im.protocol.command.ListGroupRequestPacket;
import com.yunsheng.im.protocol.command.ListGroupResponsePacket;
import com.yunsheng.im.protocol.command.SendToGroupRequestPacket;
import com.yunsheng.im.protocol.command.SendToGroupResponsePacket;
import com.yunsheng.im.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * @description: 群聊消息
 * @author uncleY
 * @date 2019/6/5 16:10
 */
@ChannelHandler.Sharable
public class SendToGroupRequestHandler extends SimpleChannelInboundHandler<SendToGroupRequestPacket> {
    public static final SendToGroupRequestHandler INSTANCE = new SendToGroupRequestHandler();

    private SendToGroupRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SendToGroupRequestPacket request) throws Exception {
        SendToGroupResponsePacket responsePacket = new SendToGroupResponsePacket();

        ChannelGroup channelGroup = SessionUtil.getChannelGroup(request.getGroupId());

        if (null == channelGroup){
            responsePacket.setResult(false);
            responsePacket.setMsg("没有查询到该群");
            ctx.channel().writeAndFlush(responsePacket);
            return;
        }


        responsePacket.setResult(true);
        responsePacket.setFromGroup(request.getGroupId());
        responsePacket.setMsg(request.getMsg());

        channelGroup.writeAndFlush(responsePacket);
    }
}

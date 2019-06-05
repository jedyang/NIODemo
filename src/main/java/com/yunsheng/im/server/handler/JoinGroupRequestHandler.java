package com.yunsheng.im.server.handler;

import com.yunsheng.im.protocol.command.JoinGroupRequestPacket;
import com.yunsheng.im.protocol.command.JoinGroupResponsePacket;
import com.yunsheng.im.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * @description: TODO
 * @author uncleY
 * @date 2019/6/5 16:10
 */
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket msg) throws Exception {

        ChannelGroup channelGroup = SessionUtil.getChannelGroup(msg.getGroupId());

        channelGroup.add(ctx.channel());


        List<String> userNamesResponse = new ArrayList<>();
        for (Channel channel : channelGroup) {
            String userName = (String) channel.attr(SessionUtil.SESSION_KEY).get();
            userNamesResponse.add(userName);
        }

        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();
        responsePacket.setJoinedUserName((String) ctx.channel().attr(SessionUtil.SESSION_KEY).get());
        responsePacket.setUserNames(userNamesResponse);
        responsePacket.setGroupId(msg.getGroupId());

        channelGroup.writeAndFlush(responsePacket);
    }
}

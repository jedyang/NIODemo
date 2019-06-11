package com.yunsheng.im.server.handler;

import com.yunsheng.im.protocol.command.JoinGroupRequestPacket;
import com.yunsheng.im.protocol.command.JoinGroupResponsePacket;
import com.yunsheng.im.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * @description: 加入群聊
 * @author uncleY
 * @date 2019/6/5 16:10
 */
@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {
    public static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();

    private JoinGroupRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket msg) throws Exception {
        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();

        ChannelGroup channelGroup = SessionUtil.getChannelGroup(msg.getGroupId());

        if (null == channelGroup){
            responsePacket.setResult(false);
            responsePacket.setMsg("没有查询到该群");
            ctx.channel().writeAndFlush(responsePacket);
            return;
        }

        channelGroup.add(ctx.channel());


        List<String> userNamesResponse = new ArrayList<>();
        for (Channel channel : channelGroup) {
            String userName = (String) channel.attr(SessionUtil.SESSION_KEY).get();
            userNamesResponse.add(userName);
        }

        responsePacket.setResult(true);
        responsePacket.setJoinedUserName((String) ctx.channel().attr(SessionUtil.SESSION_KEY).get());
        responsePacket.setUserNames(userNamesResponse);
        responsePacket.setGroupId(msg.getGroupId());

        channelGroup.writeAndFlush(responsePacket);
    }
}

package com.yunsheng.im.server.handler;

import com.yunsheng.im.protocol.command.ListGroupRequestPacket;
import com.yunsheng.im.protocol.command.ListGroupResponsePacket;
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
public class ListGroupRequestHandler extends SimpleChannelInboundHandler<ListGroupRequestPacket> {
    public static final ListGroupRequestHandler INSTANCE = new ListGroupRequestHandler();

    private ListGroupRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupRequestPacket msg) throws Exception {
        ListGroupResponsePacket responsePacket = new ListGroupResponsePacket();

        ChannelGroup channelGroup = SessionUtil.getChannelGroup(msg.getGroupId());

        if (null == channelGroup){
            responsePacket.setResult(false);
            responsePacket.setMsg("没有查询到该群");
            ctx.channel().writeAndFlush(responsePacket);
            return;
        }


        List<String> userNamesResponse = new ArrayList<>();
        for (Channel channel : channelGroup) {
            String userName = (String) channel.attr(SessionUtil.SESSION_KEY).get();
            userNamesResponse.add(userName);
        }

        responsePacket.setResult(true);
        responsePacket.setUserNames(userNamesResponse);
        responsePacket.setGroupId(msg.getGroupId());

        ctx.channel().writeAndFlush(responsePacket);
    }
}

package com.yunsheng.im.server.handler;

import com.yunsheng.im.protocol.command.ExitGroupRequestPacket;
import com.yunsheng.im.protocol.command.ExitGroupResponsePacket;
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
 * @description: 退群请求处理
 * @author uncleY
 * @date 2019/6/5 16:10
 */
@ChannelHandler.Sharable
public class ExitGroupRequestHandler extends SimpleChannelInboundHandler<ExitGroupRequestPacket> {

    public static final ExitGroupRequestHandler INSTANCE = new ExitGroupRequestHandler();

    private ExitGroupRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExitGroupRequestPacket msg) throws Exception {

        ChannelGroup channelGroup = SessionUtil.getChannelGroup(msg.getGroupId());

        // 从channelGroup移除
        channelGroup.remove(ctx.channel());


        List<String> userNamesResponse = new ArrayList<>();
        for (Channel channel : channelGroup) {
            String userName = (String) channel.attr(SessionUtil.SESSION_KEY).get();
            userNamesResponse.add(userName);
        }

        ExitGroupResponsePacket responsePacket = new ExitGroupResponsePacket();
        responsePacket.setExitUserName((String) ctx.channel().attr(SessionUtil.SESSION_KEY).get());
        responsePacket.setUserNames(userNamesResponse);
        responsePacket.setGroupId(msg.getGroupId());

        channelGroup.writeAndFlush(responsePacket);
    }
}

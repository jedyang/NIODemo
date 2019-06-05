package com.yunsheng.im.server.handler;

import com.yunsheng.im.protocol.command.CreateGroupRequestPacket;
import com.yunsheng.im.protocol.command.CreateGroupResponsePacket;
import com.yunsheng.im.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

/**
 * @description: 创建用户组请求处理
 * @author uncleY
 * @date 2019/6/5 11:03
 */
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket msg) throws Exception {
        System.out.println("收到创建组请求");
        List<String> userNames = msg.getUserNames();
        List<String> userNamesResponse = new ArrayList<>();
        // 创建一个ChannelGroup组，方便统一管理一组channel
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        // 取到用户对应的channel
        for (String userName : userNames) {

            Channel channel = SessionUtil.getChannel(userName);
            if (null != channel) {
                channelGroup.add(channel);
                userNamesResponse.add(userName);
            }
        }

        // 加上自己
        channelGroup.add(ctx.channel());
        String selfName = (String) ctx.channel().attr(SessionUtil.SESSION_KEY).get();
        userNamesResponse.add(selfName);


        int groupId = new Random().nextInt(100);
        // 将群信息保存起来
        SessionUtil.bindChannelGroup(String.valueOf(groupId), channelGroup);

        CreateGroupResponsePacket responsePacket = new CreateGroupResponsePacket();
        responsePacket.setGroupId(String.valueOf(groupId));
        responsePacket.setSuccess(true);
        responsePacket.setUserNameList(userNamesResponse);
        channelGroup.writeAndFlush(responsePacket);

    }
}

package com.yunsheng.im.client.handler;

import com.yunsheng.im.protocol.command.JoinGroupResponsePacket;
import com.yunsheng.im.protocol.command.ListGroupResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: TODO
 * @author uncleY
 * @date 2019/6/5 11:58
 */
public class ListGroupResponseHandler extends SimpleChannelInboundHandler<ListGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupResponsePacket msg) throws Exception {
        if (msg.isResult()) {
            System.out.println("群组成员:" + msg.getUserNames());
        } else {
            System.out.println("查询失败:" + msg.getMsg());
        }
    }
}

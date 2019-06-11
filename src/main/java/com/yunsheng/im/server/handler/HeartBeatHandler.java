package com.yunsheng.im.server.handler;

import com.yunsheng.im.protocol.command.HeartBeatRequestPacket;
import com.yunsheng.im.protocol.command.HeartBeatResponsePacket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: 服务端回复心跳，证明自己还活着
 * @author uncleY
 * @date 2019/6/11 17:02
 */
@ChannelHandler.Sharable
public class HeartBeatHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {
    public static final HeartBeatHandler INSTANCE = new HeartBeatHandler();

    private HeartBeatHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket msg) throws Exception {
        System.out.println("收到客户端心跳");
        ctx.channel().writeAndFlush(new HeartBeatResponsePacket());
    }
}

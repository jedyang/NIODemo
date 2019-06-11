package com.yunsheng.im.client.handler;

import com.yunsheng.im.protocol.command.HeartBeatRequestPacket;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description: TODO
 * @author uncleY
 * @date 2019/6/11 16:37
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    // 连接建立后，开始定时发送心跳
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        scheduleSendHeartBeat(ctx);

        super.channelActive(ctx);
    }

    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
//        schedule是只延迟执行一次，所以要重复调用自己
        // 不用scheduleAtFixedRate是因为，scheduleAtFixedRate是一直循环下去
        // scheduleAtFixedRate执行定时发心跳的future在channelInactive中应该cancel，
        // 否则连接都断了，还会定时执行。如果客户端连接断了程序直接退出就没问题，
        // 但是客户端如果有重连机制，程序一直跑着，就会有很多无效的定时任务再跑
        ctx.executor().schedule(() -> {

            if (ctx.channel().isActive()) {
                System.out.println("发送心跳报文");
                ctx.writeAndFlush(new HeartBeatRequestPacket());
                scheduleSendHeartBeat(ctx);
            }

        }, 5, TimeUnit.SECONDS);
    }
}

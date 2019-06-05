package com.yunsheng.im.server.handler;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description: 统计连接数
 * @author uncleY
 * @date 2019/6/4 9:39
 */
public class CountHandler extends ChannelInboundHandlerAdapter{
    public static AtomicInteger connectNum = new AtomicInteger(0);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connectNum.getAndIncrement();
//        System.out.println("当前连接数:" + connectNum);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        connectNum.decrementAndGet();
//        System.out.println("当前连接数减为:" + connectNum);
        super.channelInactive(ctx);
    }
}

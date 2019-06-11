package com.yunsheng.im.server.handler;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @description: 空闲检测
 * @author uncleY
 * @date 2019/6/11 16:28
 */
public class IMIdleStateHandler extends IdleStateHandler {

    public IMIdleStateHandler() {
//        第一个表示读空闲时间，指的是在这段时间内如果没有数据读到，就表示连接假死；第二个是写空闲时间，指的是 在这段时间如果没有写数据，就表示连接假死；第三个参数是读写空闲时间，表示在这段时间内如果没有产生数据读或者写，就表示连接假死。写空闲和读写空闲为0，表示我们不关心者两类条件；
        // 15秒没有收到数据，判定为空闲连接
        super(15, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println("15秒未收到数据，断开连接");
        ctx.channel().close();
    }
}

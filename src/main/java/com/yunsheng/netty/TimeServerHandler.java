package com.yunsheng.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by shengyun on 17/6/7.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    // channelActive是当连接建立完成开始调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 4字节是因为我们准备返回一个32位的int，表示时间
        ByteBuf time = ctx.alloc().buffer(4);
        // 2208988800为1900年1月1日00:00:00~1970年1月1日00:00:00的总秒数
        // 不需要像原生NIO那样flip。netty有自己的实现方式。
        time.writeInt((int)(System.currentTimeMillis() / 1000 + 2208988800L));

        final ChannelFuture f = ctx.writeAndFlush(time);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                assert f == channelFuture;
                // 在netty中，所有操作都是异步的。
                // 所以，context的close一定要放在operationComplete中
                // 并且注意close已不是立刻执行的，它返回的也是一个future
                ctx.close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

package com.yunsheng.netty.base;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by shengyun on 17/6/1.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    // ChannelInboundHandlerAdapter有很多可供重写的事件

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ctx.write(msg);
        ctx.flush();

        //ReferenceCountUtil.release(msg);
        // ByteBuf是一个reference-counted对象，需要在handler中释放。
        //ByteBuf in = (ByteBuf)msg;
        //try {
        //    while (in.isReadable()) {
        //        System.out.println((char)in.readByte());
        //        System.out.flush();
        //    }
        //} finally {
        //    ReferenceCountUtil.release(msg);
        //}

        //((ByteBuf)msg).release();
        // 一般应该是在finally中做
        /*
        try {
            // Do something with msg
        } finally {
            ReferenceCountUtil.release(msg);
        }
         */
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 处理异常的方法
        // 要负责关闭连接
        ctx.close();
    }
}

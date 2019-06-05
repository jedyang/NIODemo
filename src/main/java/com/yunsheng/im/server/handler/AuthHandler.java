package com.yunsheng.im.server.handler;

import com.yunsheng.im.util.LoginUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description: 服务端的登录校验
 * 为了演示，handler是可以动态移除的
 * @author uncleY
 * @date 2019/6/4 11:05
 */
public class AuthHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!LoginUtil.hasLogin(ctx.channel())){
            ctx.channel().close();
            System.out.println("未登录，关闭连接");
            return;
        }

        // 已经登录的，要移除这个handler，不需要以后再重复校验
        ctx.pipeline().remove(this);
        super.channelRead(ctx, msg);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("已登录，移除handler");
        super.handlerRemoved(ctx);
    }
}

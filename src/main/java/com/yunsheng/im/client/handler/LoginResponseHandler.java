package com.yunsheng.im.client.handler;

import com.yunsheng.im.protocol.command.LoginRequestPacket;
import com.yunsheng.im.protocol.command.LoginResponsePacket;
import com.yunsheng.im.util.LoginUtil;

import java.util.UUID;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: 客户端处理登录响应的handler
 * 关键在于这个泛型
 * @author uncleY
 * @date 2019/6/2 17:00
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        // 创建登录对象
//        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
//        loginRequestPacket.setUserId(123);
//        loginRequestPacket.setUsername("yunsheng");
////        loginRequestPacket.setUsername("uncleY");
//        loginRequestPacket.setPassword("123456");
//
//        System.out.println("发起登录");
//
//        // 写数据
//        ctx.channel().writeAndFlush(loginRequestPacket);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) throws Exception {
        System.out.println("登录结果：" + msg.isSuccess());
        // 打上已登录标签
        LoginUtil.markAsLogin(ctx.channel());
    }
}

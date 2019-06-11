package com.yunsheng.im.server.handler;

import com.yunsheng.im.protocol.command.LoginRequestPacket;
import com.yunsheng.im.protocol.command.LoginResponsePacket;
import com.yunsheng.im.util.LoginUtil;
import com.yunsheng.im.util.SessionUtil;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: 处理登录请求的handler
 * 关键在于这个泛型
 * @author uncleY
 * @date 2019/6/2 17:00
 */
// 加上注解标识，表明该 handler 是可以多个 channel 共享的
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    private LoginRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
        // 处理登录逻辑即可，不用再判断消息类型了
        System.out.println("收到客户端登录请求:" + msg.getUsername());

        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(msg.getVersion());
        responsePacket.setSuccess(true);

        // 打上登录标签
        LoginUtil.markAsLogin(ctx.channel());

        // 将用户与连接绑定
        SessionUtil.bindChannel(msg.getUsername(), ctx.channel());
        ctx.channel().attr(SessionUtil.SESSION_KEY).set(msg.getUsername());

//        ByteBuf responseByteBuf = Codec.INSTANCE.encode(ctx.alloc(), responsePacket);
//        ctx.channel().writeAndFlush(responseByteBuf);
        // 改进后，直接write这个对象即可，因为有encodeHandler处理对象编码过程
        ctx.channel().writeAndFlush(responsePacket);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 断开连接时要解绑
        SessionUtil.unBindChannel(ctx.channel());
    }
}

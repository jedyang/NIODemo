package com.yunsheng.im.handler;

import com.yunsheng.im.protocol.command.Codec;
import com.yunsheng.im.protocol.command.LoginRequestPacket;
import com.yunsheng.im.protocol.command.LoginResponsePacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: 处理登录请求的handler
 * 关键在于这个泛型
 * @author uncleY
 * @date 2019/6/2 17:00
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
        // 处理登录逻辑即可，不用再判断消息类型了

        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(msg.getVersion());
        responsePacket.setSuccess(true);
//        ByteBuf responseByteBuf = Codec.INSTANCE.encode(ctx.alloc(), responsePacket);
//        ctx.channel().writeAndFlush(responseByteBuf);
        // 改进后，直接write这个对象即可，因为有encodeHandler处理对象编码过程
        ctx.channel().writeAndFlush(responsePacket);

    }
}

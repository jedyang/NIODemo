package com.yunsheng.im.server;

import com.yunsheng.im.protocol.command.Codec;
import com.yunsheng.im.protocol.command.LoginRequestPacket;
import com.yunsheng.im.protocol.command.LoginResponsePacket;
import com.yunsheng.im.protocol.command.MessageRequestPacket;
import com.yunsheng.im.protocol.command.MessageResponsePacket;
import com.yunsheng.im.protocol.command.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author uncleY
 * @description: TODO
 * @date 2019/6/2 10:08
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接收到客户端请求");
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet decode = Codec.INSTANCE.decode(byteBuf);

        ByteBuf responseByteBuf;
        if (decode instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) decode;
            System.out.println(loginRequestPacket.getUsername() + ":" + loginRequestPacket.getPassword());

            LoginResponsePacket responsePacket = new LoginResponsePacket();
            responsePacket.setVersion(decode.getVersion());
            responsePacket.setSuccess(true);
            // 回写给客户端
            responseByteBuf = Codec.INSTANCE.encode(ctx.alloc(), responsePacket);

        } else if (decode instanceof MessageRequestPacket) {
            System.out.println("客户端消息:" + ((MessageRequestPacket) decode).getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端收到你的消息");
            responseByteBuf = Codec.INSTANCE.encode(ctx.alloc(), messageResponsePacket);

        } else {
            System.out.println("异常请求");
            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("异常消息");
            responseByteBuf = Codec.INSTANCE.encode(ctx.alloc(), messageResponsePacket);

        }


        ctx.channel().writeAndFlush(responseByteBuf);
    }
}

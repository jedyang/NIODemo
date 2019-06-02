package com.yunsheng.im.client;

import com.yunsheng.im.protocol.command.Codec;
import com.yunsheng.im.protocol.command.LoginRequestPacket;
import com.yunsheng.im.protocol.command.LoginResponsePacket;
import com.yunsheng.im.protocol.command.MessageResponsePacket;
import com.yunsheng.im.protocol.command.Packet;
import com.yunsheng.im.util.LoginUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * @author uncleY
 * @description: 客户端通信handler
 * @date 2019/6/2 9:48
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    // 模拟同server建立好连接之后，自动登录
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("发起登录");

        LoginRequestPacket packet = new LoginRequestPacket();
        packet.setUserId(123);
        packet.setUsername("yunsheng");
        packet.setPassword("123456");

        // 编码
//        然后把 ByteBuf 分配器抽取出一个参数，这里第一个实参 ctx.alloc() 获取的就是与当前连接相关的 ByteBuf 分配器，建议这样来使用。
        ByteBuf encode = Codec.INSTANCE.encode(ctx.alloc(), packet);

        ctx.channel().writeAndFlush(encode);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf responseByteBuf = (ByteBuf) msg;

        Packet decode = Codec.INSTANCE.decode(responseByteBuf);

        if (decode instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) decode;
            System.out.println("登录结果:" + loginResponsePacket.isSuccess());
            System.out.println("msg:" + loginResponsePacket.getReason());

            // 如果是登录成功，保存登录状态
            if (loginResponsePacket.isSuccess()) {
                LoginUtil.markAsLogin(ctx.channel());
            }

        } else {
            // 服务端回传的消息
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) decode;
            System.out.println(messageResponsePacket.getMessage());
        }
    }
}

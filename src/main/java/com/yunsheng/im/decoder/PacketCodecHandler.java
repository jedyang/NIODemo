package com.yunsheng.im.decoder;

import com.yunsheng.im.protocol.command.Codec;
import com.yunsheng.im.protocol.command.Packet;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

/**
 * @description: 将编解码统一在一个类里
 * @author uncleY
 * @date 2019/6/10 11:53
 */
@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {
    public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();

    private PacketCodecHandler() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {

        ByteBuf buffer = ctx.channel().alloc().ioBuffer();
        Codec.INSTANCE.encode4handler(buffer, msg);

        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(Codec.INSTANCE.decode(msg));
    }
}

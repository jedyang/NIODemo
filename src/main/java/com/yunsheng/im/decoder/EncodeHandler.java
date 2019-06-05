package com.yunsheng.im.decoder;

import com.yunsheng.im.protocol.command.Codec;
import com.yunsheng.im.protocol.command.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description: 编码handler
 * @author uncleY
 * @date 2019/6/2 17:28
 */
public class EncodeHandler extends MessageToByteEncoder<Packet>{
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        // 这里直接把out传过去，就不需要再自己分配Bytebuf了
        Codec.INSTANCE.encode4handler(out,msg);
    }
}

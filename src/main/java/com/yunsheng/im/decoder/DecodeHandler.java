package com.yunsheng.im.decoder;

import com.yunsheng.im.protocol.command.Codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author uncleY
 * @description: 解码handler
 * @date 2019/6/2 16:50
 */
public class DecodeHandler extends ByteToMessageDecoder {
    /**
     * 解码的结果放到list里面，会自动传递到下一个handler
     * 使用 ByteToMessageDecoder，Netty 会自动进行内存的释放，
     * 我们不用操心太多的内存管理方面的逻辑
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(Codec.INSTANCE.decode(in));
    }
}

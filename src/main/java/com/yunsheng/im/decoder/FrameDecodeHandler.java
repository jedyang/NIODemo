package com.yunsheng.im.decoder;

import com.yunsheng.im.protocol.command.Codec;

import java.util.concurrent.atomic.AtomicLong;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @description: 自定义拆包器
 * @author uncleY
 * @date 2019/6/3 11:22
 */
public class FrameDecodeHandler extends LengthFieldBasedFrameDecoder {
    public static AtomicLong inByteTotal = new AtomicLong(0);

    public FrameDecodeHandler() {
        super(Integer.MAX_VALUE, 7, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
//        这里的 decode() 方法中，第二个参数 in，每次传递进来的时候，均为一个数据包的开头
        // netty为我们保证了这点
        // 所以不用担心，因为粘包的原因导致取到的魔数不对
        // 注意用的是getInt，不是readInt。getInt不会改变读索引
        if (in.getInt(in.readerIndex()) != Codec.MAGIC_NUMBER) {
            // 魔数不对，直接关闭
            ctx.channel().close();
            return null;
        }

        // 增加本次入口流量
        int inByteNum = in.writerIndex() - in.readerIndex();
        inByteTotal.addAndGet(inByteNum);

        return super.decode(ctx, in);
    }
}

package com.yunsheng.netty.demo;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author uncleY
 * @description: 客户端可重连
 * @date 2019/5/31 15:42
 */
public class ClientReConnect {
    private static final int MAX_RETRY = 5;

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                // 指定IO为NIO
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // pipline 责任链模式
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                })
                // 设置自定义属性，和server一样
                .attr(AttributeKey.newInstance("fromClientKey"), "fromClientValue")
                // 设置客户端连接的TCP配置
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)//表示连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.SO_KEEPALIVE, true)//开启 TCP 底层心跳机制
                .option(ChannelOption.TCP_NODELAY, true)
        ;

        connect(bootstrap, "127.0.0.1", 8000, MAX_RETRY);

    }

    // 加一个失败重连功能
    // 通常情况下，连接建立失败不会立即重新连接，而是会通过一个指数退避的方式，
    // 比如每隔 1 秒、2 秒、4 秒、8 秒，以 2 的幂次来建立连接，然后到达一定次数之后就放弃连接
    private static ChannelFuture connect(Bootstrap bootstrap, String host, int port, int retry) {
        ChannelFuture channelFuture = bootstrap.connect(host, port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("连接成功!");
                } else if (retry == 0) {
                    System.err.println("连接失败!");
                } else {
                    // 第几次重连
                    int order = MAX_RETRY - retry + 1;
                    System.err.println("连接失败!,进行第" + order + "次重连");
                    // 本次重连的间隔,左移操作
                    int delay = 1 << order;
//                    bootstrap.config() 这个方法返回的是 BootstrapConfig，
//  他是对 Bootstrap 配置参数的抽象，
// 然后 bootstrap.config().group() 返回的就是我们在一开始的时候配置的线程模型 workerGroup，
// 调 workerGroup 的 schedule 方法即可实现定时任务逻辑。
                    bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
//                    connect(bootstrap, "127.0.0.1", 8000, retry - 1);
                }
            }
        });

        return channelFuture;
    }

}
// 服务端和客户端都是ChannelInboundHandlerAdapter，都是Inbound
// 是因为我们要处理进来的数据，所以就用Inbound
// 在向外发送时，是会自动经过一个OutBound的handler
class FirstClientHandler extends ChannelInboundHandlerAdapter {
    //    这个方法会在客户端连接建立成功之后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端向服务端发送数据");

        // 1. 获取数据流
        // Netty的服务端和客户端的数据交互是通过ByteBuf
        ByteBuf buffer = getByteBuf(ctx);

        // 2. 写数据
// 貌似加不加.channel()都行，后面调用的都是pipeline.writeAndFlush(msg)
//        ctx.channel().writeAndFlush(buffer);
        ctx.writeAndFlush(buffer);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = "你好，Netty!".getBytes(Charset.forName("utf-8"));

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }

    // 读取服务端回传的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("收到服务端消息：" + byteBuf.toString(Charset.forName("utf-8")));
    }
}

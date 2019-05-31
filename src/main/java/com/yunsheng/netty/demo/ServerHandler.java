package com.yunsheng.netty.demo;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author uncleY
 * @description: Netty处理客户端数据的简单例子
 * @date 2019/5/30 14:44
 */
public class ServerHandler {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 老板负责接活
        NioEventLoopGroup boss = new NioEventLoopGroup();
        // 员工负责干活
        NioEventLoopGroup worker = new NioEventLoopGroup();

        // 最小化参数的netty_server例子
        // 只需要设定group、channel、handler
        // 然后bind端口
        serverBootstrap.group(boss, worker)
                // 通过channel指定IO模型为NIO
                .channel(NioServerSocketChannel.class)
                // 具体的读写、业务处理逻辑在handler里
                // 注意是childHandler，是处理客户端连接后的逻辑
                // 还有一个是handler()方法，是处理服务端启动过程中的逻辑，一般用不到
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new FirstServerHandler());
                    }
                }).bind(8000);
    }

}
class FirstServerHandler extends ChannelInboundHandlerAdapter {
    // 当连接中有数据可读时会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + " 接受到消息：" + byteBuf.toString(Charset.forName("UTF-8")));

        // 回传数据
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes("服务端回传的消息".getBytes(Charset.forName("UTF-8")));
        ctx.channel().writeAndFlush(buffer);
    }
}
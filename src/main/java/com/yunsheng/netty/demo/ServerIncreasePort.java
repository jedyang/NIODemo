package com.yunsheng.netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author uncleY
 * @description: 演示端口绑定失败时，自增继续尝试绑定
 * @date 2019/5/31 14:00
 */
public class ServerIncreasePort {
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
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                });

        bindPort(serverBootstrap, 100);
    }

    // bind返回的是个ChannelFuture，可以加listener进行处理
    private static void bindPort(ServerBootstrap serverBootstrap, int port) {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("端口[" + port + "]绑定成功!");
                } else {
                    System.err.println("端口[" + port + "]绑定失败!");
                    bindPort(serverBootstrap, port + 1);
                }
            }
        });
    }
}

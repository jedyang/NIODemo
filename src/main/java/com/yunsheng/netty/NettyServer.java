package com.yunsheng.netty;

import com.yunsheng.netty.handlers.LifeCyCleTestHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author uncleY
 * @description: Netty的简单例子
 * @date 2019/5/30 14:44
 */
public class NettyServer {
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
                        ch.pipeline().addLast(new LifeCyCleTestHandler());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                }).bind(8000).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("服务端启动成功");
            } else {
                System.out.println("服务端启动异常");
            }
        });
    }
}

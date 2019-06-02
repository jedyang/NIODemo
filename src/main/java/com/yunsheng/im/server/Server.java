package com.yunsheng.im.server;

import com.yunsheng.im.handler.DecodeHandler;
import com.yunsheng.im.handler.EncodeHandler;
import com.yunsheng.im.handler.LoginRequestHandler;
import com.yunsheng.im.handler.MessageRequestHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author uncleY
 * @description: TODO
 * @date 2019/6/2 10:02
 */
public class Server {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new DecodeHandler());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new EncodeHandler());
                    }
                }).bind(8000).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("服务端在8000端口开启。。。");
            } else {
                System.err.println("服务端启动失败");
            }
        });

    }
}

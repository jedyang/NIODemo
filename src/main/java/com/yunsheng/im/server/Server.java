package com.yunsheng.im.server;

import com.yunsheng.im.server.handler.AuthHandler;
import com.yunsheng.im.decoder.DecodeHandler;
import com.yunsheng.im.decoder.EncodeHandler;
import com.yunsheng.im.server.handler.CreateGroupRequestHandler;
import com.yunsheng.im.server.handler.ExitGroupRequestHandler;
import com.yunsheng.im.server.handler.JoinGroupRequestHandler;
import com.yunsheng.im.server.handler.ListGroupRequestHandler;
import com.yunsheng.im.server.handler.LoginRequestHandler;
import com.yunsheng.im.server.handler.MessageRequestHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

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
//                        ch.pipeline().addLast(new ServerHandler());
                        // 拆包要放在第一个handler
                        // 参数1：数据包的最大长度。参数2：数据长度字段在第几个字节开始。参数3是数据长度字段的长度
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        ch.pipeline().addLast(new DecodeHandler());
                        ch.pipeline().addLast(new CreateGroupRequestHandler());
                        ch.pipeline().addLast(new JoinGroupRequestHandler());
                        ch.pipeline().addLast(new ExitGroupRequestHandler());
                        ch.pipeline().addLast(new ListGroupRequestHandler());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new AuthHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new EncodeHandler());
                    }
                }).bind(8000).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("服务端在8000端口开启。。。");
//                worker.schedule(() -> {
//                    System.out.println("当前连接数：" + CountHandler.connectNum);
//                    System.out.println("流量统计：" + FrameDecodeHandler.inByteTotal);
//                }, 1, TimeUnit.SECONDS);
//                new Thread(() -> {
//                    Executors.newScheduledThreadPool(1).schedule(() -> {
//                        System.out.println("==============");
////            System.out.println("当前连接数：" + CountHandler.connectNum);
////            System.out.println("流量统计：" + FrameDecodeHandler.inByteTotal);
//                    }, 1, TimeUnit.SECONDS);
//
//                }).start();
            } else {
                System.err.println("服务端启动失败");
            }
        });


    }
}

package com.yunsheng.im.client;


import com.yunsheng.im.protocol.command.Codec;
import com.yunsheng.im.protocol.command.MessageRequestPacket;
import com.yunsheng.im.util.LoginUtil;

import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author uncleY
 * @description: 模拟客户端
 * @date 2019/6/2 9:45
 */
public class Client {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHandler());
                    }
                })
                .connect("127.0.0.1", 8000)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        Channel channel = ((ChannelFuture) future).channel();
                        // 连接成功之后，启动控制台线程
                        // 必须启动新线程，因为控制台输入是阻塞的
                        startConsoleThread(channel);
                    }
                });
    }

    private static void startConsoleThread(Channel channel) {
        new Thread(
                () -> {
                    // 只要线程不关闭，一直执行
                    while (!Thread.interrupted()){
                        // 判断是否登录
                        if (LoginUtil.hasLogin(channel)){
                            System.out.println("输入消息发送到服务端");
                            Scanner scanner = new Scanner(System.in);
                            String msg = scanner.nextLine();

                            MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
                            messageRequestPacket.setMessage(msg);
                            ByteBuf encode = Codec.INSTANCE.encode(channel.alloc(), messageRequestPacket);

                            channel.writeAndFlush(encode);
                        }
                    }
                }
        ).start();
    }
}

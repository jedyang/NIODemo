package com.yunsheng.im.client;


import com.yunsheng.im.client.command.ConsoleCommandManager;
import com.yunsheng.im.client.command.LoginCommand;
import com.yunsheng.im.client.handler.CreateGroupResponseHandler;
import com.yunsheng.im.client.handler.ExitGroupResponseHandler;
import com.yunsheng.im.client.handler.JoinGroupResponseHandler;
import com.yunsheng.im.client.handler.ListGroupResponseHandler;
import com.yunsheng.im.client.handler.LogoutResponseHandler;
import com.yunsheng.im.decoder.DecodeHandler;
import com.yunsheng.im.decoder.EncodeHandler;
import com.yunsheng.im.client.handler.LoginResponseHandler;
import com.yunsheng.im.client.handler.MessageReponseHandler;
import com.yunsheng.im.protocol.command.LoginRequestPacket;
import com.yunsheng.im.protocol.command.MessageRequestPacket;
import com.yunsheng.im.util.LoginUtil;

import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

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
//                        ch.pipeline().addLast(new ClientHandler());
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        ch.pipeline().addLast(new DecodeHandler());
                        ch.pipeline().addLast(new CreateGroupResponseHandler());
                        ch.pipeline().addLast(new JoinGroupResponseHandler());
                        ch.pipeline().addLast(new ExitGroupResponseHandler());
                        ch.pipeline().addLast(new ListGroupResponseHandler());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new LogoutResponseHandler());
                        ch.pipeline().addLast(new MessageReponseHandler());
                        ch.pipeline().addLast(new EncodeHandler());
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
                    Scanner scanner = new Scanner(System.in);
                    ConsoleCommandManager commandManager = new ConsoleCommandManager();
                    LoginCommand loginCommand = new LoginCommand();
                    // 只要线程不关闭，一直执行
                    while (!Thread.interrupted()) {
                        // 判断是否登录
                        if (LoginUtil.hasLogin(channel)) {

                            commandManager.exec(scanner, channel);
////                            System.out.println("发送给:");
//                            String toUserName = scanner.nextLine();
////                            System.out.println("内容:");
//                            String msg = scanner.nextLine();
//
//                            MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
//                            messageRequestPacket.setToUserName(toUserName);
//                            messageRequestPacket.setMessage(msg);
//                            channel.writeAndFlush(messageRequestPacket);

                        } else {
                            loginCommand.exec(scanner, channel);
                        }
                    }
                }
        ).start();
    }
}

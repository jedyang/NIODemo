package com.yunsheng.netty.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by shengyun on 17/6/1.
 */
public class Server {
    //static final boolean SSL = System.getProperty("ssl") != null;
    private int port;
    private ChannelInboundHandlerAdapter handler;

    public Server(int port, ChannelInboundHandlerAdapter handler) {
        this.port = port;
        this.handler = handler;
    }

    public void run() throws Exception {
        // Configure SSL.
        //final SslContext sslCtx;
        //if (SSL) {
        //    SelfSignedCertificate ssc = new SelfSignedCertificate();
        //    sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        //} else {
        //    sslCtx = null;
        //}

        // server端需要两个角色
        // boss用来接收进来的连接
        // worker用来处理已经被接收的连接。boss会将请求连接注册给某个worker

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            // ServerBootstrap是一个帮助类。
            // 否则自己写下面这套流程很麻烦。
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel做boss接受请求
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //if (sslCtx != null) {
                        //    pipeline.addLast(sslCtx.newHandler(socketChannel.alloc()));
                        //}
                        pipeline.addLast(handler);// 注册handler。是个pipline，可以add多个。
                    }
                })
                // 设置tcp/ip连接的参数
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定监听
            ChannelFuture channelFuture = b.bind(port).sync();
            System.out.println("tcp server started...");

            // 关闭之前，等待所有server socker已经关闭
            // 本例子不会发生
            channelFuture.channel().closeFuture().sync();
            System.out.println("tcp server closed...");


        } finally {
            System.out.println("finally...");
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }



}

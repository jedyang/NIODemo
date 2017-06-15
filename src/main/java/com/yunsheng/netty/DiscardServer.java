package com.yunsheng.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by shengyun on 17/6/1.
 */
public class DiscardServer {
    //static final boolean SSL = System.getProperty("ssl") != null;
    private int port;

    public DiscardServer(int port) {
        this.port = port;
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
        // boss接受请求
        // worker负责处理。boss会将请求连接注册给某个worker
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
                        pipeline.addLast(new DiscardServerHandler());// 注册handler。是个pipline，可以add多个。
                    }
                })
                // 设置tcp/ip连接的参数
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定监听
            ChannelFuture channelFuture = b.bind(port).sync();

            // 关闭之前，等待所有server socker已经关闭
            // 这样做比较优雅
            channelFuture.channel().closeFuture().sync();

            System.out.println("tcp server started...");
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {

        int port = 8010;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        DiscardServer discardServer = new DiscardServer(port);

        discardServer.run();

    }

}

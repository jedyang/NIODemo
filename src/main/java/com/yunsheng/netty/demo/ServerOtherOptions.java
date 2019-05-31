package com.yunsheng.netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;

/**
 * @author uncleY
 * @description: Netty的其他参数举例
 *
 * 可以看出netty的设计，不带child的参数配置的都是server端，带child配置的是客户端连接
 * @date 2019/5/30 14:44
 */
public class ServerOtherOptions {

    private static AttributeKey<String> clientKey;
    private static AttributeKey<String> serverNameKey;

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 老板负责接活
        NioEventLoopGroup boss = new NioEventLoopGroup();
        // 员工负责干活
        NioEventLoopGroup worker = new NioEventLoopGroup();

        // 最小化参数的netty_server例子
        // 只需要设定group、channel、handler
        // 然后bind端口
        serverNameKey = AttributeKey.newInstance("serverName");
        clientKey = AttributeKey.newInstance("clientKey");
        serverBootstrap.group(boss, worker)
                // 通过channel指定IO模型为NIO
                .channel(NioServerSocketChannel.class)
                // 给服务端NioServerSocketChannel设置自定义属性
                // 可以通过channel.attr取出来
                // 其实说白了就是给NioServerSocketChannel维护一个map而已，通常情况下，我们也用不上这个方法。
                .attr(serverNameKey, "myNettyServer")
                // 是处理服务端启动过程中的逻辑，一般用不到
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) throws Exception {
                        System.out.println("服务端启动中的逻辑。。。。");
                        // 打印前面设置的参数
                        System.out.println(ch.attr(serverNameKey).get());
                    }

                })
                // 同样可以给客户端连接配置自定义属性
                .childAttr(clientKey, "clientValue")
                // 具体的读写、业务处理逻辑在handler里
                // 注意是childHandler，是处理客户端连接后的逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 打印前面设置的参数
                        System.out.println(ch.attr(clientKey).get());
                        System.out.println(ch.attr(AttributeKey.newInstance("fromClientKey")).get());

                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                // option设置服务端连接的TCP配置
                .option(ChannelOption.SO_BACKLOG, 1024) //表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                // childOption给每条客户端连接设置一些TCP底层相关的属性
                .childOption(ChannelOption.SO_KEEPALIVE, true) // 开启TCP底层心跳机制
                .childOption(ChannelOption.TCP_NODELAY, true) // 表示是否开启Nagle算法，true表示关闭，false表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启。
                .bind(8000);

    }
}

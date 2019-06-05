package com.yunsheng.netty.handlers;

import java.net.SocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 演示handler的生命周期
 *
 * 逻辑处理器被添加：handlerAdded()
 channel 绑定到线程(NioEventLoop)：channelRegistered()
 channel 准备就绪：channelActive()
 channel 有数据可读：channelRead()
 hello netty
 channel 某次数据读完：channelReadComplete()
 六月 03, 2019 4:46:50 下午 io.netty.channel.DefaultChannelPipeline onUnhandledInboundException
 警告: An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.
 java.io.IOException: 远程主机强迫关闭了一个现有的连接。
 at sun.nio.ch.SocketDispatcher.read0(Native Method)

 channel 被关闭：channelInactive()
 channel 取消线程(NioEventLoop) 的绑定: channelUnregistered()
 逻辑处理器被移除：handlerRemoved()

 */
public class LifeCyCleTestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        当检测到新连接之后，调用 ch.pipeline().addLast(new LifeCyCleTestHandler()); 之后的回调，
// 表示在当前的 channel 中，已经成功添加了一个 handler 处理器。
        System.out.println("逻辑处理器被添加：handlerAdded()");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        这个回调方法，表示当前的 channel 的所有的逻辑处理已经和某个 NIO 线程建立了绑定关系
        System.out.println("channel 绑定到线程(NioEventLoop)：channelRegistered()");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
// 当 channel 的所有的业务逻辑链准备完毕（也就是说 channel 的 pipeline 中已经添加完所有的 handler）以及绑定好一个 NIO 线程之后，
// 这条连接算是真正激活了，接下来就会回调到此方法。
//        对我们的应用程序来说，这两个方法表明的含义是 TCP 连接的建立与释放，通常我们在这两个回调里面统计单机的连接数，
// channelActive() 被调用，连接数加一，channelInActive() 被调用，连接数减一
//        另外，我们也可以在 channelActive() 方法中，实现对客户端连接 ip 黑白名单的过滤，具体这里就不展开了

        System.out.println("channel 准备就绪：channelActive()");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel 有数据可读：channelRead()");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        我们在每次向客户端写数据的时候，都通过 writeAndFlush() 的方法写并刷新到底层，其实这种方式不是特别高效，
// 我们可以在之前调用 writeAndFlush() 的地方都调用 write() 方法，
// 然后在这个方面里面调用 ctx.channel().flush() 方法，相当于一个批量刷新的机制，
// 当然，如果你对性能要求没那么高，writeAndFlush() 足矣。
        System.out.println("channel 某次数据读完：channelReadComplete()");
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 被关闭：channelInactive()");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 取消线程(NioEventLoop) 的绑定: channelUnregistered()");
        super.channelUnregistered(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("逻辑处理器被移除：handlerRemoved()");
        super.handlerRemoved(ctx);
    }
}
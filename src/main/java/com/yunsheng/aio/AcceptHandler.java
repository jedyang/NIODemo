package com.yunsheng.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 处理ACCEPT操作成功的异步消息
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, TimeServerHandler>{

    public AcceptHandler(){

    }

    @Override
    public void completed(AsynchronousSocketChannel result, TimeServerHandler attachment) {
        // 再调起一个accept，为了继续接受其他的连接请求
        attachment.getAsynchronousServerSocketChannel().accept(attachment, this);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 调起read处理类处理
        result.read(byteBuffer, byteBuffer, new ReadHandler(result));
    }

    @Override
    public void failed(Throwable exc, TimeServerHandler attachment) {
        attachment.getCountDownLatch().countDown();
    }
}

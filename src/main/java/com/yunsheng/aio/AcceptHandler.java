package com.yunsheng.aio;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 处理ACCEPT操作成功的异步消息
 */
public class AcceptHandler implements CompletionHandler<AsynchronousServerSocketChannel, TimeServerHandler>{

    @Override
    public void completed(AsynchronousServerSocketChannel result, TimeServerHandler attachment) {

    }

    @Override
    public void failed(Throwable exc, TimeServerHandler attachment) {

    }
}

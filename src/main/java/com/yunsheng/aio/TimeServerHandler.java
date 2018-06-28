package com.yunsheng.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class TimeServerHandler implements Runnable{

    private int port;
    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;
    private CountDownLatch countDownLatch;

    public AsynchronousServerSocketChannel getAsynchronousServerSocketChannel() {
        return asynchronousServerSocketChannel;
    }

    public void setAsynchronousServerSocketChannel(AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
        this.asynchronousServerSocketChannel = asynchronousServerSocketChannel;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public TimeServerHandler(int port){
        this.port = port;

        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("listen on " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        // 使用countDownLatch来做阻塞
        // 这里是为了防止服务端执行完退出了
        // 实际项目上并不需要启用独立线程来处理
        countDownLatch = new CountDownLatch(1);
        doAccept();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept() {

        asynchronousServerSocketChannel.accept(this, new AcceptHandler());
    }
}

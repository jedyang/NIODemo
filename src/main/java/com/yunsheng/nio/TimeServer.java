package com.yunsheng.nio;

/**
 * NIO模式的TimeServer
 */
public class TimeServer {
    public static void main(String[] args) {
        NIOTimeServer nioTimeServer = new NIOTimeServer(8100);

        new Thread(nioTimeServer, "nioTimeServer").start();
    }
}

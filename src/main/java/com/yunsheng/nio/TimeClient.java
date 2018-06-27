package com.yunsheng.nio;

public class TimeClient {
    public static void main(String[] args) {
        new Thread(new NIOTimeClient("127.0.0.1", 8100)).start();
    }
}

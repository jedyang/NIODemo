package com.yunsheng.aio;

public class TimeServer {
    public static void main(String[] args){
        new Thread(new TimeServerHandler(8200)).start();
    }
}

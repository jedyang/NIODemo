package com.yunsheng.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * BIO模式的TimeServer
 * 线程池模式
 */
public class TimeServerPool {
    public static void main(String[] args){
        ServerSocket serverSocket = null;
        try {
             serverSocket = new ServerSocket(8000);
            System.out.println("BIO ServerSocket started");
            ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), 100, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));
            while (true){
                Socket acceptSocket = serverSocket.accept();
                executorService.execute(new TimeServerHandler(acceptSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

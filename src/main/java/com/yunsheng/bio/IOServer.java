package com.yunsheng.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author uncleY
 * @description: 传统IO的简单例子
 * @date 2019/5/30 10:07
 */
public class IOServer {
    /**
     * 传统io的问题在于每次有新的客户端连接过来，都需要创建一个对应的线程进行处理。 而线程是服务器非常珍贵的资源。
     *
     * 而且，线程多了，cpu不断进行线程切换，对cpu也是很大的负担。 并且，在处理线程中，要通过不断的while循环来监听是否有消息发送过来。
     *
     * 如果有大量连接是空闲的，那么也是巨大的性能浪费。
     *
     * 另外，传统io的读写是面向流的。面向流有什么问题呢？
     *
     * 面向流最大的问题就是只能往前读。读过的就过去了，不能回去重新读。需要自己写代码缓存下来。
     */
    public static void main(String[] args) throws Exception {

        final ServerSocket serverSocket = new ServerSocket(8000);

        // 接受新连接的线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // 阻塞获取新的连接
                        final Socket accept = serverSocket.accept();
                        System.out.println("获得一个客户端连接");

                        // 每来一个连接需要起一个新的线程处理
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    InputStream inputStream = accept.getInputStream();
                                    System.out.println("接收到一个请求");
                                    byte[] src = new byte[1024];
                                    int len;
                                    // 一直保持连接，不断循环。为了监听是否有消息发过来
                                    while ((len = inputStream.read(src)) != -1) {
                                        System.out.println(new String(src, 0, len));
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

package com.yunsheng.bio;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author uncleY
 * @description: 传统IO的简单例子
 * @date 2019/5/30 10:30
 */
public class IOClient {
    public static void main(String[] args) throws IOException {

        new Thread(new Runnable() {
            Socket socket = new Socket("127.0.0.1", 8000);

            @Override
            public void run() {
                try {
                    int i = 0;
                    while (true) {
                        // 模拟有多个客户端建立连接
//                        Socket socket = new Socket("127.0.0.1", 8000);
                        OutputStream outputStream = socket.getOutputStream();
                        i++;
                        outputStream.write(("hello " + i).getBytes());
                        System.out.println("发送一个请求");
                        Thread.sleep(2000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

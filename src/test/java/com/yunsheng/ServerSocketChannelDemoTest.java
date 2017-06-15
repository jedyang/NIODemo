package com.yunsheng;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;

/**
 * Created by shengyun on 17/5/24.
 */
public class ServerSocketChannelDemoTest {

    // 创建几个事件
    @Test
    public void testSocket() {
        doSend(9001, "hello 9001");
        doSend(9002, "hello 9002");
        doSend(9003, "hello 9003");

    }

    private void doSend(int i, String s) {
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", i);
            OutputStream outputStream = socket.getOutputStream();
            byte[] bytes = s.getBytes();
            try {
                outputStream.write(bytes);
            } finally {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 测试netty
    @Test
    public void testNetty() {
        doSend(8080, "hello 8080");
    }

}
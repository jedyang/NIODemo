package com.yunsheng.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * BIO使用的是InputStream和OutputStream
 * 在InputStream的read方法中，有这样的说明
 * This method
 * blocks until input data is available, the end of the stream is detected,
 * or an exception is thrown
 * 说明read会一直阻塞，直到有数据可读、读完、发生异常
 *
 * 这是BIO同步阻塞的根本原因
 *
 */
public class TimeServerHandler implements Runnable {
    private Socket accept;

    public TimeServerHandler(Socket accept) {
        this.accept = accept;
    }

    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;

        try {
            InputStream acceptInputStream = this.accept.getInputStream();
            in = new BufferedReader(new InputStreamReader(acceptInputStream));

            OutputStream acceptOutputStream = this.accept.getOutputStream();
            out = new PrintWriter(acceptOutputStream, true);

            String currentTime;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String recv = in.readLine();
            for (; recv != null; recv = in.readLine()) {
                System.out.println("收到请求：" + recv);
                if ("QUERY TIME".equalsIgnoreCase(recv)) {
                    currentTime = sdf.format(new Date(System.currentTimeMillis()));
                    out.println(currentTime);
                }else {
                    out.println("异常请求");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                out.close();
            }

            if (this.accept != null) {
                try {
                    this.accept.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

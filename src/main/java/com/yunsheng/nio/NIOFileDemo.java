package com.yunsheng.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;

/**
 * Created by shengyun on 17/5/22.
 *
 * 在BIO例子中可以看到，BIO阻塞的关键是Stream的阻塞操作。
 * 所以在NIO中，设计了一个缓冲区Buffer，所有对数据的操作都是个Buffer打交道
 *
 * 1,
 * 缓冲区Buffer 本质上就是一个数组 + 对数据的结构化访问接口 + 对读写位置信息的维护等
 *
 * 每一种java基本类型都有自己对应的缓冲区，最常用的还是ByteBuffer
 *
 * 2,
 * 用于代替流的是channel。全双工通信。可同时读写
 * channel主要分两类：用户文件的FileChannel，用于网络的SelectableChannel
 *
 * 3,多路复用器selector
 * 这是NIO模式的核心。channel注册在selector上，selector不断轮询。一旦channel发生读写事件，
 * 这个channel就处于就绪状态，会被selector挑选出来，进行接下来的操作。
 *
 */
public class NIOFileDemo {

    public static void copyFile(String src, String dest) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(new File(src));
            fos = new FileOutputStream(new File(dest));

            FileChannel fic = fis.getChannel();
            FileChannel foc = fos.getChannel();
            try {
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                while (true) {

                    int i = fic.read(buffer);
                    if (i == -1) {
                        // 读到结尾
                        break;
                    }

                    // 翻转，切换到buffer的读模式
                    // limit 置为position position置为0，
                    buffer.flip();

                    foc.write(buffer);

                    // 写完后重置buffer，以便一边下一次对buffer的使用
                    // 重设position=0,limit=capacity
                    buffer.clear();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (null != fic){
                    try {
                        fic.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (null != foc){
                    try {
                        foc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

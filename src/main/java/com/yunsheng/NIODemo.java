package com.yunsheng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by shengyun on 17/5/22.
 */
public class NIODemo {

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

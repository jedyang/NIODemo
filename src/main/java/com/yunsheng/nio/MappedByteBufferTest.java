package com.yunsheng.nio;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class MappedByteBufferTest {
    public static void main(String[] args) {

//        onlyReadTest();

        rwTest();
    }

    private static void rwTest() {

        int length = 0x8FFFFFF;//一个byte占1B，所以共向文件中存128M的数据
        try (
                FileChannel channel = FileChannel.open(Paths.get("D://test1.txt"),
                        StandardOpenOption.READ, StandardOpenOption.WRITE))

        {
            MappedByteBuffer mapBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, length);
            for (int i = 0; i < length; i++) {
                mapBuffer.put((byte) 0);
            }
            for (int i = length / 2; i < length / 2 + 4; i++) {
                //像数组一样访问
                System.out.println(mapBuffer.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void onlyReadTest() {
        //        通过MappedByteBuffer读取文件
        File file = new File("D://test.txt");
        long len = file.length();
        byte[] ds = new byte[(int) len];

        try {
            RandomAccessFile srcFile = new RandomAccessFile(file, "r");
            FileChannel fileChannel = srcFile.getChannel();
            // FileChannel提供了map方法把文件映射到虚拟内存，通常情况可以映射整个文件，如果文件比较大，可以进行分段映射。
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, len);
            for (int offset = 0; offset < len; offset++) {
                byte b = mappedByteBuffer.get();
                ds[offset] = b;
            }

            Scanner scan = new Scanner(new ByteArrayInputStream(ds)).useDelimiter(" ");
            while (scan.hasNext()) {
                System.out.print(scan.next() + " ");
            }

        } catch (IOException e) {
        }

    }
}
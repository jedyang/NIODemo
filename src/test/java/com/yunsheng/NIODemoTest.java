package com.yunsheng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

/**
 * Created by shengyun on 17/5/22.
 */
public class NIODemoTest {

    public static void main(String[] args){
        String path = NIODemoTest.class.getResource("").getPath();
        System.out.println(path);

        // 这两种获取文件的方式，在IDE下都可以
        // 但是在控制台里，使用下面getResourceAsStream方式使用classPath是靠谱的
        // 下面这种路径直接拿File的会有问题
        //File f = new File("src/test/java/com/yunsheng/src.txt");

        InputStream in = NIODemoTest.class.getResourceAsStream("/com/yunsheng/src.txt");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String temp = null;
            while((temp = reader.readLine()) != null){
                System.out.println(temp);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        File f = new File("src/test/resources/src.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String temp = null;
            while((temp = reader.readLine()) != null){
                System.out.println(temp);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void copyFileTest() {

        String src = "src/test/resources/src.txt";
        String dest = "src/test/resources/dest.txt";
        NIODemo.copyFile(src, dest);

    }
}
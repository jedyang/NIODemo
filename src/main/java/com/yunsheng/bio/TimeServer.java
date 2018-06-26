package com.yunsheng.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO模式的TimeServer
 */
public class TimeServer {
    public static void main(String[] args){
        ServerSocket serverSocket = null;
        try {
             serverSocket = new ServerSocket(8000);
            System.out.println("BIO ServerSocket started");
            while (true){
                Socket acceptSocket = serverSocket.accept();
                new Thread(new TimeServerHandler(acceptSocket)).start();
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

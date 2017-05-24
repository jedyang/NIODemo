package com.yunsheng;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

/**
 * Created by shengyun on 17/5/24.
 */
public class ServerSocketChannelDemo {

    private int ports[];
    private ByteBuffer echoBuffer = ByteBuffer.allocate(1024);

    public ServerSocketChannelDemo(int[] ports) {
        this.ports = ports;
    }

    private void exec() throws IOException {
        // 1,创建selector，NIO的核心
        // 用于监听处理事件
        Selector selector = Selector.open();

        // 2,为每个端口开一个Channel
        for (int i : ports) {
            // 没监听一个端口要开一个serverSocketChannel
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false); // 设置非阻塞
            ServerSocket serverSocket = ssc.socket();
            serverSocket.bind(new InetSocketAddress(i)); //监听端口

            // 3，注册到selector
            // OP_ACCEPT是适用于ServerSocketChannel的唯一事件
            SelectionKey selectionKey = ssc.register(selector, OP_ACCEPT);

            System.out.println("listen on " + i);
        }

        // 4, 开始循环
        while (true) {
            // select()会阻塞，直到有事件发生，返回值是事件的数量
            int select = selector.select();


            // 事件的key集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 循环处理事件
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey selectionKey = keyIterator.next();
                System.out.println(selectionKey.interestOps());
                // 使用完的及时移除
                keyIterator.remove();

                // 我们需要的aeecpt事件
                if (selectionKey.isAcceptable()){
                    // 有accept事件，说明有一个连接在等待，所以accept是不会阻塞的
                    ServerSocketChannel ssc = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel socketChannel = ssc.accept();
                    socketChannel.configureBlocking(false);

                    // 拿到这个连接，载注册到selector，以便使用
                    socketChannel.register(selector, SelectionKey.OP_READ);



                    System.out.println("get connection from " + socketChannel);
                } else if(selectionKey.isReadable()){
                    // 如果是连接的读事件
                    SocketChannel sc = (SocketChannel)selectionKey.channel();
                    StringBuffer transStr = new StringBuffer();
                    while (true){

                        int r = sc.read(echoBuffer);
                        if( r <= 0 ){
                            sc.close();// channel取完数据要关掉，否则一直有read事件触发select
                            break;
                        }
                        echoBuffer.flip();

                        //sc.write(echoBuffer);
                        // 构建一个byte数组
                        byte [] content = new byte[echoBuffer.limit()];
                        // 从ByteBuffer中读取数据到byte数组中
                        echoBuffer.get(content);
                        // 把byte数组的内容写到标准输出
                        transStr.append(new String(content));

                        echoBuffer.clear();

                        //sc.register(selector, OP_WRITE);


                    }
                    System.out.println("echoed :" + transStr.toString() + " from " + sc);
                }


            }

        }

    }

    public static void main(String args[]){
        ServerSocketChannelDemo demo = new ServerSocketChannelDemo(new int[]{9001, 9002, 9003});

        try {
            demo.exec();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.yunsheng.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

public class NIOTimeServer implements Runnable {
    ServerSocketChannel ssc;
    Selector selector;

    private volatile boolean stop;

    public void stop() {
        this.stop = true;
    }

    public NIOTimeServer(int port) {
        try {
            // 1,打开一个ServerSocketChannel
            // 每监听一个端口要开一个serverSocketChannel
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false); // 设置非阻塞
            ServerSocket serverSocket = ssc.socket();
            // backlog即队列里的最大连接数，设为1024。
            // 含义是同时存在的连接只能是1024个
            serverSocket.bind(new InetSocketAddress(port), 1024); //监听端口

            // 2,创建selector，NIO的核心
            // 用于监听处理事件
            selector = Selector.open();

            // 3，将ServerSocketChannel注册到selector上
            // 并监听ACCEPT事件
            // OP_ACCEPT是适用于ServerSocketChannel的唯一事件
            SelectionKey key = ssc.register(selector, OP_ACCEPT);
            System.out.println("listen on" + port);
        } catch (IOException e) {
            e.printStackTrace();
            // 异常退出
            System.exit(1);
        }
    }

    // 4, 开始轮询,放在了子线程
    public void run() {
        while (!stop) {

            try {
                /**
                 int select()
                 int select(long timeout)
                 int selectNow()

                 select()阻塞到至少有一个通道在你注册的事件上就绪了。

                 select(long timeout)和select()一样，除了最长会阻塞timeout毫秒(参数)。

                 selectNow()不会阻塞，不管什么通道就绪都立刻返回（译者注：此方法执行非阻塞的选择操作。如果自从前一次选择操作后，没有通道变成可选择的，则此方法直接返回零。）。

                 select()方法返回的int值表示有多少通道已经就绪。亦即，自上次调用select()方法后有多少通道变成就绪状态。如果调用select()方法，因为有一个通道变成就绪状态，返回了1，若再次调用select()方法，如果另一个通道就绪了，它会再次返回1。如果对第一个就绪的channel没有做任何操作，现在就有两个就绪的通道，但在每次select()方法调用之间，只有一个通道就绪了。
                 **/
                int num = selector.select(1000);//这里设置了1s，即每1秒会被唤醒一次


                // 准备就绪的key集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                // 循环处理事件
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (keyIterator.hasNext()) {
                    selectionKey = keyIterator.next();
                    System.out.println(selectionKey.interestOps());
                    // 使用完的及时移除
                    keyIterator.remove();

                    // 处理事件
                    try {
                        handleEvent(selectionKey);
                    } catch (Exception e) {
                        if (selectionKey != null) {
                            selectionKey.cancel();
                            if (selectionKey.channel() != null) {
                                selectionKey.channel().close();
                            }
                        }
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // selector关闭时，会自动释放channel等资源，不需要重复释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 事件处理
     */
    private void handleEvent(SelectionKey selectionKey) throws Exception {
        if (selectionKey.isValid()) {
            // 处理新连接，因为是ACCEPT类型的，所以是新建立的连接。channel也应该是ServerSocketChannel
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel keyChannel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = keyChannel.accept();
                socketChannel.configureBlocking(false);
                // 对这个新连接注册read事件
                socketChannel.register(selector, SelectionKey.OP_READ);
                System.out.println("get a new connection from " + socketChannel);
            }

            if (selectionKey.isReadable()) {
                SocketChannel sc = (SocketChannel) selectionKey.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);

                if (readBytes > 0) {
                    readBuffer.flip();//翻转，开始读
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);//写到bytes数组里
                    String recv = new String(bytes);

                    System.out.println("receive data:" + recv);

                    // 写响应
                    // 注意没有处理写半包的情况，需要注册写操作 TODO
                    String currentTime;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if ("QUERY TIME".equalsIgnoreCase(recv)) {
                        currentTime = sdf.format(new Date(System.currentTimeMillis()));
                        sendResponse(sc, currentTime);
                    } else {
                        sendResponse(sc, "错误请求");
                    }

                } else if (readBytes < 0) {// -1
                    // 对端关闭
                    System.out.println("对端关闭");
                    selectionKey.cancel();
                    sc.close();
                } else
                    ;//读到0字节，正常情况，忽略

            }
        }
    }

    private void sendResponse(SocketChannel sc, String currentTime) throws IOException {
        byte[] timeBytes = currentTime.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(timeBytes.length);
        writeBuffer.put(timeBytes);
        writeBuffer.flip();
        sc.write(writeBuffer);
        System.out.println("sendResponse:" + currentTime);
    }
}

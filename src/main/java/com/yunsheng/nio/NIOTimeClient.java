package com.yunsheng.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOTimeClient implements Runnable {
    private String host;
    private int port;

    private Selector selector;
    private SocketChannel socketChannel;

    private volatile boolean stop;

    public void stop() {
        this.stop = true;
    }

    public NIOTimeClient(String host, int port) {
        this.port = port;
        this.host = host;

        try {
            selector = Selector.open();

            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void run() {

        // 连接服务端
        // 生产上，这里需要考虑重连问题
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (iterator.hasNext()) {
                    selectionKey = iterator.next();
                    iterator.remove();
                    System.out.println("selectionKey:" + selectionKey.interestOps());
                    handleInput(selectionKey);

                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            if (selector != null){
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void handleInput(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()) {
            SocketChannel sc = (SocketChannel) selectionKey.channel();
            if (selectionKey.isConnectable()) {
                // 连接成功，注册写事件。向服务端发送请求。
                if (sc.finishConnect()) {
                    System.out.println("建立连接成功");
                    sc.register(selector, SelectionKey.OP_READ);
                    sendReq(sc);
                } else {
                    System.out.println("异常退出");
                    System.exit(1);
                }
            }

            if (selectionKey.isReadable()) {
                ByteBuffer dst = ByteBuffer.allocate(1024);
                /**
                 * NIO的读写是异步的
                 */
                int read = sc.read(dst);
                if (read > 0) {
                    dst.flip();
                    byte[] dstBytes = new byte[dst.remaining()];
                    dst.get(dstBytes);
                    String response = new String(dstBytes, "UTF-8");

                    System.out.println("now is " + response);

                    // 关闭
                    this.stop = true;
                } else if (read < 0) {
                    // 对端关闭
                    System.out.println("对端关闭");
                    selectionKey.cancel();
                    sc.close();
                } else {
                    // 读到空，正常。
                }

            }

        }
    }

    /**
     * NIO的连接是异步的
     * @throws IOException
     */
    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            // 如果已经连上，注册读事件。向服务端发送请求。
            socketChannel.register(selector, SelectionKey.OP_READ);
            sendReq(socketChannel);
        } else {
            // 还没连上，并不是连接就失败了，可能还没返回tcp握手应答
            // 注册连接事件
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void sendReq(SocketChannel socketChannel) throws IOException {
        byte[] bytes = "QUERY TIME".getBytes();

        ByteBuffer req = ByteBuffer.allocate(bytes.length);
        req.put(bytes);
        req.flip();
        socketChannel.write(req);

        if (!req.hasRemaining()) {
            System.out.println("send req done");
        } else {
            System.out.println("半包请求，未处理");
        }
    }
}

package com.yunsheng.netty;

public class ServerStarter {

    public static void main(String[] args) throws Exception {

        int port = 8010;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        TimeServerHandler handler = new TimeServerHandler();
        Server server = new Server(port, handler);

        server.run();

    }
}

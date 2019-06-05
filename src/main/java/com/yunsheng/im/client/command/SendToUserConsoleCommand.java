package com.yunsheng.im.client.command;

import com.yunsheng.im.protocol.command.MessageRequestPacket;

import java.util.Scanner;

import io.netty.channel.Channel;

public class SendToUserConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("发送消息给某个某个用户：");

        String toUserName = scanner.next();
        String message = scanner.next();
        channel.writeAndFlush(new MessageRequestPacket(toUserName, message));
    }
}

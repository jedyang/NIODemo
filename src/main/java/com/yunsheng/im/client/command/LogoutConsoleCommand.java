package com.yunsheng.im.client.command;

import com.yunsheng.im.protocol.command.LogoutRequestPacket;

import java.util.Scanner;

import io.netty.channel.Channel;

public class LogoutConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        LogoutRequestPacket logoutRequestPacket = new LogoutRequestPacket();
        channel.writeAndFlush(logoutRequestPacket);
    }
}

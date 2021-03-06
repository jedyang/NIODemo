package com.yunsheng.im.client.command;

import com.yunsheng.im.protocol.command.ExitGroupRequestPacket;
import com.yunsheng.im.protocol.command.JoinGroupRequestPacket;

import java.util.Scanner;

import io.netty.channel.Channel;

/**
 * @description: 退群
 * @author uncleY
 * @date 2019/6/5 16:01
 */
public class ExitGroupCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("输入要退出的群号");
        String groupId = scanner.nextLine();

        ExitGroupRequestPacket packet = new ExitGroupRequestPacket();
        packet.setGroupId(groupId);

        channel.writeAndFlush(packet);
    }
}

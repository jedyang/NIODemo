package com.yunsheng.im.client.command;

import com.yunsheng.im.protocol.command.JoinGroupRequestPacket;

import java.util.Scanner;

import io.netty.channel.Channel;

/**
 * @description: 加入群
 * @author uncleY
 * @date 2019/6/5 16:01
 */
public class JoinGroupCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("输入要加入的群号");
        String groupId = scanner.nextLine();

        JoinGroupRequestPacket packet = new JoinGroupRequestPacket();
        packet.setGroupId(groupId);

        channel.writeAndFlush(packet);
    }
}

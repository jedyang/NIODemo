package com.yunsheng.im.client.command;

import com.yunsheng.im.protocol.command.JoinGroupRequestPacket;
import com.yunsheng.im.protocol.command.ListGroupRequestPacket;

import java.util.Scanner;

import io.netty.channel.Channel;

/**
 * @description: 加入群
 * @author uncleY
 * @date 2019/6/5 16:01
 */
public class ListGroupCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("输入要查询的群号");
        String groupId = scanner.nextLine();

        ListGroupRequestPacket packet = new ListGroupRequestPacket();
        packet.setGroupId(groupId);

        channel.writeAndFlush(packet);
    }
}

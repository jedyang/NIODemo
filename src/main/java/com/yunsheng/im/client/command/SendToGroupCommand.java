package com.yunsheng.im.client.command;

import com.yunsheng.im.protocol.command.JoinGroupRequestPacket;
import com.yunsheng.im.protocol.command.SendToGroupRequestPacket;

import java.util.Scanner;

import io.netty.channel.Channel;

/**
 * @description: 发送群消息
 * @author uncleY
 * @date 2019/6/5 16:01
 */
public class SendToGroupCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("输入要加入的群号:");
        String groupId = scanner.nextLine();

        SendToGroupRequestPacket packet = new SendToGroupRequestPacket();
        packet.setGroupId(groupId);


        System.out.println("消息:");
        String msg = scanner.nextLine();
        packet.setMsg(msg);

        channel.writeAndFlush(packet);
    }
}

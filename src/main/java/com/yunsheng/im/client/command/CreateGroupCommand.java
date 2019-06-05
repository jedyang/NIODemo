package com.yunsheng.im.client.command;

import com.yunsheng.im.protocol.command.CreateGroupRequestPacket;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import io.netty.channel.Channel;

/**
 * @description: TODO
 * @author uncleY
 * @date 2019/6/5 10:09
 */
public class CreateGroupCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("【拉人群聊】输入用户名，用户名之间英文逗号隔开：");

        String userNames = scanner.nextLine();
        List<String> userNameList = Arrays.asList(userNames.split(","));
        CreateGroupRequestPacket packet = new CreateGroupRequestPacket();
        packet.setUserNames(userNameList);
        channel.writeAndFlush(packet);
    }
}

package com.yunsheng.im.client.command;

import com.yunsheng.im.protocol.command.LoginRequestPacket;

import java.util.Scanner;

import io.netty.channel.Channel;

/**
 * @description: TODO
 * @author uncleY
 * @date 2019/6/5 10:50
 */
public class LoginCommand implements ConsoleCommand{
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("输入用户名登录");
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        String userName = scanner.nextLine();
        loginRequestPacket.setUsername(userName);
        channel.writeAndFlush(loginRequestPacket);

        try {
            // 等待2秒登录
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

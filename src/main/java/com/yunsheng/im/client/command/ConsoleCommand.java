package com.yunsheng.im.client.command;

import java.util.Scanner;

import io.netty.channel.Channel;

/**
 * @description: 客户端命令的抽象
 * @author uncleY
 * @date 2019/6/5 10:07
 */
public interface ConsoleCommand {
    void exec(Scanner scanner, Channel channel);
}

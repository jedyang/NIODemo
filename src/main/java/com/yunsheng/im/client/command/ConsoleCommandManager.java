package com.yunsheng.im.client.command;

import com.yunsheng.im.util.SessionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import io.netty.channel.Channel;

/**
 * @description: 客户端命令统一管理
 * @author uncleY
 * @date 2019/6/5 10:06
 */
public class ConsoleCommandManager {

    private Map<String, ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager() {
        consoleCommandMap = new HashMap<>();
        consoleCommandMap.put("sendToUser", new SendToUserConsoleCommand());
        consoleCommandMap.put("logout", new LogoutConsoleCommand());
        consoleCommandMap.put("createGroup", new CreateGroupCommand());
        consoleCommandMap.put("joinGroup", new JoinGroupCommand());
        consoleCommandMap.put("exitGroup", new ExitGroupCommand());
        consoleCommandMap.put("listGroup", new ListGroupCommand());
        consoleCommandMap.put("sendToGroup", new SendToGroupCommand());
    }

    public void exec(Scanner scanner, Channel channel){

        System.out.println("请输入命令:");
        String command = scanner.nextLine();

        ConsoleCommand consoleCommand = consoleCommandMap.get(command);

        if (null == consoleCommand){
            System.out.println("输入的命令有误");
        } else {
            consoleCommand.exec(scanner, channel);
        }


    }
}

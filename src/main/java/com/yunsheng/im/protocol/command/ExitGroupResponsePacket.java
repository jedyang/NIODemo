package com.yunsheng.im.protocol.command;

import java.util.List;

import lombok.Data;

import static com.yunsheng.im.protocol.command.Command.EXIT_GROUP_REQUEST;
import static com.yunsheng.im.protocol.command.Command.EXIT_GROUP_RESPONSE;

/**
 * @description: 退群
 * @author uncleY
 * @date 2019/6/5 16:05
 */
@Data
public class ExitGroupResponsePacket extends Packet {

    private String groupId;

    private String exitUserName;

    private List<String> userNames;

    @Override
    public Byte getCommand() {
        return EXIT_GROUP_RESPONSE;
    }
}

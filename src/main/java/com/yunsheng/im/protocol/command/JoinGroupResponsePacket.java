package com.yunsheng.im.protocol.command;

import java.util.List;

import lombok.Data;

import static com.yunsheng.im.protocol.command.Command.JOIN_GROUP_REQUEST;
import static com.yunsheng.im.protocol.command.Command.JOIN_GROUP_RESPONSE;

/**
 * @description: 加入群
 * @author uncleY
 * @date 2019/6/5 16:05
 */
@Data
public class JoinGroupResponsePacket extends Packet{

    private String groupId;
    private String joinedUserName;
    private List<String> userNames;

    @Override
    public Byte getCommand() {
        return JOIN_GROUP_RESPONSE;
    }
}

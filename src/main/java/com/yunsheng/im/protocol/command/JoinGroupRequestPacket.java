package com.yunsheng.im.protocol.command;

import lombok.Data;

import static com.yunsheng.im.protocol.command.Command.JOIN_GROUP_REQUEST;

/**
 * @description: 加入群
 * @author uncleY
 * @date 2019/6/5 16:05
 */
@Data
public class JoinGroupRequestPacket extends Packet{

    private String groupId;

    @Override
    public Byte getCommand() {
        return JOIN_GROUP_REQUEST;
    }
}

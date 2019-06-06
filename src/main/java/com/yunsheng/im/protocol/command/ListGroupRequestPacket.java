package com.yunsheng.im.protocol.command;

import java.util.List;

import lombok.Data;

import static com.yunsheng.im.protocol.command.Command.LIST_GROUP_REQUEST;
import static com.yunsheng.im.protocol.command.Command.LIST_GROUP_RESPONSE;

/**
 * @description: 群成员查询
 * @author uncleY
 * @date 2019/6/5 16:05
 */
@Data
public class ListGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {
        return LIST_GROUP_REQUEST;
    }
}

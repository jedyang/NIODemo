package com.yunsheng.im.protocol.command;

import java.util.List;

import lombok.Data;

import static com.yunsheng.im.protocol.command.Command.CREATE_GROUP_REQUEST;

/**
 * @description: TODO
 * @author uncleY
 * @date 2019/6/5 10:11
 */
@Data
public class CreateGroupRequestPacket extends Packet {

    private List<String> userNames;

    @Override
    public Byte getCommand() {
        return CREATE_GROUP_REQUEST;
    }
}

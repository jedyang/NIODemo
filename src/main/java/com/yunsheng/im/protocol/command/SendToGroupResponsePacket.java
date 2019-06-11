package com.yunsheng.im.protocol.command;

import lombok.Data;

import static com.yunsheng.im.protocol.command.Command.SEND_TO_GROUP_RESPONSE;


@Data
public class SendToGroupResponsePacket extends Packet {

    private String fromGroup;

    private String msg;

    private boolean result;

    @Override
    public Byte getCommand() {

        return SEND_TO_GROUP_RESPONSE;
    }
}
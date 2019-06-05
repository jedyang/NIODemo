package com.yunsheng.im.protocol.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import static com.yunsheng.im.protocol.command.Command.MESSAGE_REQUEST;

@Data
@AllArgsConstructor
public class MessageRequestPacket extends Packet {

    private String toUserName;

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
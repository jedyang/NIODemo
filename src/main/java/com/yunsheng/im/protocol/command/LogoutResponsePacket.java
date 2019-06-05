package com.yunsheng.im.protocol.command;

import lombok.Data;

import static com.yunsheng.im.protocol.command.Command.LOGOUT_REQUEST;
import static com.yunsheng.im.protocol.command.Command.LOGOUT_RESPONSE;

@Data
public class LogoutResponsePacket extends Packet {
    private boolean success;

    @Override
    public Byte getCommand() {

        return LOGOUT_RESPONSE;
    }
}

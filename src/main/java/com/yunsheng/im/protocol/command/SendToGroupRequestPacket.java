package com.yunsheng.im.protocol.command;

import lombok.Data;

import static com.yunsheng.im.protocol.command.Command.JOIN_GROUP_REQUEST;
import static com.yunsheng.im.protocol.command.Command.SEND_TO_GROUP_REQUEST;

/**
 * @description: 发送群消息
 * @author uncleY
 * @date 2019/6/5 16:05
 */
@Data
public class SendToGroupRequestPacket extends Packet{

    private String groupId;
    private String msg;

    @Override
    public Byte getCommand() {
        return SEND_TO_GROUP_REQUEST;
    }
}

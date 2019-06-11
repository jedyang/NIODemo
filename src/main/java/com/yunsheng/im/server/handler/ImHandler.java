package com.yunsheng.im.server.handler;

import com.yunsheng.im.protocol.command.Packet;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.yunsheng.im.protocol.command.Command.*;

/**
 * @description: 优化handler链
 * @author uncleY
 * @date 2019/6/11 15:18
 */
@ChannelHandler.Sharable
public class ImHandler extends SimpleChannelInboundHandler<Packet> {
    public static final ImHandler INSTANCE = new ImHandler();

    private Map<Byte, SimpleChannelInboundHandler<? extends Packet>> handlerMap;
    private ImHandler() {
        handlerMap = new HashMap<>();
        handlerMap.put(MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);
        handlerMap.put(CREATE_GROUP_REQUEST, CreateGroupRequestHandler.INSTANCE);
        handlerMap.put(JOIN_GROUP_REQUEST, JoinGroupRequestHandler.INSTANCE);
        handlerMap.put(EXIT_GROUP_REQUEST, ExitGroupRequestHandler.INSTANCE);
        handlerMap.put(LIST_GROUP_REQUEST, ListGroupRequestHandler.INSTANCE);
        handlerMap.put(SEND_TO_GROUP_REQUEST, SendToGroupRequestHandler.INSTANCE);
        handlerMap.put(LOGOUT_REQUEST, LogoutRequestHandler.INSTANCE);
        handlerMap.put(LOGIN_REQUEST, LoginRequestHandler.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        handlerMap.get(msg.getCommand()).channelRead(ctx, msg);
    }
}

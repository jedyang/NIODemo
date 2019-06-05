package com.yunsheng.im.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.AttributeKey;

/**
 * @description: 保持用户与连接的对应关系
 * @author uncleY
 * @date 2019/6/4 14:05
 */
public class SessionUtil {
    private static Map<String, Channel> sessions = new ConcurrentHashMap<>();

    private static Map<String, ChannelGroup> channelGroupMap = new ConcurrentHashMap<>();

    public static final AttributeKey SESSION_KEY = AttributeKey.newInstance("session");

    public static Channel getChannel(String username) {
        return sessions.get(username);
    }

    public static void bindChannel(String username, Channel channel) {
        sessions.put(username, channel);
    }

    public static void unBindChannel(Channel channel) {
        String username = (String) channel.attr(SESSION_KEY).get();
        sessions.remove(username);
    }

    public static void bindChannelGroup(String groupId, ChannelGroup channelGroup){
        channelGroupMap.put(groupId, channelGroup);
    }

    public static ChannelGroup getChannelGroup(String groupId){
        return channelGroupMap.get(groupId);
    }
}

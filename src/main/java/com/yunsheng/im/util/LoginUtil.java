package com.yunsheng.im.util;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @author uncleY
 * @description: TODO
 * @date 2019/6/2 10:51
 */
public class LoginUtil {
    private static AttributeKey loginKey = AttributeKey.newInstance("loginKey");

    public static void markAsLogin(Channel channel) {
        channel.attr(loginKey).set(true);
    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(loginKey);

        return loginAttr.get() != null;
    }
}

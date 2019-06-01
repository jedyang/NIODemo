package com.yunsheng.im.serializer;

import com.yunsheng.im.serializer.impl.JSONSerializer;

public interface Serializer {
    /**
     * 默认使用json 序列化
     */
    byte JSON_SERIALIZER = 1;
    Serializer DEFAULT = new JSONSerializer();


    /**
     * 序列化算法
     */
    byte getSerializerAlgorithm();
    
    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
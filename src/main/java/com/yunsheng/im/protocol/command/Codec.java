package com.yunsheng.im.protocol.command;

import com.yunsheng.im.serializer.Serializer;
import com.yunsheng.im.serializer.SerializerAlgorithm;
import com.yunsheng.im.serializer.impl.JSONSerializer;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static com.yunsheng.im.protocol.command.Command.CREATE_GROUP_REQUEST;
import static com.yunsheng.im.protocol.command.Command.CREATE_GROUP_RESPONSE;
import static com.yunsheng.im.protocol.command.Command.EXIT_GROUP_REQUEST;
import static com.yunsheng.im.protocol.command.Command.EXIT_GROUP_RESPONSE;
import static com.yunsheng.im.protocol.command.Command.JOIN_GROUP_REQUEST;
import static com.yunsheng.im.protocol.command.Command.JOIN_GROUP_RESPONSE;
import static com.yunsheng.im.protocol.command.Command.LIST_GROUP_REQUEST;
import static com.yunsheng.im.protocol.command.Command.LIST_GROUP_RESPONSE;
import static com.yunsheng.im.protocol.command.Command.LOGIN_REQUEST;
import static com.yunsheng.im.protocol.command.Command.LOGIN_RESPONSE;
import static com.yunsheng.im.protocol.command.Command.LOGOUT_REQUEST;
import static com.yunsheng.im.protocol.command.Command.LOGOUT_RESPONSE;
import static com.yunsheng.im.protocol.command.Command.MESSAGE_REQUEST;
import static com.yunsheng.im.protocol.command.Command.MESSAGE_RESPONSE;

/**
 * @author uncleY
 * @description: 编解码处理类
 * @date 2019/6/1 15:26
 */
public class Codec {

    // 单例，方便以后使用
    public static final Codec INSTANCE = new Codec();


    // 协议格式
    // 4字节魔数  + 1字节版本号 + 1字节序列化算法 + 1字节指令 + 4字节数据长度 + n字节数据

    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetTypeMap.put(LOGOUT_RESPONSE, LogoutResponsePacket.class);
        packetTypeMap.put(CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        packetTypeMap.put(CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        packetTypeMap.put(JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        packetTypeMap.put(LIST_GROUP_REQUEST, ListGroupRequestPacket.class);
        packetTypeMap.put(LIST_GROUP_RESPONSE, ListGroupResponsePacket.class);
        packetTypeMap.put(EXIT_GROUP_RESPONSE, ExitGroupResponsePacket.class);
        packetTypeMap.put(EXIT_GROUP_REQUEST, ExitGroupRequestPacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    // 魔数
    public static final int MAGIC_NUMBER = 0x12345678;

    // 编码过程
    public ByteBuf encode(ByteBufAllocator alloc, Packet packet) {
        // 1,创建ByteBuf对象
//        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        ByteBuf byteBuf = alloc.ioBuffer();// 改为使用channel自己的分配器

        // 2, 序列化packet对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3， 写入ByteBuf
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    // 编码过程
    public ByteBuf encode4handler(ByteBuf byteBuf, Packet packet) {
        // 2, 序列化packet对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3， 写入ByteBuf
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    // 解码过程
    public Packet decode(ByteBuf byteBuf) {
        // 魔数
        int magic = byteBuf.readInt();
        if (MAGIC_NUMBER != magic) {
            System.err.println("魔数不对!!");
            return null;
        }

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();
        Serializer serializer = serializerMap.get(serializeAlgorithm);

        // 数据包类型
        Class<? extends Packet> packetClass = packetTypeMap.get(byteBuf.readByte());

        // 数据长度
        int length = byteBuf.readInt();

        // 数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        return serializer.deserialize(packetClass, bytes);
    }
}

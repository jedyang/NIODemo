package com.yunsheng.im.protocol.command;

public interface Command {

    Byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;

    Byte MESSAGE_REQUEST = 3;

    Byte MESSAGE_RESPONSE = 4;

    Byte LOGOUT_REQUEST = 5;

    Byte LOGOUT_RESPONSE = 6;

    Byte CREATE_GROUP_RESPONSE = 7;

    Byte CREATE_GROUP_REQUEST = 8;

    Byte JOIN_GROUP_REQUEST = 9;

    Byte JOIN_GROUP_RESPONSE = 10;

    Byte EXIT_GROUP_REQUEST = 11;

    Byte EXIT_GROUP_RESPONSE = 12;

}
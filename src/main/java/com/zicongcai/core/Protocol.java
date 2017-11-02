package com.zicongcai.core;

import com.google.common.primitives.Bytes;
import com.zicongcai.util.ByteUtils;

/**
 * 协议基类
 */
public class Protocol {

    /**
     * 解码 readbuf 中从 start 开始的 length 字节
     *
     * @param readbuf 字节数组
     * @param start   开始的字节下标
     * @param length  解码的字节长度
     * @return 协议类的实例
     */
    public Protocol decode(byte[] readbuf, int start, int length) {
        return new Protocol();
    }

    /**
     * 编码
     *
     * @return 消息字节数组
     */
    public byte[] encode() {
        return new byte[]{};
    }

    /**
     * 打包成协议消息包（在编码后的字节数组添加一个4字节的int，描述消息的总长度）
     *
     * @return 消息包的字节数组
     */
    public byte[] pack() {

        byte[] msgBytes = encode();
        byte[] lenBytes = ByteUtils.int2ByteArray(msgBytes.length);
        byte[] data = Bytes.concat(lenBytes, msgBytes);

        return data;
    }

    /**
     * 获取协议名称
     *
     * @return 协议名称
     */
    public String getName() {
        return "";
    }

    /**
     * 获取协议描述
     *
     * @return
     */
    public String getDesc() {
        return "";
    }
}

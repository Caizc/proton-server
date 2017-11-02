package com.zicongcai.core;

import com.google.common.primitives.Bytes;
import com.zicongcai.util.ByteUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 字节流协议
 */
public class ProtocolBytes extends Protocol {

    private static final Log log = LogFactory.getLog(ProtocolBytes.class);

    private static final String DEFAULT_CHARSET = "UTF8";

    private byte[] bytes;

    @Override
    public Protocol decode(byte[] readbuf, int start, int length) {

        ProtocolBytes proto = new ProtocolBytes();

        if (length > 0) {
            proto.bytes = Arrays.copyOfRange(readbuf, start, start + length);
        }

        return proto;
    }

    @Override
    public byte[] encode() {
        return bytes;
    }

    @Override
    public String getName() {
        return getString(0);
    }

    @Override
    public String getDesc() {

        if (bytes == null) {
            return "";
        }

//        StringBuffer sb = null;
//
//        for (int i = 0; i < bytes.length; i++) {
//            sb.append(Byte.toString(bytes[i]));
//        }
//
//        return sb.toString();

        // FIXME: 直接将 bytes 转为 String 会不会有问题？
        return bytes.toString();
    }

    public void addString(String str) {

        int len = str.length();
        byte[] lenBytes = ByteUtils.int2ByteArray(len);

        byte[] strBytes = null;
        try {
            strBytes = str.getBytes(DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            log.error("Error occur when encoding byte array from String", e);
        }

        if (bytes == null) {
            bytes = Bytes.concat(lenBytes, strBytes);
        } else {
            bytes = Bytes.concat(bytes, lenBytes, strBytes);
        }
    }

    public String getString(int start, int[] end) {

        if (bytes == null) {
            return "";
        }

        if (bytes.length < start + 4) {
            return "";
        }

        byte[] strLenBytes = Arrays.copyOfRange(bytes, start, start + 4);
        int strLen = ByteUtils.byteArray2Int(strLenBytes);

        if (bytes.length < start + 4 + strLen) {
            return "";
        }

        byte[] strBytes = Arrays.copyOfRange(bytes, start + 4, start + 4 + strLen);
        String str = StringUtils.toEncodedString(strBytes, Charset.forName(DEFAULT_CHARSET));

        end[0] = start + 4 + strLen;

        return str;
    }

    public String getString(int start) {

        int[] end = {0};

        return getString(start, end);
    }

    public void addInt(int num) {

        byte[] numBytes = ByteUtils.int2ByteArray(num);

        if (bytes == null) {
            bytes = numBytes;
        } else {
            bytes = Bytes.concat(bytes, numBytes);
        }
    }

    public int getInt(int start, int[] end) {

        if (bytes == null) {
            return 0;
        }

        //FIXME: 这里可能出错，int 类型变量的长度为 4 ？
        if (bytes.length < start + 4) {
            return 0;
        }

        int num = 0;
        byte[] numBytes = Arrays.copyOfRange(bytes, start, start + 4);
        num = ByteUtils.byteArray2Int(numBytes);

        end[0] = start + 4;

        return num;
    }

    public int getInt(int start) {

        int[] end = {0};

        return getInt(start, end);
    }

    public void addFloat(float f) {

        byte[] floatBytes = ByteUtils.float2ByteArray(f);

        bytes = Bytes.concat(bytes, floatBytes);
    }

    public float getFloat(int start, int[] end) {

        if (bytes == null) {
            return 0;
        }

        // FIXME: 这里可能出错，float 类型变量的长度为 4 ？
        if (bytes.length < start + 4) {
            return 0;
        }

        byte[] floatBytes = Arrays.copyOfRange(bytes, start, start + 4);

        end[0] = start + 4;

        return ByteUtils.byteArray2Float(floatBytes);
    }

    public float getfloat(int start) {

        int[] end = {0};

        return getFloat(start, end);
    }
}

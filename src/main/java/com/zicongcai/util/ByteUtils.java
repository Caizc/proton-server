package com.zicongcai.util;

/**
 * 字节工具类
 */
public class ByteUtils {

    public static byte[] float2ByteArray(float f) {

        int num = Float.floatToIntBits(f);

        byte[] bytes = new byte[4];

        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) (num >> 8 * i & 0xFF);
        }

        return bytes;
    }

    public static float byteArray2Float(byte[] bytes) {

        int num = 0;
        byte bt;

        for (int i = 0; i < bytes.length; i++) {
            bt = bytes[i];
            num += (bt & 0xFF) << (8 * i);
        }

        return Float.intBitsToFloat(num);
    }

    public static byte[] int2ByteArray(int num) {

        byte[] bytes = new byte[4];

        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) (num >> 8 * i & 0xFF);
        }

        return bytes;
    }

    public static int byteArray2Int(byte[] bytes) {

        int num = 0;
        byte bt;

        for (int i = 0; i < bytes.length; i++) {
            bt = bytes[i];
            num += (bt & 0xFF) << (8 * i);
        }

        return num;
    }
}

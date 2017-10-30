package com.zicongcai.core;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 字符串协议
 */
public class ProtocolStr extends Protocol {

    private static final Log log = LogFactory.getLog(ProtocolStr.class);

    private static final String DEFAULT_CHARSET = "UTF8";

    private String str;

    @Override
    public Protocol decode(byte[] readbuf, int start, int length) {

        ProtocolStr proto = new ProtocolStr();

        byte[] strBytes = Arrays.copyOfRange(readbuf, start, start + length);
        proto.str = StringUtils.toEncodedString(strBytes, Charset.forName(DEFAULT_CHARSET));

        return proto;
    }

    @Override
    public byte[] encode() {

        byte[] strBytes = null;

        try {
            strBytes = str.getBytes(DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            log.error("Error occur when encoding byte array from String", e);
        }

        return strBytes;
    }

    @Override
    public String getName() {

        if (Strings.isNullOrEmpty(str)) {
            return "";
        }

        String[] strArray = StringUtils.split(str, ",");
        if (strArray == null || strArray.length == 0) {
            return "";
        }

        return strArray[0];
    }

    @Override
    public String getDesc() {
        return str;
    }
}

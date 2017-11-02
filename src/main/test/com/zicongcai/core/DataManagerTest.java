package com.zicongcai.core;

import com.google.common.base.CharMatcher;

public class DataManagerTest {

    public static void main(String[] args) {

        String str1 = "Helloworld";
        String str2 = "h12;";
        String str3 = "@er3";

        System.out.println(isSafeStr(str1));
        System.out.println(isSafeStr(str2));
        System.out.println(isSafeStr(str3));
    }

    private static boolean isSafeStr(String str) {
        String result = CharMatcher.javaLetterOrDigit().retainFrom(str);
        return str.equals(result);
    }
}

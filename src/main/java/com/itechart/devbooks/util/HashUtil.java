package com.itechart.devbooks.util;

import org.springframework.util.DigestUtils;

public class HashUtil {

    public static String stringToHash(String str){
        return DigestUtils.md5DigestAsHex(str.getBytes()).toUpperCase();
    }

    public static boolean compare(String str, String hash){
        String hashedStr = DigestUtils.md5DigestAsHex(str.getBytes()).toUpperCase();
        return hashedStr.equals(hash);
    }
}

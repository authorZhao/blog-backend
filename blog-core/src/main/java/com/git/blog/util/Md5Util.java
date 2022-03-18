package com.git.blog.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Md5Util {
    private static final String[] HEX_DIGITS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public Md5Util() {
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuffer resultSb = new StringBuffer();

        for(int i = 0; i < bytes.length; ++i) {
            resultSb.append(byteToHexString(bytes[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (b < 0) {
            n = b + 256;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }


    public static String md5(String str) {
        return md5Encode(str, StandardCharsets.UTF_8);
    }


    public static String md5Encode(String origin, Charset charset) {
        String resultString = null;

        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes(charset)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
}

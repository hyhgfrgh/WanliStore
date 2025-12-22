package com.wanli.WanliStore.utils;

import java.security.MessageDigest;
import java.util.HexFormat;

public class Encrypt {
    public static String hash(String input, String algorithm) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(input.getBytes());
            return HexFormat.of().formatHex(digest); // Java 17+
        } catch (Exception e) {
            return "";
        }
    }
}

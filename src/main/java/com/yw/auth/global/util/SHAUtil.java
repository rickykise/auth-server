package com.yw.auth.global.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtil {
    private static final String CRYPT_ALGORITHM = "SHA-512";
    private static final String CRYPT_CHARSET = "UTF-8";


    public static String encrypt(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return encrypt(CRYPT_ALGORITHM, CRYPT_CHARSET, input);
    }

    private static String encrypt(String algorithm, String charSet, String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(input.getBytes(charSet));
        byte[] byteData = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte byteDatum : byteData) {
            sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}

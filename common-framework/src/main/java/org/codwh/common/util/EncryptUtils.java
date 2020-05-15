package org.codwh.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SuppressWarnings("all")
public class EncryptUtils {

    public static final String MD5_16 = "MD5-16";
    public static final String MD5_32 = "MD5-32";
    public static final String SHA_256 = "SHA-256";
    public static final String SHA_512 = "SHA-512";
    public static final String BASE64 = "BASE64";
    public static final String URL = "URL";
    public static final String UTF8 = "UTF-8";

    /***
     * 加密
     *
     * @param pattern
     * @param text
     * @return
     */
    public static String encrypt(String text, String pattern) {
        if (pattern.equals(MD5_16)) {
            return encryptMD5(text, 16);
        } else if (pattern.equals(MD5_32)) {
            return encryptMD5(text, 32);
        } else if (pattern.equals(SHA_256)) {
            return encryptSHA(text, SHA_256);
        } else if (pattern.equals(SHA_512)) {
            return encryptSHA(text, SHA_512);
        } else if (pattern.equals(BASE64)) {
            return encryptBASE64(text, false);
        } else if (pattern.equals(URL)) {
            return encryptBASE64(text, true);
        }
        return null;
    }

    /**
     * MD5 16位和32位加密
     *
     * @param text
     * @param len
     * @return
     */
    private static String encryptMD5(String text, int len) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(text.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < bytes.length; i++) {
                int bit = bytes[i];
                if (bit < 0) {
                    bit += 256;
                }
                if (bit < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(bit));
            }

            //16位加密
            if (len == 16) {
                return sb.toString().substring(8, 24);
            } else {
                return sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * SHA 加密算法，支持SHA256与SHA512
     *
     * @param text
     * @param shaType
     * @return
     */
    private static String encryptSHA(String text, String shaType) {
        try {
            MessageDigest messageDigest = null;
            if (shaType.equals(SHA_256)) {
                messageDigest = MessageDigest.getInstance(SHA_256);
            } else {
                messageDigest = MessageDigest.getInstance(SHA_512);
            }
            messageDigest.update(text.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                int bit = bytes[i];
                if (bit < 0) {
                    bit += 256;
                }
                if (bit < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(bit));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Base64加密算法，支持base和url两种加密方式
     *
     * @param text
     * @param url
     * @return
     */
    public static String encryptBASE64(String text, boolean url) {
        try {
            if (!url) {
                byte[] encoded = Base64.getEncoder().encode(text.getBytes(UTF8));
                return new String(encoded, UTF8);
            } else {
                byte[] encoded = Base64.getUrlEncoder().encode(text.getBytes(UTF8));
                return new String(encoded, UTF8);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String text, String pattern) {
        if (pattern.equals(BASE64)) {
            return decryptBASE64(text, false);
        } else if (pattern.equals(URL)) {
            return decryptBASE64(text, true);
        }
        return null;
    }

    /**
     * Base64解密算法
     *
     * @param text
     * @param url
     * @return
     */
    public static String decryptBASE64(String text, boolean url) {
        try {
            if (url) {
                byte[] decoded = Base64.getUrlDecoder().decode(text.getBytes(UTF8));
                return new String(decoded, UTF8);
            } else {
                byte[] decoded = Base64.getDecoder().decode(text.getBytes(UTF8));
                return new String(decoded, UTF8);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

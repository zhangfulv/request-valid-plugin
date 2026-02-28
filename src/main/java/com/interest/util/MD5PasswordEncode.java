package com.interest.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * @ClassName MD5PasswordEncode
 * @Author xulian
 * @Date 2021/1/25 17:15
 */
public class MD5PasswordEncode  {
    private final static String ALGORITHM = "MD5";

    public static String encodePassword(String rawPass, Object salt) {
        MessageDigest messageDigest = getMessageDigest();
        String saltedPass = mergePasswordAndSalt(rawPass, salt);
        byte[] digest = messageDigest.digest(saltedPass.getBytes());
        return new String(encodeHex(digest));
    }

    public static boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        String pass1 = "" + encPass;
        String pass2 = encodePassword(rawPass, salt);

        pass1 = pass1.toLowerCase(Locale.ENGLISH);
        pass2 = pass2.toLowerCase(Locale.ENGLISH);

        return pass1.equals(pass2);
    }

    private static final MessageDigest getMessageDigest() throws IllegalArgumentException {
        try {
            return MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm [" + ALGORITHM + "]");
        }
    }

    private static String mergePasswordAndSalt(String password, Object salt) {
        if (password == null) {
            password = "";
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }

    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};

    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }

    public static void main(String[] args) {
        String str = "SETTLEMENT=1,_dc=1611904341639&iii=1611904341641&limit=20&page=1&start=0";
        //SETTLEMENT=1,_dc=1611904341639&iii=1611904341641&limit=20&page=1&start=0
        //SETTLEMENT=1&_dc=1611904341639&iii=1611904341641&limit=20&page=1&start=0
        System.out.println("args = [" + MD5PasswordEncode.encodePassword(str,null)+ "]");
    }
}





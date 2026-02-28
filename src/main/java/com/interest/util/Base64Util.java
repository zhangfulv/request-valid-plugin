package com.interest.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @ClassName Base64Util
 * @Author Mr.zf, link:zhangfu@broadtech.com.cn
 * @Date 2023/8/28 13:45
 */
public class Base64Util {

     /**
      * @Description  base64解码
      * @param
      * @return
      * @Author Mr.zf.link:zhangfu@broadtech.com.cn
      * @Date 2023-08-28 13:48
      * @Update
      **/
    public static String decode(String base64EncodeStr) throws UnsupportedEncodingException {
         return  new String(Base64.getDecoder().decode(base64EncodeStr),StandardCharsets.UTF_8);
    }

    public static String encode(byte[] strBytes) throws UnsupportedEncodingException {
        return  new String(Base64.getEncoder().encode(strBytes),StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        try {
            String decode = "dGFibGU9U1lTTUVOVSZjb25kaXRpb249eyJzdGF0dXMiOjEsIm1vZHVsZWlkIjoiQGUl5byg5LiJIiwidHlwZSI6IkBpbjEsMiwzLDQifSZzb3J0PWluZGV4";
            System.err.println(Base64Util.decode(decode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

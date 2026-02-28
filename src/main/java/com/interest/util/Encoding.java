//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.interest.util;

import java.net.URLEncoder;
import java.nio.charset.Charset;

public class Encoding {
    public Encoding() {
    }

    public static String getEncoding(String str) {
        String encode = "UTF-16";

        try {
            if (str.equals(new String(str.getBytes(), encode))) {
                return encode;
            }
        } catch (Exception var7) {
        }

        encode = "ASCII";

        try {
            if (str.equals(new String(str.getBytes(), encode))) {
                return "字符串<< " + str + " >>中仅由数字和英文字母组成，无法识别其编码格式";
            }
        } catch (Exception var6) {
        }

        encode = "ISO-8859-1";

        try {
            if (str.equals(new String(str.getBytes(), encode))) {
                return encode;
            }
        } catch (Exception var5) {
        }

        encode = "GB2312";

        try {
            if (str.equals(new String(str.getBytes(), encode))) {
                return encode;
            }
        } catch (Exception var4) {
        }

        encode = "UTF-8";

        try {
            if (str.equals(new String(str.getBytes(), encode))) {
                return encode;
            }
        } catch (Exception var3) {
        }

        return "未识别编码格式";
    }

    public static String gb2312ToUtf8(String str) {
        String urlEncode = "";

        try {
            urlEncode = URLEncoder.encode(str, "UTF-8");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return urlEncode;
    }

    public static String gbk2utf8(String gbk) {
        String l_temp = GBK2Unicode(gbk);
        l_temp = unicodeToUtf8(l_temp);
        return l_temp;
    }

    public static String GBK2Unicode(String str) {
        StringBuffer result = new StringBuffer();

        for(int i = 0; i < str.length(); ++i) {
            char chr1 = str.charAt(i);
            if (!isNeedConvert(chr1)) {
                result.append(chr1);
            } else {
                result.append("\\u" + Integer.toHexString(chr1));
            }
        }

        return result.toString();
    }

    public static boolean isNeedConvert(char para) {
        return (para & 255) != para;
    }

    public static String unicodeToUtf8(String theString) {
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        int x = 0;

        while(true) {
            while(true) {
                while(x < len) {
                    char aChar = theString.charAt(x++);
                    if (aChar == '\\') {
                        aChar = theString.charAt(x++);
                        if (aChar == 'u') {
                            int value = 0;

                            for(int i = 0; i < 4; ++i) {
                                aChar = theString.charAt(x++);
                                switch (aChar) {
                                    case '0':
                                    case '1':
                                    case '2':
                                    case '3':
                                    case '4':
                                    case '5':
                                    case '6':
                                    case '7':
                                    case '8':
                                    case '9':
                                        value = (value << 4) + aChar - 48;
                                        break;
                                    case ':':
                                    case ';':
                                    case '<':
                                    case '=':
                                    case '>':
                                    case '?':
                                    case '@':
                                    case 'G':
                                    case 'H':
                                    case 'I':
                                    case 'J':
                                    case 'K':
                                    case 'L':
                                    case 'M':
                                    case 'N':
                                    case 'O':
                                    case 'P':
                                    case 'Q':
                                    case 'R':
                                    case 'S':
                                    case 'T':
                                    case 'U':
                                    case 'V':
                                    case 'W':
                                    case 'X':
                                    case 'Y':
                                    case 'Z':
                                    case '[':
                                    case '\\':
                                    case ']':
                                    case '^':
                                    case '_':
                                    case '`':
                                    default:
                                        throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                                    case 'A':
                                    case 'B':
                                    case 'C':
                                    case 'D':
                                    case 'E':
                                    case 'F':
                                        value = (value << 4) + 10 + aChar - 65;
                                        break;
                                    case 'a':
                                    case 'b':
                                    case 'c':
                                    case 'd':
                                    case 'e':
                                    case 'f':
                                        value = (value << 4) + 10 + aChar - 97;
                                }
                            }

                            outBuffer.append((char)value);
                        } else {
                            if (aChar == 't') {
                                aChar = '\t';
                            } else if (aChar == 'r') {
                                aChar = '\r';
                            } else if (aChar == 'n') {
                                aChar = '\n';
                            } else if (aChar == 'f') {
                                aChar = '\f';
                            }

                            outBuffer.append(aChar);
                        }
                    } else {
                        outBuffer.append(aChar);
                    }
                }

                return outBuffer.toString();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("系统默认编码：" + System.getProperty("file.encoding"));
        System.out.println("系统默认字符编码：" + Charset.defaultCharset());
        System.out.println("系统默认语言：" + System.getProperty("user.language"));
        System.out.println();
        String s1 = "hi, nice to meet you!";
        String s2 = "hi, 我来了！";
        System.out.println(getEncoding(s1));
        System.out.println(getEncoding(s2));
    }
}

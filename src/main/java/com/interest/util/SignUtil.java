//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.interest.util;

import cn.hutool.core.lang.Console;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.regex.Pattern;

public class SignUtil {
    private static final Log logger = LogFactory.getLog(SignUtil.class);

    public SignUtil() {
    }

    /** @deprecated */
    @Deprecated
    public static boolean signature(String sign, Map<String, String> map, String encodeBodyString) {
        return signature(sign, map, encodeBodyString, "");
    }

    public static boolean signature(String sign, Map<String, String> map, String encodeBodyString, String salt) {
        if (StringUtils.isNotBlank(encodeBodyString)) {
            map.put("bodystr", encodeBodyString);
        }

        String linkStringByGetWithSort = createLinkStringByGetWithSort(map);
        logger.info("linkStringByGetWithSort:" + linkStringByGetWithSort);
        Console.log("linkStringByGetWithSort -> {}", new Object[]{linkStringByGetWithSort});
        return MD5PasswordEncode.isPasswordValid(sign, linkStringByGetWithSort, salt);
    }

    public static String createLinkStringByGetWithSort(Map<String, String> params) {
        List<String> keys = new ArrayList(params.keySet());
        Collections.sort(keys);
        return getLinkString(params, keys);
    }

    private static String getLinkString(Map<String, String> params, List<String> keys) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < keys.size(); ++i) {
            String key = (String)keys.get(i);
            if (!key.equals("signnn")) {
                String value = (String)params.get(key);
                if (i == keys.size() - 1) {
                    stringBuilder.append(key).append("=").append(value);
                } else {
                    stringBuilder.append(key).append("=").append(value).append("&");
                }
            }
        }

        if (stringBuilder.toString().lastIndexOf("&") == stringBuilder.length() - 1) {
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        } else {
            return stringBuilder.toString();
        }
    }

    public static boolean checkTime(String time, Long variable) {
        if (StringUtils.isBlank(time)) {
            return false;
        } else {
            Long timestamp = Long.valueOf(time);
            Long currentTimeMillis = System.currentTimeMillis();
            Long addTime = currentTimeMillis + (long)variable;
            Long subTime = currentTimeMillis - (long)variable;
            return addTime > timestamp && timestamp > subTime;
        }
    }

    private static boolean filterUrls(String excludePath, String reqUrl) {
        String regPath = getRegPath(excludePath);
        return Pattern.compile(regPath).matcher(reqUrl).matches();
    }

    public static boolean checkBlackList(List<String> excludeUrls, String reqUrl) {
        Iterator var2 = excludeUrls.iterator();

        String url;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            url = (String)var2.next();
        } while(!filterUrls(url, reqUrl));

        return true;
    }

    private static String getRegPath(String path) {
        char[] chars = path.toCharArray();
        int len = chars.length;
        StringBuilder sb = new StringBuilder();
        boolean preX = false;

        for(int i = 0; i < len; ++i) {
            if (chars[i] == '*') {
                if (preX) {
                    sb.append(".*");
                    preX = false;
                } else if (i + 1 == len) {
                    sb.append("[^/]*");
                } else {
                    preX = true;
                }
            } else {
                if (preX) {
                    sb.append("[^/]*");
                    preX = false;
                }

                if (chars[i] == '?') {
                    sb.append('.');
                } else {
                    sb.append(chars[i]);
                }
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
       /* boolean b = checkTime("1617063675520", 30000);
        System.out.println("args = [" + b + "]");
        String bodyString = "";
        if (StringUtils.isNotBlank(bodyString)) {
            bodyString = JSONObject.toJSONString(JSONObject.parseObject(bodyString), new SerializerFeature[]{SerializerFeature.MapSortField, SerializerFeature.WriteMapNullValue});
        }

        System.out.println("args = [" + bodyString + "]");
        if (StringUtils.isNotBlank(bodyString)) {
            System.out.println("yes");
        }*/

    }
}

package com.interest.util;

import cn.hutool.core.lang.Console;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 签名校验
 * @author xulian
 * @date 2020/7/15
 */
public class SignUtil {

    private static final Log logger = LogFactory.getLog(SignUtil.class);

    private static final String salt = "01234567890iccs0";

    public static boolean signature(String sign, Map<String, String> map, String encodeBodyString) {
        if (StringUtils.isNotBlank(encodeBodyString)) {
            map.put("bodystr", encodeBodyString);
        }
        String linkStringByGetWithSort = createLinkStringByGetWithSort(map);

        logger.info("linkStringByGetWithSort:" + linkStringByGetWithSort);
        Console.log("linkStringByGetWithSort -> {}",linkStringByGetWithSort);
        return MD5PasswordEncode.isPasswordValid(sign, linkStringByGetWithSort, null);
    }

    public static boolean signature(String sign,String user_id,String token,String time) {
        if (StringUtils.isNotBlank(sign) && StringUtils.isNotBlank(user_id)
                && StringUtils.isNotBlank(token) && StringUtils.isNotBlank(time)) {
            String deToken = RSAUtil.getInstance().decode(token);
            String formatStr = String.format("token=%s&time=%s&userid=%s", deToken, time, user_id);
            logger.info("sign data:" + formatStr);
            Console.log("sign data -> {},deToken ->{}",formatStr,deToken);
            return MD5PasswordEncode.isPasswordValid(sign, formatStr, null);
        }
        return false;
    }


    public static String createLinkStringByGetWithSort(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        return getLinkString(params, keys);
    }

    private static String getLinkString(Map<String, String> params, List<String> keys) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {
                stringBuilder.append(key).append("=").append(value);
            } else {
                stringBuilder.append(key).append("=").append(value).append("&");
            }

        }
        return stringBuilder.toString();
    }

    public static boolean checkTime(String time, Integer variable){
        if (StringUtils.isBlank(time)) {
            return false;
        }
        Long timestamp = Long.valueOf(time);
        Long currentTimeMillis = System.currentTimeMillis();
        Long addTime = currentTimeMillis + variable;
        Long subTime = currentTimeMillis - variable;
        if (addTime > timestamp && timestamp > subTime){
            return true;
        }
        return false;
    }

    /**
     * 通配符模式
     *
     * @param excludePath - 不过滤地址
     * @param reqUrl      - 请求地址
     * @return
     */
    private static boolean filterUrls(String excludePath, String reqUrl) {
        String regPath = getRegPath(excludePath);
        return Pattern.compile(regPath).matcher(reqUrl).matches();
    }

    /**
     * 检验是否在非过滤地址
     *
     * @param excludeUrls
     * @param reqUrl
     * @return
     */
    public static boolean checkBlackList(List<String> excludeUrls, String reqUrl) {
        for (String url : excludeUrls) {
            if (filterUrls(url, reqUrl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将通配符表达式转化为正则表达式
     *
     * @param path
     * @return
     */
    private static String getRegPath(String path) {
        char[] chars = path.toCharArray();
        int len = chars.length;
        StringBuilder sb = new StringBuilder();
        boolean preX = false;
        for (int i = 0; i < len; i++) {
            if (chars[i] == '*') {//遇到*字符
                if (preX) {//如果是第二次遇到*，则将**替换成.*
                    sb.append(".*");
                    preX = false;
                } else if (i + 1 == len) {//如果是遇到单星，且单星是最后一个字符，则直接将*转成[^/]*
                    sb.append("[^/]*");
                } else {//否则单星后面还有字符，则不做任何动作，下一把再做动作
                    preX = true;
                    continue;
                }
            } else {//遇到非*字符
                if (preX) {//如果上一把是*，则先把上一把的*对应的[^/]*添进来
                    sb.append("[^/]*");
                    preX = false;
                }
                if (chars[i] == '?') {//接着判断当前字符是不是?，是的话替换成.
                    sb.append('.');
                } else {//不是?的话，则就是普通字符，直接添进来
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(MD5PasswordEncode.encodePassword("access_token=d97071d6-ed1e-4623-aee0-97d781f9cd36&iii=1611121750683&num=5&orgId=5&bodystr=99914b932bd37a50b983c5e7c90ae93b", null));
    }
}

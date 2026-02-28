//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.interest.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.interest.pojo.DefaultBlackListConfig;
import com.interest.util.MD5PasswordEncode;
import com.interest.util.RequestUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.interest.util.SignUtil;
import com.interest.util.VerifyConstants;
import com.interest.wrapper.DefaultBodyHttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;

public class DefaultVerifySign implements IVerifySign {

    public <T> T getBlackListBean(T cls) {
        T definedBean = BeanUtil.fillBeanWithMap(VerifyConstants.VALID_CONFIG, cls, false);
        return definedBean;
    }

    public boolean shouldFilter(HttpServletRequest request) {
        try {
            DefaultBlackListConfig defaultBlackListConfig = (DefaultBlackListConfig) this.getBlackListBean(new DefaultBlackListConfig());
            String requestUri = request.getRequestURI();
            String contentType = request.getContentType();
            String queryString = request.getQueryString();
            Console.log("requestUri -> {}", new Object[]{requestUri});
            Console.log("ContentType -> {}", new Object[]{contentType});
            List<String> uriConfigList = defaultBlackListConfig.getUri();
            List<String> whiteuriList = defaultBlackListConfig.getWhiteuri();
            List<String> whiteQueryString = defaultBlackListConfig.getWhiteQueryString();
            return SignUtil.checkBlackList(uriConfigList, requestUri) &&
                    !SignUtil.checkBlackList(whiteuriList, requestUri) &&
                    !SignUtil.checkBlackList(whiteQueryString, queryString);
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public boolean verifySign(HttpServletRequest request, HttpServletResponse response) {
        try {
            Console.log("Server Default Charset -> {}", new Object[]{Charset.defaultCharset()});
            DefaultBlackListConfig blackListConfig = (DefaultBlackListConfig) this.getBlackListBean(new DefaultBlackListConfig());
            Map<String, String> param = this.getAllRequestParam(request);
            if (param.isEmpty()) {
                return true;
            } else {
                String signHeader = request.getHeader("signnn");
                if (StringUtils.isBlank(signHeader)) {
                    signHeader = (String) param.get("signnn");
                }
                String bodyString = "";
                try {
                    bodyString = this.getBodyString(request);
                    bodyString = this.jsonStringSort(bodyString);
                } catch (Exception var14) {
                    var14.printStackTrace();
                }
                String encodeBodyString = null;
                if (StringUtils.isNotBlank(bodyString)) {
                    encodeBodyString = MD5PasswordEncode.encodePassword(bodyString, blackListConfig.getEncodeSalt());
                }

                Console.log("bodyString -> {}", new Object[]{bodyString});
                Console.log("encodeBodyString -> {}", new Object[]{encodeBodyString});
                Console.log("signHeader -> {}", new Object[]{signHeader});
                boolean valid = SignUtil.signature(signHeader, param, encodeBodyString, blackListConfig.getEncodeSalt());
                if (!valid) {
                    Console.log("signHeader error -> {}", new Object[]{signHeader});
                    this.responseMessage(response, 403, VerifyConstants.UNICODE_SIGN_ERROR);
                    return false;
                } else {
                    //校验正常.
                    if (VerifyConstants.SIGN_CACHE.containsKey(signHeader)) {
                        Console.log("signHeader reconvert -> {}", new Object[]{signHeader});
                        this.responseMessage(response, 403, VerifyConstants.UNICODE_SIGN_ERROR);
                        return false;
                    }
                    String timeout = blackListConfig.getTimeout();
                    Long lSecondTimeout = 300L; //默认5分钟
                    if (timeout.indexOf("ms") != -1) {
                        lSecondTimeout = Long.parseLong(timeout.replace("ms", "")) / 1000;
                    } else if (timeout.indexOf("s") != -1) {
                        lSecondTimeout = Long.parseLong(timeout.replace("s", ""));
                    }
                    synchronized (VerifyConstants.syncObj) {
                        //如果没有，则进行填充.
                        VerifyConstants.SIGN_CACHE.put(signHeader, 1, lSecondTimeout);
                    }
                    String timeStamp = request.getParameter("iii");
                    if (StringUtils.isBlank(timeStamp)) {
                        this.responseMessage(response, 403, VerifyConstants.UNICODE_SIGN_ERROR);
                        return false;
                    } else {
                        boolean checkTime = true;
                        checkTime = SignUtil.checkTime(timeStamp, lSecondTimeout * 1000 * 1000);
                        if (!checkTime) {
                            Console.log("timeStamp time out -> {}", new Object[]{timeStamp});
                            this.responseMessage(response, 403, VerifyConstants.UNICODE_SIGN_ERROR);
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception var15) {
            var15.printStackTrace();
            return false;
        }
    }

    private String jsonStringSort(String bodyString) {
        try {
            if (StringUtils.isNotBlank(bodyString)) {
                bodyString = JSON.toJSONString(JSON.parseObject(bodyString), new SerializerFeature[]{SerializerFeature.MapSortField, SerializerFeature.WriteMapNullValue});
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            Console.log("body str sort error:->{}", new Object[]{bodyString});
        }

        return bodyString;
    }

    private Map<String, String> getAllRequestParam(HttpServletRequest request) {
        DefaultBlackListConfig defaultBlackListConfig = (DefaultBlackListConfig) this.getBlackListBean(new DefaultBlackListConfig());
        Map<String, String> res = new HashMap();
        Enumeration<?> temp = request.getParameterNames();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String[] values = (String[]) parameterMap.get(en);
                StringBuilder strBuilder = new StringBuilder();
                String[] var9 = values;
                int var10 = values.length;

                for (int var11 = 0; var11 < var10; ++var11) {
                    String value = var9[var11];
                    if (strBuilder.length() == 0) {
                        strBuilder.append(value);
                    } else {
                        strBuilder.append(",").append(value);
                    }
                }

                if (defaultBlackListConfig.getEmptyValueJoinVerify().equals("true")) {
                    res.put(en, strBuilder.toString());
                } else if (strBuilder.length() != 0) {
                    res.put(en, strBuilder.toString());
                }
            }
        }

        return res;
    }

    /**
     * @deprecated
     */
    @Deprecated
    private Map<String, String> getAllRequestParam_old(HttpServletRequest request) {
        Map<String, String> res = new HashMap();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
            }
        }

        return res;
    }

    private String getBodyString(HttpServletRequest request) {
        try {
            if (RequestUtil.isReadBodyContent(request)) {
                DefaultBodyHttpServletRequestWrapper defaultBodyHttpServletRequestWrapper = (DefaultBodyHttpServletRequestWrapper) request;
                return defaultBodyHttpServletRequestWrapper.getBody();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return "";
    }

    private boolean responseMessage(HttpServletResponse response, int status, String message) {
        try {
            if (VerifyConstants.UNICODE_SIGN_ERROR.equals(message)) {
                JSONObject json = new JSONObject();
                json.put("code", "-99");
                json.put("message", "非法请求");
                json.put("subMessage", "请确保使用指定系统进行操作");
                message = json.toJSONString();
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(status);
            response.getOutputStream().write(message.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

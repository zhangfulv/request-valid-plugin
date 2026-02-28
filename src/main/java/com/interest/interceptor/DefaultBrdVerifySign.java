package com.interest.interceptor;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Console;
import com.interest.pojo.DefaultBlackListConfig;
import com.interest.util.MD5PasswordEncode;
import com.interest.util.SignUtil;
import com.interest.util.VerifyConstants;
import com.interest.wrapper.IBodyTrans;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DefaultBrdVerifySign
 * @DESCRIPTION TODO
 * @Author Mr.zf, link:282734967@qq.com
 * @Date 2021/2/1 14:59
 */
public class DefaultBrdVerifySign implements IVerifySign {
    @Autowired
    private IBodyTrans bodyTrans;

    private static final String LOGIN_USER = "login_user";

    @Override
    public <T> T getBlackListBean(T cls) {
        T definedBean = BeanUtil.fillBeanWithMap(VerifyConstants.VALID_CONFIG, cls, false);
        return definedBean;
    }

    @Override
    public boolean shouldFilter(HttpServletRequest request) {
        try {
            DefaultBlackListConfig defaultBlackListConfig = getBlackListBean(new DefaultBlackListConfig());
            String requestUri = request.getRequestURI();
            String contentType = request.getContentType();
            Console.log("requestUri -> {}", requestUri);
            Console.log("ContentType -> {}", contentType);
            //2021-02-22 zf 去掉contentType校验
          /*  List<String> contentTypeBlackList = defaultBlackListConfig.getContenttype();
            if (contentType != null && !contentTypeBlackList.contains(contentType)) {
                return false;
            }*/
            List<String> uriConfigList = defaultBlackListConfig.getUri();
            return SignUtil.checkBlackList(uriConfigList, requestUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public boolean verifySign(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 案例：拦截所有都服务接口，判断服务接口上是否有传递userToekn参数
            Map<String, String> param = getAllRequestParam(request);

            //没有请求参数,直接放过
            if (param.isEmpty()) {
                return true;
            }

            //验证token时候 token的参数 从请求头获取
            String signHeader = request.getHeader(VerifyConstants.UNICODE_CLIENT_SIGN);
            String tokenHeader = request.getHeader(VerifyConstants.UNICODE_CLIENT_TOKEN);
            String clientTimeHeader = request.getHeader(VerifyConstants.UNICODE_CLIENT_TIME);

            String bodyString = "";
            try {
                bodyString = getBodyString(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String encodeBodyString = null;
            if (StringUtils.isNotBlank(bodyString)) {
                encodeBodyString = MD5PasswordEncode.encodePassword(bodyString, null);
            }
            Console.log("bodyString -> {}" , bodyString);
            Console.log("encodeBodyString -> {}" , encodeBodyString);

            Console.log("signHeader -> {}" ,  signHeader);
            Object login_user = request.getSession().getAttribute(LOGIN_USER);
            String str_user_id = login_user == null ? "" : String.valueOf(login_user);
            boolean valid = SignUtil.signature(signHeader, str_user_id,tokenHeader, clientTimeHeader);

            if (!valid) {
                //返回错误提示
                //false  不会继续往下执行 不会调用服务接口了 网关直接响应给客户了
                Console.log("signHeader error -> {}" ,signHeader);
                responseMessage(response,VerifyConstants.UNICODE_SIGN_ERROE_STATUS,VerifyConstants.UNICODE_SIGN_ERROR);
                return false;
            }

            DefaultBlackListConfig blackListConfig = getBlackListBean(new DefaultBlackListConfig());
            String timeout = blackListConfig.getTimeout();
            boolean checkTime = true;
            //2021-03-01 zf 新增超时判断
            if(timeout == null || "".equals(timeout) || timeout.length() == 0) {
                timeout = "no set";
                Console.log("timeout is empty , so set -> {}" + timeout);
            }
            if(timeout.indexOf("ms") != -1){
                checkTime = SignUtil.checkTime(clientTimeHeader, Integer.parseInt(timeout.replace("ms","")));
            }else if(timeout.indexOf("s") != -1){
                checkTime = SignUtil.checkTime(clientTimeHeader, Integer.parseInt(timeout.replace("s","")) * 1000);
            }else{
                //默认300s过期时间
                checkTime = SignUtil.checkTime(clientTimeHeader, 300 * 1000);
            }
            if (!checkTime) {
                //false  不会继续往下执行 不会调用服务接口了 网关直接响应给客户了
                Console.log("timeStamp error -> {}" + clientTimeHeader);
                responseMessage(response,VerifyConstants.UNICODE_SIGN_ERROE_STATUS,VerifyConstants.UNICODE_SIGN_ERROR);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }

        //否则正常执行 调用服务接口...
        return true;
    }
    //2021-03-05 10:20 胡云川  传参数包含数组的解析
     /**
      * @Description
      * @param
      * @return
      * @Author Mr.zf.link:282734967@qq.com
      * @Date 2021-03-05 上午10:34
      * @Update  胡云川  传参数包含数组的解析
      **/
    private Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<>();
        Enumeration<?> temp = request.getParameterNames();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String[] values = parameterMap.get(en);
                StringBuilder strBuilder = new StringBuilder();
                for(String value:values){
                    if(strBuilder.length() == 0){
                        strBuilder.append( value );
                    }else{
                        strBuilder.append(",").append(value);
                    }
                }
                res.put(en, strBuilder.toString());
            }
        }
        return res;
    }
    @Deprecated
    private Map<String, String> getAllRequestParam_old(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<>();
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
        return bodyTrans.getBody();
    }
    private boolean responseMessage(HttpServletResponse response,int status,String message) {
        try {
            response.setStatus(status);
            response.getOutputStream().write(message.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}

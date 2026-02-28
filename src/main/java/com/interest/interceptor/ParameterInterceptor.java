package com.interest.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.interest.interceptor.IVerifySign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName ParameterInterceptor
 * @DESCRIPTION TODO
 * @Author xulian
 * @Date 2021/1/25 17:21
 */
public class ParameterInterceptor implements HandlerInterceptor {
    @Autowired
    private IVerifySign verifySign;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(verifySign.shouldFilter(httpServletRequest)){
            return verifySign.verifySign(httpServletRequest,httpServletResponse);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}


package com.interest.wrapper;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName DefualtBodyTrans
 * @Author Mr.zf, link:282734967@qq.com
 * @Date 2021/1/29 8:38
 */
public class DefualtBodyTrans implements IBodyTrans {
    private DetaultBodyHttpServletRequestWrapper detaultBodyHttpServletRequestWrapper = null;
    @Override
    public String getBody() {
        return detaultBodyHttpServletRequestWrapper.getBody();
    }

    @Override
    public IBodyTrans builder(HttpServletRequest request) throws IOException{
        detaultBodyHttpServletRequestWrapper = new DetaultBodyHttpServletRequestWrapper(request);
        return this;
    }

    @Override
    public ServletRequest getServletRequest() {
        return this.detaultBodyHttpServletRequestWrapper;
    }
}

package com.interest.wrapper;


import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Deprecated
public interface IBodyTrans {
    public String getBody();
    public IBodyTrans builder(HttpServletRequest request) throws IOException;
    public ServletRequest getServletRequest();
}

package com.interest.filter;

import com.interest.wrapper.IBodyTrans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName RequserAgainFilter
 * @Author Mr.zf, link:282734967@qq.com
 * @Date 2021/1/18 10:33
 */
@Configuration
public class RequserAgainFilter implements Filter {
    @Autowired
    private IBodyTrans bodyTrans;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(
                bodyTrans.builder((HttpServletRequest)servletRequest).getServletRequest(),
                servletResponse);
    }


    @Override
    public void destroy() {

    }

}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.interest.util;

import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
    public RequestUtil() {
    }

    public static boolean isReadBodyContent(HttpServletRequest request) {
        StandardServletMultipartResolver standardServletMultipartResolver = new StandardServletMultipartResolver();
        if (!standardServletMultipartResolver.isMultipart(request)) {
            String header = request.getHeader("Content-Type");
            if (header != null && header != "" && header.indexOf("application/x-www-form-urlencoded") < 0) {
                return true;
            }
        }

        return false;
    }
}

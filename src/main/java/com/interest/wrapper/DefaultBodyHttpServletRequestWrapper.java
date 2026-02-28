package com.interest.wrapper;

import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @ClassName BodyReaderHttpServletRequestWrapper
 * @DESCRIPTION TODO
 * @Author Mr.zf, link:282734967@qq.com
 * @Date 2021/1/18 10:34
 */
public class DefaultBodyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;
    private ServletInputStream inputStream;
    public DefaultBodyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StandardServletMultipartResolver standardServletMultipartResolver = new StandardServletMultipartResolver();
        //做判断，过滤掉form表单形式的，避免form表单的参数
        if(standardServletMultipartResolver.isMultipart(request)){

        }else {
            body = copyToByteArray(request.getInputStream());
            inputStream = new RequestCachingInputStream(body);
        }
    }
    private  byte[] copyToByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
        copy((InputStream)in, out);
        return out.toByteArray();
    }
    private  int copy(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[4096];

        int bytesRead;
        for(boolean var4 = true; (bytesRead = in.read(buffer)) != -1; byteCount += bytesRead) {
            out.write(buffer, 0, bytesRead);
        }

        out.flush();
        return byteCount;
    }
    public String getBody(){

        return new String(body);
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (inputStream != null) {
            return inputStream;
        }
        return super.getInputStream();
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return super.getParameterMap();
    }



}

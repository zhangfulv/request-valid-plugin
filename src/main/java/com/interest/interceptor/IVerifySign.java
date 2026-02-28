package com.interest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IVerifySign {
     /**
      * @Description   得到自定义验签bean
      * @param
      * @return
      * @Author Mr.zf.link:282734967@qq.com
      * @Date 2021-02-01 15:11
      * @Update
      **/
    <T> T getBlackListBean(T cls);
     /**
      * @Description   判断是否进行验签
      * @param  request
      * @return true ：需要验签  false：不需要
      * @Author Mr.zf.link:282734967@qq.com
      * @Date 2021-02-01 14:57
      * @Update
      **/
    boolean shouldFilter(HttpServletRequest request);

     /**
      * @Description   验签
      * @param
      * @return
      * @Author Mr.zf.link:282734967@qq.com
      * @Date 2021-02-01 15:08
      * @Update
      **/
     boolean verifySign(HttpServletRequest request,HttpServletResponse response);


}

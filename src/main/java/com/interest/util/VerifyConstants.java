package com.interest.util;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

import java.util.HashMap;
import java.util.Map;

/**
  * @Description   系统常量
  * @Author Mr.zf.link:282734967@qq.com
  * @Date 2021-01-18 17:20
  **/
public class VerifyConstants {
   public volatile static Map<String,Object> VALID_CONFIG = new HashMap<>();


   public static final String UNICODE_SIGN_KEY = "signnn";
   public static final String UNICODE_SIGN_ERROR = "sign error";
   public static final int UNICODE_SIGN_ERROE_STATUS = 403;

   /**
   * desc: 创建一个缓存器，默认30s清空.
   */
   public volatile static TimedCache<String,Integer> SIGN_CACHE = CacheUtil.newTimedCache(30);
   public static final Object syncObj = new Object();


}

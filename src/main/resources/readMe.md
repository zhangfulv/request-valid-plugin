## 使用方式:
### 
1.将jar包引入到对应项目中
```
    <dependency>
        <groupId>com.interest.commonroupId>
        <artifactId>verify-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```
###
2.通过设置Interceptor将ParameterInterceptor加入进去
例如:
```
    <mvc:interceptor>
    <mvc:mapping path="/**"/>
    <bean class="com.xxx.interceptor.ParameterInterceptor"></bean>
    </mvc:interceptor>
```
或者
```

    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new ParameterInterceptor())
                    .addPathPatterns("/**");  // 指定拦截的路径模式，这里拦截所有路径
                    
        }
    }

```

###
3.通过注解建立校验对象(
默认可以使用 DefaultVerifySign对象
例如:
```
    @Bean
    private IVerifySign getVerifySign(){
        return new DefaultVerifySign();
    )
```
或者自定义校验方式:
```
    public class MyVerifySign implements IVerifySign {
        @override
        <T> T getBlackListBean(T cls){
            return null;
        }
        public boolean shouldFilter(HttpServletRequest request){
            return false;
        }
        public boolean verifySign(HttpServletRequest request, HttpServletResponse response){
            return false;
        }
    }
```
4.配置参数说明
```
    uri:
      - /xxxapi/**  需要拦截的校验的请求接口地址,支持通配符
    contenttype:
      - application/x-www-form-urlencoded //需要拦截的请求类型
      - application/json
      - application/json; charset=UTF-8
    timeout: 30s //请求地址超时验证
    whiteuri:
      - /xxxapi/** //白名单,支持部分接口地址不进行校验直接通过
    encodeSalt: 803f243e21aa4bf4a139046c0141bcf8 //对参数进行md5加解密的盐值
    emptyValueJoinVerify: true //对空值是否纳入验签
```
###

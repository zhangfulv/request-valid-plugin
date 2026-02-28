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
###

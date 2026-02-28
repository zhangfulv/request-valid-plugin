package com.interest.util;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Console;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName YamlUtil yml读取信息工具类
 * @DESCRIPTION TODO
 * @Author Mr.zf, link:282734967@qq.com
 * @Date 2021/1/26 10:48
 * 依赖:
 * <dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-yaml</artifactId>
    <version>2.12.1</version>
    </dependency>
 */
public class YamlUtil {
     /**
      * @Description   读取yaml文件信息
      *                 * yaml文件参数和对象的值必须一一对应
      * @param  classPropertiesFilePath resources下的文件路径
      * @param cls 需要返回的数据类型
      * @return
      * @Author Mr.zf.link:282734967@qq.com
      * @Date 2021-01-26 11:08
      * @Update 如果linux开始有/ 会自动去掉，所以需要多加一个斜线/
      **/
    public static<T> T readYaml(String classPropertiesFilePath,Class<T> cls){
        try {
            ObjectMapper  mapper = new ObjectMapper(new YAMLFactory());
            ClassPathResource resource = null;
            String path = "";
            try {
                resource = new ClassPathResource(classPropertiesFilePath);
                path =resource.getUrl().getPath();
                path= propertiesBuilder(path);
            } catch (Exception e) {
                e.printStackTrace();
                path = classPropertiesFilePath;
            }
            T u2 = mapper.readValue(new File(path), cls);
            return u2;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
     /**
      * @Description 初始化属性地址，如果是linux操作系统，将开始的/多写一个，因为hutool会将最前面的/消除
      * @return
      * @Author Mr.zf.link:282734967@qq.com
      * @Date 2021-02-02 10:18
      * @Update
      **/
     private final static String _OS_LINUX = "linux";
    private static String propertiesBuilder(String path){
        //获得系统属性集
        Properties props=System.getProperties();
        //操作系统名称
        String osName = props.getProperty("os.name");
        Console.log("osName ->{}",osName);
        if(osName.toLowerCase().indexOf(_OS_LINUX) >= 0 )
        {
            path = String.format("/%s",path);
        }
        Console.log("path ->{}",path);
        return path;
    }
    public static void main(String[] args) {
     /*   DefaultBlackListConfig blackListConfig = YamlUtil.readYaml("application-blacklist.yml", DefaultBlackListConfig.class);
        blackListConfig.getUri().stream().forEach(e->{
            System.out.println("getUri = [" + e + "]");
        });
        blackListConfig.getContenttype().stream().forEach(e->{
            System.out.println("getContenttype = [" + e + "]");
        });
        System.out.println(blackListConfig.getTimeout());*/
      /*  Map map = YamlUtil.readYaml("application-blacklist.yml", Map.class);
        DefaultVerifySign defaultVerifySign = new DefaultVerifySign();
        VerifyConstants.VALID_CONFIG = map;
        DefaultBlackListConfig blackListConfig = defaultVerifySign.getBlackListBean(new DefaultBlackListConfig());
        blackListConfig.getUri().stream().forEach(e->{
            System.out.println("getUri = [" + e + "]");
        });
        blackListConfig.getContenttype().stream().forEach(e->{
            System.out.println("getContenttype = [" + e + "]");
        });
        System.out.println(blackListConfig.getTimeout());
        Properties props=System.getProperties(); //获得系统属性集
        String osName = props.getProperty("os.name"); //操作系统名称
        System.out.println("osName = [" + osName + "]");*/

//        ClassPathResource resource = new ClassPathResource("/home/zhangfu/projects/ideaWorkspace/verify2.0/src/main/resources/application-blacklist.yml\n");
        Map<String,Object> map = YamlUtil.readYaml("/home/zhangfu/projects/ideaWorkspace/verify2.0/src/main/resources/application-blacklist.yml",Map.class);
        System.out.println("args = [" + map + "]");

    }
}

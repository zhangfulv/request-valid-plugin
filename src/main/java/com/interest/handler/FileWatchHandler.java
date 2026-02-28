package com.interest.handler;

import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.lang.Console;
import com.interest.util.VerifyConstants;
import com.interest.util.YamlUtil;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Map;

/**
 * @ClassName FileWatchHandler
 * @Author Mr.zf, link:282734967@qq.com
 * @Date 2021/1/15 9:38
 */
public class FileWatchHandler implements Watcher {

    @Override
    public void onCreate(WatchEvent<?> event, Path currentPath) {
        Object obj = event.context();
        Console.log("创建：{}-> {}", currentPath, obj);
    }
    private final String __CLS = "classes";
    @Override
    public void onModify(WatchEvent<?> event, Path currentPath) {
        Object obj = event.context();
        Console.log("修改：{}-> {}", currentPath, obj);
        String str = currentPath.toString();
        String format = String.format("%s/%s",
                __CLS.equals(str.substring(str.lastIndexOf("\\") + 1)) ? "": str.substring(str.lastIndexOf("\\") + 1) ,
                obj);
        Console.log("format：{}", format);
        setValue(format);
    }
    private void setValue(String fileRelativePath){
        try {
            //这里需要相对路径
            VerifyConstants.VALID_CONFIG = YamlUtil.readYaml(fileRelativePath, Map.class);
            Console.log("################验签属性更新##################");
            for(Map.Entry<String,Object> entry : VerifyConstants.VALID_CONFIG.entrySet()){
                Console.log("文件更新：key->{}, val ->{}", entry.getKey(),entry.getValue());
            }
            Console.log("################END 验签属性更新 END##################");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDelete(WatchEvent<?> event, Path currentPath) {
        Object obj = event.context();
        Console.log("删除：{}-> {}", currentPath, obj);
    }

    @Override
    public void onOverflow(WatchEvent<?> event, Path currentPath) {
        Object obj = event.context();
        Console.log("Overflow：{}-> {}", currentPath, obj);
    }
    public void setDefaultVal(String fileRelativePath){
        this.setValue(fileRelativePath);
    }

    public static void main(String[] args) {
        String str = "D:/apache-tomcat-9.0.41/webapps/ms-webapp/WEB-INF/classes/conf";
        System.out.println(str.substring(str.lastIndexOf("/") + 1));
    }
}

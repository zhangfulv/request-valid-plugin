package com.interest.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.watch.WatchMonitor;
import com.interest.handler.FileWatchHandler;

import java.io.File;

/**
 * @ClassName FileWatchUtil
 * @DESCRIPTION TODO
 * @Author Mr.zf, link:282734967@qq.com
 * @Date 2021/1/18 18:24
 */
public class FileWatchUtil {
    private String fileRelativePath;

    private FileWatchUtil(String path){
        fileRelativePath = path;
    }
    private volatile static FileWatchUtil _INSTANCE = null;
    public static FileWatchUtil getInstance(String path){
        FileWatchUtil ins = _INSTANCE;
        if(ins == null){
            synchronized (FileWatchUtil.class){
                if(ins == null){
                    ins =_INSTANCE = new FileWatchUtil(path);
                }

            }
        }
        return ins;
    }

    public void watch(){
        try {
            FileWatchHandler fileWatchHandler = new FileWatchHandler();
            ClassPathResource resource = null;
            File file = null;
            try {
                resource = new ClassPathResource(this.fileRelativePath);
                file = FileUtil.file(resource.getUrl().getPath());
            } catch (Exception e) {
                e.printStackTrace();
                //如果想对路径获取失败，则使用绝对路径。。2021-02-23 zf
                file = FileUtil.file(this.fileRelativePath);
                System.out.println("absolute path : "+ this.fileRelativePath);
            }
            //这里只监听文件或目录的修改事件
            WatchMonitor watchMonitor = WatchMonitor.create(file, WatchMonitor.ENTRY_MODIFY,WatchMonitor.ENTRY_CREATE);
            watchMonitor.setWatcher(fileWatchHandler);
            //设置监听目录的最大深入，目录层级大于制定层级的变更将不被监听，默认只监听当前层级目录
            watchMonitor.setMaxDepth(3);
            //启动监听
            watchMonitor.start();
            //设置一个默认值
            fileWatchHandler.setDefaultVal(this.fileRelativePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

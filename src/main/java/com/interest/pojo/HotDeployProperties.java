package com.interest.pojo;

import com.interest.util.FileWatchUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName HotDeployProperties
 * @DESCRIPTION TODO 热加载监听配置文件
 * @Author Mr.zf, link:282734967@qq.com
 * @Date 2021/1/28 16:42
 */
public class HotDeployProperties {
    private String resourceFilePath;
    public HotDeployProperties(){

    }

    public String getResourceFilePath() {
        return resourceFilePath;
    }

    public void setResourceFilePath(String resourceFilePath) {
        this.resourceFilePath = resourceFilePath;
        watch();
    }
    public void watch(){
        if(StringUtils.isEmpty(this.getResourceFilePath())){
            FileWatchUtil.getInstance("/config/application-blacklist.yml").watch();
        }else {
            FileWatchUtil.getInstance(this.getResourceFilePath()).watch();
        }
    }
}

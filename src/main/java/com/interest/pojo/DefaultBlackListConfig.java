package com.interest.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DefaultBlackListConfig
 * @DESCRIPTION TODO
 * @Author xulian
 * @Date 2021/1/25 17:24
 * @Component
 * @RefreshScope
 * @ConfigurationProperties(prefix = "blacklist")
 */
public class DefaultBlackListConfig {
    private List<String> uri = new ArrayList<>();
    private List<String> contenttype = new ArrayList<>();
    private String timeout = "";

    public List<String> getUri() {
        return uri;
    }

    public void setUri(List<String> uri) {
        this.uri = uri;
    }

    public List<String> getContenttype() {
        return contenttype;
    }

    public void setContenttype(List<String> contenttype) {
        this.contenttype = contenttype;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}

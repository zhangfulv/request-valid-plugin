//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.interest.pojo;

import java.util.ArrayList;
import java.util.List;

public class DefaultBlackListConfig {
    private List<String> uri = new ArrayList();
    private List<String> whiteuri = new ArrayList();//白名单请求url
    private List<String> whiteQueryString = new ArrayList();//白名单请求get参数
    private List<String> contenttype = new ArrayList();
    private String timeout = "30s";
    private String emptyValueJoinVerify = "true";
    private String encodeSalt = "803f243e21aa4bf4a139046c0141bcf8";

    public DefaultBlackListConfig() {
    }

    public List<String> getUri() {
        return this.uri;
    }

    public void setUri(List<String> uri) {
        this.uri = uri;
    }

    public List<String> getContenttype() {
        return this.contenttype;
    }

    public void setContenttype(List<String> contenttype) {
        this.contenttype = contenttype;
    }

    public String getTimeout() {
        return this.timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getEmptyValueJoinVerify() {
        return this.emptyValueJoinVerify;
    }

    public void setEmptyValueJoinVerify(String emptyValueJoinVerify) {
        this.emptyValueJoinVerify = emptyValueJoinVerify;
    }

    public String getEncodeSalt() {
        return this.encodeSalt;
    }

    public void setEncodeSalt(String encodeSalt) {
        this.encodeSalt = encodeSalt;
    }

    public List<String> getWhiteuri() {
        return this.whiteuri;
    }

    public void setWhiteuri(List<String> whiteuri) {
        this.whiteuri = whiteuri;
    }


    public List<String> getWhiteQueryString() {
        return whiteQueryString;
    }

    public void setWhiteQueryString(List<String> whiteQueryString) {
        this.whiteQueryString = whiteQueryString;
    }
}

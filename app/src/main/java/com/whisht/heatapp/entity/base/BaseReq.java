package com.whisht.heatapp.entity.base;


import com.whisht.heatapp.constant.NetConstant;

import java.io.Serializable;

public class BaseReq implements Serializable {
    private String devId = "";
    private String sessionId = NetConstant.getSessionId();
    private String className;
    public BaseReq(){
        this.className = this.getClass().getSimpleName();
    }
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}

package com.whisht.heatapp.entity;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description:(这里用一句话描述这个类的作用)
 * @author: LQY
 * @time: 2020-02-22 15:33
 */
public class LoginInfo implements Serializable {
    private String sessionId;
    private String userId;
    //登录名称
    private String loginName;
    //用户名称
    private String userName;
    //单位id
    private String unitId;
    //单位级别
    private String unitLevel;
    //单位名称
    private String unit;
    private String mapLevel;
    private String mapCenter;

    public String getUnitLevel() {
        return unitLevel;
    }

    public void setUnitLevel(String unitLevel) {
        this.unitLevel = unitLevel;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMapLevel() {
        return mapLevel;
    }

    public void setMapLevel(String mapLevel) {
        this.mapLevel = mapLevel;
    }

    public String getMapCenter() {
        return mapCenter;
    }

    public void setMapCenter(String mapCenter) {
        this.mapCenter = mapCenter;
    }
}

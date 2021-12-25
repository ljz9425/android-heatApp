package com.whisht.heatapp.entity.http.resp;



import com.whisht.heatapp.entity.base.BaseResp;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台根据请求参数的手机号或者uuid判断之前是否已经发送过
 */
public class LoginResp extends BaseResp {
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
    //部门id
    private String departmentId;
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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
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

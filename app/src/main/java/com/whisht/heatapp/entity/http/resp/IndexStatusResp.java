package com.whisht.heatapp.entity.http.resp;

import com.whisht.heatapp.entity.base.BaseResp;

public class IndexStatusResp extends BaseResp {
    private String area;
    private String host;
    private String count;
    private String openCount;
    private String closeCount;
    private String offlineCount;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOpenCount() {
        return openCount;
    }

    public void setOpenCount(String openCount) {
        this.openCount = openCount;
    }

    public String getCloseCount() {
        return closeCount;
    }

    public void setCloseCount(String closeCount) {
        this.closeCount = closeCount;
    }

    public String getOfflineCount() {
        return offlineCount;
    }

    public void setOfflineCount(String offlineCount) {
        this.offlineCount = offlineCount;
    }
}

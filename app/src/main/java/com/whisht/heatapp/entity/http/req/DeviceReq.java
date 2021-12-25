package com.whisht.heatapp.entity.http.req;

import com.whisht.heatapp.entity.base.BaseReq;

public class DeviceReq extends BaseReq {
    private int pageIndex;
    private int pageSize;
    private String unitCode;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}

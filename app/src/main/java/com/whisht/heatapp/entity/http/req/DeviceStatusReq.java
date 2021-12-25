package com.whisht.heatapp.entity.http.req;

import com.whisht.heatapp.entity.base.BaseReq;

public class DeviceStatusReq extends BaseReq {
    private String unitCode;
    private String setTemp;

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getSetTemp() {
        return setTemp;
    }

    public void setSetTemp(String setTemp) {
        this.setTemp = setTemp;
    }

    public DeviceStatusReq(String unitCode) {
        this.unitCode = unitCode;
    }
}

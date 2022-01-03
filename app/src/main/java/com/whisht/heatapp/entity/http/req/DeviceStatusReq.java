package com.whisht.heatapp.entity.http.req;

import com.whisht.heatapp.entity.base.BaseReq;

public class DeviceStatusReq extends BaseReq {
    private String roomId;
    private String roomName;
    private String unitCode;
    private String setTemp;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

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

package com.whisht.heatapp.entity.http.req;

import com.whisht.heatapp.entity.base.BaseReq;

public class DeviceConfigReq extends BaseReq {
    private String id;
    private String unitCode;
    private String unitName;
    private byte ctrlType;
    private byte saturdayCtrl;
    private byte sundayCtrl;
    private byte backTemp = 0;
    private String openCloseTimingStart;
    private String openCloseTimingStop;
    private byte backTemp2 = 0;
    private String openCloseTimingStart2;
    private String openCloseTimingStop2;
    private byte backTemp3 = 0;
    private String openCloseTimingStart3;
    private String openCloseTimingStop3;
    private byte roomTemp = 0;
    private String startDate;
    private String stopDate;
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public byte getCtrlType() {
        return ctrlType;
    }

    public void setCtrlType(byte ctrlType) {
        this.ctrlType = ctrlType;
    }

    public byte getSaturdayCtrl() {
        return saturdayCtrl;
    }

    public void setSaturdayCtrl(byte saturdayCtrl) {
        this.saturdayCtrl = saturdayCtrl;
    }

    public byte getSundayCtrl() {
        return sundayCtrl;
    }

    public void setSundayCtrl(byte sundayCtrl) {
        this.sundayCtrl = sundayCtrl;
    }

    public byte getBackTemp() {
        return backTemp;
    }

    public void setBackTemp(byte backTemp) {
        this.backTemp = backTemp;
    }

    public String getOpenCloseTimingStart() {
        return openCloseTimingStart;
    }

    public void setOpenCloseTimingStart(String openCloseTimingStart) {
        this.openCloseTimingStart = openCloseTimingStart;
    }

    public String getOpenCloseTimingStop() {
        return openCloseTimingStop;
    }

    public void setOpenCloseTimingStop(String openCloseTimingStop) {
        this.openCloseTimingStop = openCloseTimingStop;
    }

    public byte getBackTemp2() {
        return backTemp2;
    }

    public void setBackTemp2(byte backTemp2) {
        this.backTemp2 = backTemp2;
    }

    public String getOpenCloseTimingStart2() {
        return openCloseTimingStart2;
    }

    public void setOpenCloseTimingStart2(String openCloseTimingStart2) {
        this.openCloseTimingStart2 = openCloseTimingStart2;
    }

    public String getOpenCloseTimingStop2() {
        return openCloseTimingStop2;
    }

    public void setOpenCloseTimingStop2(String openCloseTimingStop2) {
        this.openCloseTimingStop2 = openCloseTimingStop2;
    }

    public byte getBackTemp3() {
        return backTemp3;
    }

    public void setBackTemp3(byte backTemp3) {
        this.backTemp3 = backTemp3;
    }

    public String getOpenCloseTimingStart3() {
        return openCloseTimingStart3;
    }

    public void setOpenCloseTimingStart3(String openCloseTimingStart3) {
        this.openCloseTimingStart3 = openCloseTimingStart3;
    }

    public String getOpenCloseTimingStop3() {
        return openCloseTimingStop3;
    }

    public void setOpenCloseTimingStop3(String openCloseTimingStop3) {
        this.openCloseTimingStop3 = openCloseTimingStop3;
    }

    public byte getRoomTemp() {
        return roomTemp;
    }

    public void setRoomTemp(byte roomTemp) {
        this.roomTemp = roomTemp;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}

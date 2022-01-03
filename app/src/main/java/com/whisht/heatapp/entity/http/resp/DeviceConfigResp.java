package com.whisht.heatapp.entity.http.resp;

import com.whisht.heatapp.entity.base.BaseResp;

public class DeviceConfigResp extends BaseResp {
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
    private byte roomTempOpen = 0;
    private byte roomTempClose = 0;
    private String startDate;
    private String stopDate;
    private String updateTime;

    private String startMon = "11";
    private String startDay = "1";
    private String stopMon = "3";
    private String stopDay = "31";

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

    public byte getRoomTempOpen() {
        return roomTempOpen;
    }

    public void setRoomTempOpen(byte roomTempOpen) {
        this.roomTempOpen = roomTempOpen;
    }

    public byte getRoomTempClose() {
        return roomTempClose;
    }

    public void setRoomTempClose(byte roomTempClose) {
        this.roomTempClose = roomTempClose;
    }

    public String getStartMon() {
        return startMon;
    }

    public void setStartMon(String startMon) {
        this.startMon = startMon;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getStopMon() {
        return stopMon;
    }

    public void setStopMon(String stopMon) {
        this.stopMon = stopMon;
    }

    public String getStopDay() {
        return stopDay;
    }

    public void setStopDay(String stopDay) {
        this.stopDay = stopDay;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        if (startDate.indexOf("月") > 0) {
            this.startMon = this.startDate.substring(0, startDate.indexOf("月"));
            if (startDate.indexOf("日") > 0) {
                this.startDay = this.startDate.substring(startDate.indexOf("月") + 1, startDate.indexOf("日"));
            }
        }
    }

    public String getStopDate() {
        return this.stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
        if (stopDate.indexOf("月") > 0) {
            this.stopMon = this.stopDate.substring(0, stopDate.indexOf("月"));
            if (stopDate.indexOf("日") > 0) {
                this.stopDay = this.stopDate.substring(stopDate.indexOf("月") + 1, stopDate.indexOf("日"));
            }
        }
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}

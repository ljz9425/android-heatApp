package com.whisht.heatapp.entity;

import java.io.Serializable;

public class StatDayInfo implements Serializable {
    private String unitCode;
    private String unitName;
    private String date;
    private String openTime;
    private String openDesc;
    private String startPower;
    private String stopPower;
    private String totalPower;
    private String area;
    private String weather;
    private String consume;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getOpenDesc() {
        return openDesc;
    }

    public void setOpenDesc(String openDesc) {
        this.openDesc = openDesc;
    }

    public String getStartPower() {
        return startPower;
    }

    public void setStartPower(String startPower) {
        this.startPower = startPower;
    }

    public String getStopPower() {
        return stopPower;
    }

    public void setStopPower(String stopPower) {
        this.stopPower = stopPower;
    }

    public String getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(String totalPower) {
        this.totalPower = totalPower;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }
}

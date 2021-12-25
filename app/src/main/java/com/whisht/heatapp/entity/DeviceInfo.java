package com.whisht.heatapp.entity;

import java.io.Serializable;

public class DeviceInfo implements Serializable {
    private String unitCode;
    private String unitName;
    private String hostTypeName;
    private String weatherTemp;
    private String weatherIcon;
    private String weather;
    private String statusDesc;
    private String setTemp;
    private String sendTemp;
    private String backTemp;
    private String evnTemp;
    private String elecA;
    private String elecB;
    private String elecC;
    private String voltA;
    private String voltB;
    private String voltC;
    private String totalPower;
    private String fault;
    private String macLastTime;
    private String lon;
    private String lat;

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

    public String getHostTypeName() {
        return hostTypeName;
    }

    public void setHostTypeName(String hostTypeName) {
        this.hostTypeName = hostTypeName;
    }

    public String getWeatherTemp() {
        return weatherTemp;
    }

    public void setWeatherTemp(String weatherTemp) {
        this.weatherTemp = weatherTemp;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getSetTemp() {
        return setTemp;
    }

    public void setSetTemp(String setTemp) {
        this.setTemp = setTemp;
    }

    public String getSendTemp() {
        return sendTemp;
    }

    public void setSendTemp(String sendTemp) {
        this.sendTemp = sendTemp;
    }

    public String getBackTemp() {
        return backTemp;
    }

    public void setBackTemp(String backTemp) {
        this.backTemp = backTemp;
    }

    public String getEvnTemp() {
        return evnTemp;
    }

    public void setEvnTemp(String evnTemp) {
        this.evnTemp = evnTemp;
    }

    public String getElecA() {
        return elecA;
    }

    public void setElecA(String elecA) {
        this.elecA = elecA;
    }

    public String getElecB() {
        return elecB;
    }

    public void setElecB(String elecB) {
        this.elecB = elecB;
    }

    public String getElecC() {
        return elecC;
    }

    public void setElecC(String elecC) {
        this.elecC = elecC;
    }

    public String getVoltA() {
        return voltA;
    }

    public void setVoltA(String voltA) {
        this.voltA = voltA;
    }

    public String getVoltB() {
        return voltB;
    }

    public void setVoltB(String voltB) {
        this.voltB = voltB;
    }

    public String getVoltC() {
        return voltC;
    }

    public void setVoltC(String voltC) {
        this.voltC = voltC;
    }

    public String getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(String totalPower) {
        this.totalPower = totalPower;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public String getMacLastTime() {
        return macLastTime;
    }

    public void setMacLastTime(String macLastTime) {
        this.macLastTime = macLastTime;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}

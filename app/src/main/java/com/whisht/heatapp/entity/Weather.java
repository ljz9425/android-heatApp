package com.whisht.heatapp.entity;

import java.io.Serializable;

/**
 * 	天气
* @Description: TODO
* @author LY
* @date 2019年5月12日 下午5:27:32
 */
public class Weather implements Serializable {
	private String weatherId;
	private String date;//	预报日期	2013-12-30
	private String tmpMax;//	最高温度	4
	private String tmpMin;//	最低温度	-5
	private Integer dayWeatherCode;
	private String dayWeather;
	private Integer nightWeatherCode;
	private String nightWeather;
	private String address;
	private String addressCode;
	private String windDir;//风向
	private String windSc;//风级

	private Integer condCode;//	天气状况代码	100
	private String condTxt;//	天气状况描述	晴

	public String getWeatherId() {
		return weatherId;
	}
	public void setWeatherId(String weatherId) {
		this.weatherId = weatherId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTmpMax() {
		return tmpMax;
	}
	public void setTmpMax(String tmpMax) {
		this.tmpMax = tmpMax;
	}
	public String getTmpMin() {
		return tmpMin;
	}
	public void setTmpMin(String tmpMin) {
		this.tmpMin = tmpMin;
	}
	public Integer getDayWeatherCode() {
		return dayWeatherCode;
	}
	public void setDayWeatherCode(Integer dayWeatherCode) {
		this.dayWeatherCode = dayWeatherCode;
	}
	public String getDayWeather() {
		return dayWeather;
	}
	public void setDayWeather(String dayWeather) {
		this.dayWeather = dayWeather;
	}
	public Integer getNightWeatherCode() {
		return nightWeatherCode;
	}
	public void setNightWeatherCode(Integer nightWeatherCode) {
		this.nightWeatherCode = nightWeatherCode;
	}
	public String getNightWeather() {
		return nightWeather;
	}
	public void setNightWeather(String nightWeather) {
		this.nightWeather = nightWeather;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressCode() {
		return addressCode;
	}
	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}
	public String getWindDir() {
		return windDir;
	}
	public void setWindDir(String windDir) {
		this.windDir = windDir;
	}
	public String getWindSc() {
		return windSc;
	}
	public void setWindSc(String windSc) {
		this.windSc = windSc;
	}
	public Integer getCondCode() {
		return condCode;
	}
	public void setCondCode(Integer condCode) {
		this.condCode = condCode;
	}
	public String getCondTxt() {
		return condTxt;
	}
	public void setCondTxt(String condTxt) {
		this.condTxt = condTxt;
	}

}

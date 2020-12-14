package com.example.playerxui.bean;

import java.io.Serializable;

/**
 * Created by dang on 2020-12-11.
 * Time will tell.
 * 此类为获取到的城市信息及天气
 * @description
 */
public class LocaAndWeather implements Serializable {
    private String addrStr;
    private String city;
    private String cityId;
    private String weather;
    private String temp;

    public String getAddrStr() {
        return addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}

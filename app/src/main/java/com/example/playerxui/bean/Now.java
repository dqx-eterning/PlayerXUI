package com.example.playerxui.bean;

import java.io.Serializable;

/**
 * Created by dang on 2020-12-11.
 * Time will tell.
 * 该类为返回天气实体类
 * @description
 */
public class Now implements Serializable {

    private String cloud;
    private String dew;
    private String feelsLike;//体感温度
    private String humidity;//相对湿度
    private String icon;
    private String obsTime;
    private String precip;
    private String pressure;
    private String temp;//温度
    private String text;//天气
    private String vis;//能见度
    private String wind360;
    private String windDir;
    private String windScale;
    private String windSpeed;
    public void setCloud(String cloud) {
        this.cloud = cloud;
    }
    public String getCloud() {
        return cloud;
    }

    public void setDew(String dew) {
        this.dew = dew;
    }
    public String getDew() {
        return dew;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }
    public String getFeelsLike() {
        return feelsLike;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
    public String getHumidity() {
        return humidity;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getIcon() {
        return icon;
    }

    public void setObsTime(String obsTime) {
        this.obsTime = obsTime;
    }
    public String getObsTime() {
        return obsTime;
    }

    public void setPrecip(String precip) {
        this.precip = precip;
    }
    public String getPrecip() {
        return precip;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
    public String getPressure() {
        return pressure;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
    public String getTemp() {
        return temp;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }
    public String getVis() {
        return vis;
    }

    public void setWind360(String wind360) {
        this.wind360 = wind360;
    }
    public String getWind360() {
        return wind360;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }
    public String getWindDir() {
        return windDir;
    }

    public void setWindScale(String windScale) {
        this.windScale = windScale;
    }
    public String getWindScale() {
        return windScale;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
    public String getWindSpeed() {
        return windSpeed;
    }

}
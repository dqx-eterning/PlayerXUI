package com.example.playerxui.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Created by dang on 2020-12-11.
 * Time will tell.
 * 城市名称与城市吗对应类
 * @description
 */
/*
* 101120801西安:101110101延安:101110300榆林:101110401铜川:101111001商洛:101110601安康:101110701汉中:101110801宝鸡:101110901咸阳:101110200渭南*/
public class City extends LitePalSupport {
    private int id;
    private String cityName;
    private String cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}

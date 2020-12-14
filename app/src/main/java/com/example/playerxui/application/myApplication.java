package com.example.playerxui.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.qweather.sdk.view.HeConfig;
import com.xuexiang.xui.XUI;

import org.litepal.LitePal;


/**
 * Created by dang on 2020-12-07.
 * Time will tell.
 *
 * @description
 */
public class myApplication extends Application {

    private static Context mContext;
    //onCreate--->程序创建的时候执行
    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);

        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.GCJ02);

        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
        mContext = getApplicationContext();
        //Log.e("MyApplication", "MyApplication----onCreate()方法！！！！！！！！！！！！");

        HeConfig.init("HE2012111424501366", "1e0e30d5ae854ff1a0dd5a133e70efa3");

        //切换至开发版服务
        HeConfig.switchToDevService();

        //引入multidex
        MultiDex.install(this);

        //引入litepal
        LitePal.initialize(this);
    }
}

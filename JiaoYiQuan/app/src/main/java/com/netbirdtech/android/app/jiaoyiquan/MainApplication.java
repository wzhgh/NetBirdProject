package com.netbirdtech.android.app.jiaoyiquan;

import android.app.Application;

import com.rey.material.app.ThemeManager;

/**
 * Created by Administrator on 2016/3/4 0004.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化rey的material的主题样式
        ThemeManager.init(this, 2, 0, null);
    }
}

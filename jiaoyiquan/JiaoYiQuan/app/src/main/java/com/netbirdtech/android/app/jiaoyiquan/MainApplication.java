package com.netbirdtech.android.app.jiaoyiquan;

import android.app.Application;
import android.content.Context;

import com.netbirdtech.android.app.jiaoyiquan.utils.PreferenceMgr;
import com.netbirdtech.android.app.jiaoyiquan.utils.VolleyUtil;
import com.rey.material.app.ThemeManager;

/**
 * Created by Administrator on 2016/3/4 0004.
 */
public class MainApplication extends Application {
    private Context mContext ;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext() ;
        //初始化rey的material的主题样式
        ThemeManager.init(this, 2, 0, null);
        VolleyUtil.initialize(mContext);
        PreferenceMgr.initialize(mContext);
    }
}

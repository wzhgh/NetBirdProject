package com.netbirdtech.android.app.jiaoyiquan.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/3/11 0011.
 */
public class PreferenceMgr {

    private static Context mContext ;
    private static SharedPreferences userInfoPre = null;

    public static synchronized void initialize(Context context) {
        if (mContext == null) {
            synchronized (PreferenceMgr.class) {
                mContext = context ;
                init();
            }
        }
    }

    private static void init(){
        if(userInfoPre == null){
            userInfoPre = mContext.getSharedPreferences("UserInfoPre", mContext.MODE_PRIVATE) ;
        }
    }

    public static void addUserInfoToPre(int uid,String userName){
        checkUserInfoPre();
        SharedPreferences.Editor spfEditor = userInfoPre.edit() ;
        spfEditor.clear() ;
        spfEditor.putInt("uid",uid) ;
        spfEditor.putString("username", userName) ;
        spfEditor.putBoolean("loginFlag",true) ;
        spfEditor.commit();
    }

    /**
     * 清空用户信息
     */
    public static void clearUserInfo(){
        checkUserInfoPre();
        SharedPreferences.Editor spfEditor = userInfoPre.edit() ;
        spfEditor.clear() ;
        spfEditor.putBoolean("loginFlag", false) ;
        spfEditor.commit() ;
    }

    /**
     * 判断用户是否登录
     */
    public static Boolean isLogin(){
        checkUserInfoPre();
       return  userInfoPre.getBoolean("loginFlag",false) ;
    }

    public static String getUserName(){
        checkUserInfoPre();
        return userInfoPre.getString("username", "") ;
    }

    public static int getUid(){
        checkUserInfoPre();
        return userInfoPre.getInt("uid", -1) ;
    }

    private static void checkUserInfoPre(){
        if(userInfoPre == null){
            throw new RuntimeException("请先初始化PreferenceMgr,call initialize()");
        }
    }
}

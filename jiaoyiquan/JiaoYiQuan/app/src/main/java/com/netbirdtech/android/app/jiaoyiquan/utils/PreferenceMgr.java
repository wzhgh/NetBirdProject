package com.netbirdtech.android.app.jiaoyiquan.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/3/11 0011.
 */
public class PreferenceMgr {

    private Context mContext ;
    private SharedPreferences userInfoPre = null;
    private static PreferenceMgr mInstance ;

    private  PreferenceMgr(Context context) {
        mContext = context ;
        init();
    }

    public static PreferenceMgr getInstance(Context context){
        if(mInstance == null){
            mInstance = new PreferenceMgr(context) ;
        }
        return mInstance ;
    }

    private void init(){
        if(userInfoPre == null){
            userInfoPre = mContext.getSharedPreferences("UserInfoPre", mContext.MODE_PRIVATE) ;
        }
    }

    public void addUserInfoToPre(int uid,String userName){
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
    public void clearUserInfo(){
        SharedPreferences.Editor spfEditor = userInfoPre.edit() ;
        spfEditor.clear() ;
        spfEditor.putBoolean("loginFlag", false) ;
        spfEditor.commit() ;
    }

    /**
     * 判断用户是否登录
     */
    public Boolean isLogin(){
       return  userInfoPre.getBoolean("loginFlag",false) ;
    }

    public String getUserName(){
        return userInfoPre.getString("username", "") ;
    }

    public int getUid(){
        return userInfoPre.getInt("uid",-1) ;
    }
}

package com.netbirdtech.android.app.jiaoyiquan.entity;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/7 0007.
 */
public class Talk {
    //消息id
    private int mId ;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    private int userId ;
    private String userName ;
    private String createdData ;
    private String content ;
    private List<String> imageUrlList ;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreatedData() {
        return createdData;
    }

    public void setCreatedData(String createData) {
        this.createdData = createData;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

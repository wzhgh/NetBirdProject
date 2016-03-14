package com.netbirdtech.android.app.jiaoyiquan.entity;

/**
 * Created by wzh on 2016/3/12.
 */
public interface Constant {

    /**
     * 内网服务器地址
     */
    public static final String TEST_HOST = "http://192.168.1.253" ;

    /**
     * 外网服务器地址
     */
    public static final String RELEASE_HOST = "http://42.51.23.85:8081" ;

    /**
     * 上传图片的host地址
     */
    public static final String UPLOAD_IMAGE_URL = TEST_HOST + "/upload/file" ;

    /**
     * 上传图片的key值
     */
    public static final String UPLOAD_IMAGE_PARAM = "upfile" ;

    /**
     * 发表说说
     */
    public static final String PUBLISH_CONTENT = TEST_HOST + "/message/post" ;

}

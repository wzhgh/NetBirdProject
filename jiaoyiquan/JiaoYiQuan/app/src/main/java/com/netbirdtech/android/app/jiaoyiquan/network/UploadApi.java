package com.netbirdtech.android.app.jiaoyiquan.network;

import com.android.volley.Request;
import com.netbirdtech.android.app.jiaoyiquan.entity.Constant;
import com.netbirdtech.android.app.jiaoyiquan.form.FormImage;
import com.netbirdtech.android.app.jiaoyiquan.utils.VolleySington;
import com.netbirdtech.android.app.jiaoyiquan.utils.VolleyUtil;

import java.util.List;

/**
 * Created by wzh on 2016/3/12.
 */
public class UploadApi {

    /**
     * 一次上传一张图片
     */
    public static void uploadSingleImage(FormImage formImage,ResponseListener listener){
        Request request = new PostUploadRequestSingle(Constant.UPLOAD_IMAGE_URL,formImage,listener) ;
        VolleyUtil.getRequestQueue().add(request) ;
    }

    /**
     * 同时上传多张图片
     */
    public static void uploadMultipleImage(List<FormImage> imageList,ResponseListener listener){
        Request request = new PostUploadRequest(Constant.UPLOAD_IMAGE_URL,imageList,listener) ;
        VolleyUtil.getRequestQueue().add(request) ;
    }
}

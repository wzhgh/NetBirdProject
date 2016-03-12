package com.netbirdtech.android.app.jiaoyiquan.form;

/**
 * Created by Administrator on 2016/3/11 0011.
 */
public class FormImage {
    //参数名称
    private String paramName ;
    //image图片名称(含后缀名)
    private String fileName ;
    //图片本地路径
    private String imageUrl ;
    //图片的格式
    private String mime ;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    //得到图片的二进制数组
    public byte[] getImageBytes(){
        return null ;
    }
}

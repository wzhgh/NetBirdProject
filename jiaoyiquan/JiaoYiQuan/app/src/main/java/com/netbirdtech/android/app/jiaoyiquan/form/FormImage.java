package com.netbirdtech.android.app.jiaoyiquan.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
    //图片的mime类型，默认为"image/*" 任何格式图片都可以，然后服务端来判断哪些合不合法
    private String mime = "image/*" ;

    public FormImage(String imageUrl,String paramName) {
        this.imageUrl = imageUrl;
        this.paramName = paramName ;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getFileName() {
        File imgFile = new File(imageUrl) ;
        if(imgFile.exists() && imgFile.isFile()){
            fileName = imgFile.getName() ;
        }else{
            fileName = "" ;
        }
        return fileName;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public String getMime() {
        return mime;
    }

    //得到图片的二进制数组
    public byte[] getImageBytes(){
        File imgFile = new File(imageUrl) ;
        if(!imgFile.exists() || imgFile.isDirectory()){
            return null ;
        }
        byte[] result = null ;
        FileInputStream fis = null ;
        ByteArrayOutputStream baos = null ;
        try {
            fis = new FileInputStream(imgFile) ;
            byte[] buffer = new byte[1024];
            //直接用ByteArrayOutputStream 来讲字节数组数据全部写入到这个流里，然后在baos.toByteArray() 来获取全部的字节数组数据
            baos = new ByteArrayOutputStream() ;
            int count = 0 ;
            while ((count = fis.read(buffer)) != -1) {
                //将缓存的字节数组数据全部写入到ByteArrayOutputStream流中，缓存起来
                baos.write(buffer, 0, count);
            }
            //从ByteArrayOutputStream一次性获取缓存的字节数据
            result = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            result = null ;
        }finally {
            //流的关闭
            try {
                if(fis != null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (baos != null){
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result ;
    }
}

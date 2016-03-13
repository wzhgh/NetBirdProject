package com.netbirdtech.android.app.jiaoyiquan.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.netbirdtech.android.app.jiaoyiquan.form.FormImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
/**
 * Created by wzh on 2016/3/12.
 * 单个文件上传
 */
public class PostUploadRequestSingle extends Request<String>{
        // 超时时间
        private static final int TIME_OUT = 5 * 1000;
        //字符编码
        private static final String CHARSET = "utf-8";
        //正确数据的时候回掉用
        private ResponseListener mListener ;
        //请求数据通过参数的形式传入
        private FormImage mFormImage ;
        //数据分隔线，使用UUID在生成
        private String BOUNDARY = UUID.randomUUID().toString();
        //提交表单的格式
        private String MULTIPART_FORM_DATA = "multipart/form-data";

        public PostUploadRequestSingle(String url, FormImage formImage, ResponseListener listener) {
                super(Method.POST, url, listener);
                this.mListener = listener ;
                setShouldCache(false);
                mFormImage = formImage ;
                setRetryPolicy(new DefaultRetryPolicy
                (TIME_OUT,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        /**
         * 这里开始解析数据
         * @param response Response from the network
         * @return
         */
        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String mString =
                        new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Log.v("wzh", "====mString===" + mString);
                    return Response.success(mString,
                        HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
         }

        /**
         * 回调正确的数据
         * @param response The parsed response returned by
         */
        @Override
        protected void deliverResponse(String response) {
                mListener.onResponse(response);
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
                if (mFormImage == null){
                    return super.getBody() ;
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
                StringBuffer sb= new StringBuffer() ;
                //第一行
                sb.append("--"+BOUNDARY);
                sb.append("\r\n") ;
                    /*第二行*/
                sb.append("Content-Disposition: form-data;");
                sb.append(" name=\"");
                sb.append(mFormImage.getParamName()) ;
                sb.append("\"") ;
                sb.append("; filename=\"") ;
                sb.append(mFormImage.getFileName()) ;
                sb.append("\"");
                sb.append("\r\n") ;
                    /*第三行*/
                sb.append("Content-Type: ");
                sb.append(mFormImage.getMime()) ;
                sb.append("\r\n") ;
                    /*第四行*/
                sb.append("\r\n") ;
                try {
                    bos.write(sb.toString().getBytes("utf-8"));
                    /*第五行*/
                    bos.write(mFormImage.getImageBytes());
                    bos.write("\r\n".getBytes("utf-8"));
                } catch (IOException e) {
                    //这里出异常后，程序还继不继续往下走?     TODO
                    e.printStackTrace();
                }
                /*结尾行*/
                String endLine = "--" + BOUNDARY + "--" + "\r\n" ;
                try {
                    bos.write(endLine.toString().getBytes("utf-8"));
                } catch (IOException e){
                    e.printStackTrace();
                }
                Log.v("wzh","=====formImage====\n"+bos.toString()) ;
                return bos.toByteArray();
        }

        @Override
        public String getBodyContentType() {
                return MULTIPART_FORM_DATA+"; boundary="+BOUNDARY;
        }
}

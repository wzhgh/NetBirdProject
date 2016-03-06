package com.netbirdtech.android.app.jiaoyiquan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.netbirdtech.android.app.jiaoyiquan.R;
import com.netbirdtech.android.app.jiaoyiquan.utils.StringUtils;
import com.netbirdtech.android.app.jiaoyiquan.utils.ViewUtils;
import com.rey.material.widget.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录/注册页面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext ;
    private TextView mBackTV ;
    private TextView mSwitchToRegTV ;
    private TextView mSwitchToLogTV ;
    private View mLoginPart ;
    private View mRegPart ;
    private TextView loginTitle ;
    private Button mLoginBtn ;
    private Button mRegBtn ;

    private EditText mLoginName ;
    private EditText mLoginPw ;
    private EditText mRegName ;
    private EditText mRegPw ;
    private EditText mRegPw2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this ;
        initView() ;
    }

    private void initView(){
        mBackTV = (TextView)findViewById(R.id.backtoup_id) ;

        mSwitchToRegTV = (TextView)findViewById(R.id.switchtoreg_id) ;

        mLoginPart = findViewById(R.id.login_part_id) ;
        mRegPart = findViewById(R.id.register_part_id) ;

        mSwitchToLogTV = (TextView)findViewById(R.id.switchtolog_id) ;

        loginTitle = (TextView)findViewById(R.id.login_title_id) ;

        //登录注册点击
        mLoginBtn = (Button)findViewById(R.id.user_login_id) ;
        mRegBtn = (Button)findViewById(R.id.user_register_id) ;

        mLoginName = (EditText)findViewById(R.id.uname_login_id) ;
        mLoginPw = (EditText)findViewById(R.id.pw_login_id) ;
        mRegName = (EditText)findViewById(R.id.uname_reg_id) ;
        mRegPw = (EditText)findViewById(R.id.pw_reg_id) ;
        mRegPw2 = (EditText)findViewById(R.id.pw2_reg_id) ;

        mBackTV.setOnClickListener(this);
        mSwitchToRegTV.setOnClickListener(this);
        mSwitchToLogTV.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mRegBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backtoup_id :
                LoginActivity.this.finish();
                break ;
            case R.id.switchtoreg_id:
                mLoginPart.setVisibility(View.GONE);
                mRegPart.setVisibility(View.VISIBLE);
                loginTitle.setText("注册");
                break ;
            case R.id.switchtolog_id:
                mLoginPart.setVisibility(View.VISIBLE);
                mRegPart.setVisibility(View.GONE);
                loginTitle.setText("登录");
                break ;
            case R.id.user_login_id:
                //Button的点击效果
                if(view instanceof FloatingActionButton){
                    FloatingActionButton bt = (FloatingActionButton)view;
                    bt.setLineMorphingState((bt.getLineMorphingState() + 1) % 2, true);
                }

                String loginUserName = mLoginName.getText().toString() ;
                String loginPwdVal = mLoginPw.getText().toString() ;
                if(StringUtils.isEmpty(loginUserName)){
                    Toast.makeText(LoginActivity.this,"用户名不能为空！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(StringUtils.isEmpty(loginPwdVal)){
                    Toast.makeText(LoginActivity.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                loginByJsonObjectRequest(loginUserName,loginPwdVal);
                break ;
            case R.id.user_register_id:
                //Button的点击效果
                if(view instanceof FloatingActionButton){
                    FloatingActionButton bt = (FloatingActionButton)view;
                    bt.setLineMorphingState((bt.getLineMorphingState() + 1) % 2, true);
                }

                String regUserName = mRegName.getText().toString() ;
                String regPwd = mRegPw.getText().toString() ;
                String regPwd2 = mRegPw2.getText().toString() ;
                if(StringUtils.isEmpty(regUserName) || StringUtils.isEmpty(regPwd) || StringUtils.isEmpty(regPwd2)){
                    Toast.makeText(LoginActivity.this,"用户名或密码不能为空！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(!regPwd.equals(regPwd2)){
                    Toast.makeText(LoginActivity.this,"2次密码必须相同",Toast.LENGTH_SHORT).show();
                    return ;
                }
                registerByJsonObjectRequest(regUserName,regPwd,regPwd2);
                break  ;
            default:
                break;
        }
    }

    /**
     * 登录请求(StringRequest)
     */
    private void loginByStringRequest(final String loginUserName,final String loginPwdVal){
        final Dialog loadingDialog = ViewUtils.createLoadingDialog(LoginActivity.this, "正在登录......", true) ;
        loadingDialog.show();
        String loginUrl = "http://192.168.1.253/user/login.do" ;
        RequestQueue rq = Volley.newRequestQueue(LoginActivity.this) ;
        StringRequest request = new StringRequest(Request.Method.POST,
                loginUrl,
                new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }
                     },
                new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(LoginActivity.this,"登录失败！！",Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }
                     }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String,String>();
                map.put("username", loginUserName);
                map.put("password", loginPwdVal);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        } ;
        rq.add(request) ;
    }

    /**
     * 登录请求(JsonObjectRequest)
     * POST方式
     * 使用Volley的JsonObjectRequest
     */
    private void loginByJsonObjectRequest(String loginUserName,String loginPwdVal){
        final Dialog loadingDialog = ViewUtils.createLoadingDialog(LoginActivity.this, "正在登录......", true) ;
        loadingDialog.show();
        String loginUrl = "http://192.168.1.253/user/login.do" ;
        //post请求携带的参数
        Map<String,String> paramMap = new HashMap<>() ;
        paramMap.put("username",loginUserName) ;
        paramMap.put("password", loginPwdVal) ;
        final String requestBody  = buildStringParam(loginUrl,paramMap) ;

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this) ;
        JsonObjectRequest jor = new JsonObjectRequest(
                Request.Method.POST,
                loginUrl,
                (String)null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        loadingDialog.dismiss();
                        try {
                            //json数据
                            //登录成功 {"data":{"uid":1,"name":"admin"},"errMsg":"Login success","code":0} ;
                            //登录失败：{"data":[],"errMsg":"","code":4002003}
                            long code = jsonObject.getLong("code") ;
                            if(code == 0){
                                Toast.makeText(LoginActivity.this,"登录成功！！",Toast.LENGTH_SHORT).show();
                                //解析和保存用户数据
                                Boolean bool = parseAndSaveJsonData(jsonObject);
                                if(bool){
                                    //用户名密码正确，登录成功，返回主界面
                                    Intent it = new Intent() ;
                                    it.setClass(LoginActivity.this,MainActivity.class) ;
                                    startActivity(it);
                                    //关闭当前Activity
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this,"数据异常，登录失败！！",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this,"用户名或密码不正确！！",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this,"数据异常，登录失败！！",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loadingDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"连接异常，登录失败！！",Toast.LENGTH_SHORT).show();
                    }
                }){
                  @Override
                  public String getBodyContentType() {
                          //设置参数的类型；是json格式还是key-val格式
                         return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
                  }

                  @Override
                  public byte[] getBody() {
                        try {
                            //设置参数内容的编码(utf-8)
                            return requestBody == null ? null : requestBody.getBytes(PROTOCOL_CHARSET) ;
                        } catch (UnsupportedEncodingException e) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                    requestBody, PROTOCOL_CHARSET);
                            return null;
                        }
                  }
                } ;
        requestQueue.add(jor) ;
    }
    /**
     *
     * 这里未编码
     */
    private String buildStringParam(String url,Map<String,String> params){
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        for(Map.Entry<String,String> entry:params.entrySet()){
            builder.appendQueryParameter(entry.getKey(),entry.getValue());
        }
        return builder.build().getQuery();
    }

    /**
     * 注册请求(JsonObjectRequest)
     * POST方式
     * 使用Volley的JsonObjectRequest
     */
    private void registerByJsonObjectRequest(String regUserName,String regPwd,String regPwdConfirm){
        final Dialog loadingDialog = ViewUtils.createLoadingDialog(LoginActivity.this, "正在请求......", true) ;
        loadingDialog.show();
        String loginUrl = "http://192.168.1.253/user/register.do" ;
        Map<String,String> paramMap = new HashMap<>() ;
        paramMap.put("username",regUserName) ;
        paramMap.put("password", regPwd) ;
        paramMap.put("password_confirmation", regPwdConfirm) ;
        final String requestBody  = buildStringParam(loginUrl,paramMap) ;
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this) ;
        JsonObjectRequest jor = new JsonObjectRequest(
                Request.Method.POST,
                loginUrl,
                (String)null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        loadingDialog.dismiss();
                        try {
                            //json数据
                            //注册成功：{"data":{"uid":36,"name":"gppttp"},"errMsg":"Register success","code",0}
                            //注册失败：{"data":[],"errMsg":"","code",4001001}
                            long code = jsonObject.getLong("code") ;
                            if(code == 0){
                                //解析和保存数据
                                Boolean bool = parseAndSaveJsonData(jsonObject);
                                if(bool){
                                    //注册成功，启动主界面
                                    Intent it = new Intent() ;
                                    it.setClass(LoginActivity.this,MainActivity.class) ;
                                    startActivity(it);
                                    //关闭当前Activity
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this,"数据异常，注册失败！！",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                //如重复注册
                                Toast.makeText(LoginActivity.this,"其他异常，注册失败！！",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //如没有code键
                            Toast.makeText(LoginActivity.this,"数据异常，注册失败！！",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loadingDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"连接异常，注册失败！！",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public String getBodyContentType() {
                //设置参数的类型；是json格式还是key-val格式
                return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
            }

            @Override
            public byte[] getBody() {
                try {
                    //设置参数内容的编码(utf-8)
                    return requestBody == null ? null : requestBody.getBytes(PROTOCOL_CHARSET) ;
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, PROTOCOL_CHARSET);
                    return null;
                }
            }
        } ;
        requestQueue.add(jor) ;
    }

    private Boolean parseAndSaveJsonData(JSONObject jsonObject){
        Boolean result = true ;
        try {
            JSONObject dataJsonObject = jsonObject.getJSONObject("data") ;
            int uid = dataJsonObject.getInt("uid") ;
            String userName = dataJsonObject.getString("name") ;
            //将用户数据保存到SharedPreferences
            SharedPreferences spf = LoginActivity.this.getSharedPreferences("UserInfoPre", mContext.MODE_PRIVATE) ;
            SharedPreferences.Editor spfEditor = spf.edit() ;
            spfEditor.putInt("uid",uid) ;
            spfEditor.putString("username", userName) ;
            spfEditor.commit();

        } catch (JSONException e) {
            //如没有data,uid,name字段会出现异常
            result = false ;
            e.printStackTrace();
        }
        return result ;
    }

}

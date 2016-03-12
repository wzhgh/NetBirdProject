package com.netbirdtech.android.app.jiaoyiquan.network;

import com.android.volley.Response;

/**
 * Created by wzh on 2016/3/11 0011.
 */
public interface ResponseListener<T> extends Response.ErrorListener,Response.Listener<T> {

}

package com.netbirdtech.android.app.jiaoyiquan.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;

import com.netbirdtech.android.app.jiaoyiquan.R;

public class EditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setContentView(R.layout.activity_edit);


    }

}

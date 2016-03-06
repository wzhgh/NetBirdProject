package com.netbirdtech.android.app.jiaoyiquan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.netbirdtech.android.app.jiaoyiquan.R;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setContentView(R.layout.activity_edit);
    }
}

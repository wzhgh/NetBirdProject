package com.netbirdtech.android.app.jiaoyiquan.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.view.Window;

import com.liangfeizc.slidepageindicator.CirclePageIndicator;
import com.netbirdtech.android.app.jiaoyiquan.R;
import com.netbirdtech.android.app.jiaoyiquan.adapter.SimplePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PhotoPreviewActivity extends AppCompatActivity {
    public final static String EXTRA_PHOTOS = "photos";
    private ViewPager mViewPager ;
    private CirclePageIndicator mPageIndicator ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setContentView(R.layout.activity_photo_preview);
        ArrayList<String> paths = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
        mViewPager = (ViewPager)findViewById(R.id.photo_pager_id) ;
        mViewPager.setAdapter(new SimplePagerAdapter(PhotoPreviewActivity.this, paths));
        //设置ViewPager的指示器
        mPageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mPageIndicator.setViewPager(mViewPager);
    }
}

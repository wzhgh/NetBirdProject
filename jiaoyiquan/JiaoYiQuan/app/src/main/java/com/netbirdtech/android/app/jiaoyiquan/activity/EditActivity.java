package com.netbirdtech.android.app.jiaoyiquan.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.netbirdtech.android.app.jiaoyiquan.R;
import com.netbirdtech.android.app.jiaoyiquan.adapter.PhotoRVAdapter;
import com.netbirdtech.android.app.jiaoyiquan.entity.Constant;
import com.netbirdtech.android.app.jiaoyiquan.form.FormImage;
import com.netbirdtech.android.app.jiaoyiquan.network.ResponseListener;
import com.netbirdtech.android.app.jiaoyiquan.network.UploadApi;
import com.netbirdtech.android.app.jiaoyiquan.utils.PreferenceMgr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import me.iwf.photopicker.PhotoPickerActivity;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    public final static int REQUEST_CODE = 1;
    private TextView cancelTV ;
    private TextView commitTV ;
    private EditText contentET ;

    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private PhotoRVAdapter photoAdapter ;
    private RecyclerView recyclerView;
    //上传成功的图片(url地址)
    private ArrayList<String> uploadSuccPhotos = new ArrayList<>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setContentView(R.layout.activity_edit);
        initView() ;
    }

    private void initView(){
        cancelTV = (TextView)findViewById(R.id.edit_cancel_id) ;
        commitTV = (TextView)findViewById(R.id.edit_commit_id) ;
        contentET = (EditText)findViewById(R.id.edit_content_id) ;

        cancelTV.setOnClickListener(EditActivity.this);
        commitTV.setOnClickListener(EditActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        photoAdapter = new PhotoRVAdapter(EditActivity.this, selectedPhotos);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_cancel_id:
                finish();
                break ;
            case R.id.edit_commit_id:
                publishComment() ;
                break ;
            default:
                break ;
        }
    }

    private void publishComment(){
        uploadImage();
        //用户id
        int uid = PreferenceMgr.getUid() ;
        //发表内容
        String content = contentET.getText().toString() ;

    }

    /**
     * 上传图片
     */
    private void uploadImage(){
        //使用计数锁CountDownLatch，只有当所有图片上传完成后，程序才能继续往下执行
        final CountDownLatch cdl = new CountDownLatch(selectedPhotos.size()) ;
        for(String imgPath : selectedPhotos){
            FormImage imgObj = new FormImage(imgPath,Constant.UPLOAD_IMAGE_PARAM) ;
            UploadApi.uploadSingleImage(imgObj, new ResponseListener<String>() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //不管
                    cdl.countDown();
                }

                @Override
                public void onResponse(String response) {
                    //因为多个线程会操作共享数据cdl或其它的，所以需要对这里代码进行synchronized同步一下
                    cdl.countDown();
                    //解析和保存数据到uploadSuccPhotos
//                    uploadSuccPhotos.add()
                }
            });
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            //若出现异常，不管，程序继续往下走
            e.printStackTrace();
            Log.i("jiaoyiquan","====EditActivity===   CountDownLatch："+e.getMessage()) ;
        }
    }



    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
    }



    /*
    //检查权限
    private void checkPermission() {
        int permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionState != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode.ordinal());
            }
        } else {
            // Permission 通过
            onClick(requestCode.mViewId);
        }
    }
    */




}

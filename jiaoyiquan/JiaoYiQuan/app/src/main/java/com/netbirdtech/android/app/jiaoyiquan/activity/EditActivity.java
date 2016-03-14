package com.netbirdtech.android.app.jiaoyiquan.activity;

import android.Manifest;
import android.app.Dialog;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.netbirdtech.android.app.jiaoyiquan.R;
import com.netbirdtech.android.app.jiaoyiquan.adapter.PhotoRVAdapter;
import com.netbirdtech.android.app.jiaoyiquan.entity.Constant;
import com.netbirdtech.android.app.jiaoyiquan.form.FormImage;
import com.netbirdtech.android.app.jiaoyiquan.network.ResponseListener;
import com.netbirdtech.android.app.jiaoyiquan.network.UploadApi;
import com.netbirdtech.android.app.jiaoyiquan.utils.PreferenceMgr;
import com.netbirdtech.android.app.jiaoyiquan.utils.ViewUtils;
import com.netbirdtech.android.app.jiaoyiquan.utils.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import me.iwf.photopicker.PhotoPickerActivity;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    public final static int REQUEST_CODE = 1;
    private TextView cancelTV ;
    private TextView commitTV ;
    private EditText contentET ;

    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private ArrayList<String> upLoadSucPhotos = new ArrayList<>() ;
    private PhotoRVAdapter photoAdapter ;
    private RecyclerView recyclerView;

    private Dialog mDialog = null ;
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

        mDialog= ViewUtils.createLoadingDialog(EditActivity.this, "正在发表，请稍等......", true) ;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_cancel_id:
                finish();
                break ;
            case R.id.edit_commit_id:
                mDialog.show();
                //从第一张图片开始上传
                uploadImage(0);
                break ;
            default:
                break ;
        }
    }

    /**
     * 上传图片
     * 使用递归，一张一张上传
     */
    private void uploadImage(int value){
        if(selectedPhotos==null || selectedPhotos.size() == 0 || value < 0 || value > selectedPhotos.size()){
            return ;
        }
        final int count = value ;
        String imgPath = selectedPhotos.get(count) ;
        FormImage imgObj = new FormImage(imgPath, Constant.UPLOAD_IMAGE_PARAM) ;
        UploadApi.uploadSingleImage(imgObj, new ResponseListener<String>() {
            int newCount = 0;
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //上传出现问题，这里不做处理
                newCount = count + 1;
                if (newCount < selectedPhotos.size()) {
                    //上传下一张图片
                    uploadImage(newCount);
                } else {
                    //new Count == selectedPhotos.size() ：表示的图片都上传完了
                    //然后提交发表请求
                    publishContent();
                }
            }

            @Override
            public void onResponse(String response) {
                //解析和保存数据
                parseUploadImgResponse(response);
                newCount = count + 1;
                if (newCount < selectedPhotos.size()) {
                    uploadImage(newCount);
                } else {
                    //new Count == selectedPhotos.size() ：表示的图片都上传完了
                    //然后提交发表请求
                    publishContent();
                }
            }
        });
    }

    private void publishContent(){
//        发表喊单
//        接口  /message/post
//        参数  uid(用户id)，content(喊单内容)，images(图片列表["http://a.com/upload/1.jpg","http:/a.com/upload/2.png"]) 键值对格式
//        方式  POST
//
//       数据格式(注意value都没有双引号)： uid=15&content=aabbb&images=["http://a.com/upload/1.jpg","http:/a.com/upload/2.png"]
        RequestQueue rq = VolleyUtil.getRequestQueue() ;
        StringRequest request = new StringRequest(Request.Method.POST, Constant.PUBLISH_CONTENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        //{"code":0,"errMsg":"success","data":[]}
                        try {
                            JSONObject jo = new JSONObject(response) ;
                            int code = jo.getInt("code") ;
                            if (code == 0 ){
                                Toast.makeText(EditActivity.this, "发表成功!!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(EditActivity.this,"发表失败，请重新发表！",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditActivity.this,"发表失败，请重新发表！",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mDialog.dismiss();
                        Toast.makeText(EditActivity.this,"发表失败，请重新发表！"+ volleyError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                int uid = PreferenceMgr.getUid() ;
                String content = contentET.getText().toString() ;
                StringBuilder imagesSB = new StringBuilder() ;
                imagesSB.append("[") ;
                for (int i = 0 ; i < upLoadSucPhotos.size() ; i++){
                    imagesSB.append("\"" + upLoadSucPhotos.get(i) + "\"") ;
                    if (i + 1 < upLoadSucPhotos.size()){
                        imagesSB.append(",") ;
                    }
                }
                imagesSB.append("]") ;
                Map<String,String> map = new HashMap<>();
                map.put("uid",uid+"");
                map.put("content",content) ;
                map.put("images", imagesSB.toString()) ;
                return map;
            }
        } ;
        rq.add(request) ;
    }

    /**
     * 图片上传成功后，解析返回的图片url地址
     * @param response
     */
    public void parseUploadImgResponse(String response){
        //{"code":0,"errMsg":"upload success","data":{"url":"http:\/\/192.168.1.253\/upload\/20160314\/1457936466ecztau.png"}}
        try {
            JSONObject jsonObj = new JSONObject(response) ;
            int code = jsonObj.getInt("code") ;
            if (code == 0){
                JSONObject dataJO = jsonObj.getJSONObject("data") ;
                String imgUrl = dataJO.getString("url") ;
                upLoadSucPhotos.add(imgUrl) ;
            }
        } catch (JSONException e) {
            //不处理
            e.printStackTrace();
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

package com.netbirdtech.android.app.jiaoyiquan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.netbirdtech.android.app.jiaoyiquan.R;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView cancelTV ;
    private TextView commitTV ;
    private ImageView addImage ;

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
        addImage = (ImageView)findViewById(R.id.addimage_id) ;

        cancelTV.setOnClickListener(EditActivity.this);
        commitTV.setOnClickListener(EditActivity.this);
        addImage.setOnClickListener(EditActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_cancel_id:
                finish();
                break ;
            case R.id.edit_commit_id:
                break ;
            case R.id.addimage_id:
                //打开图片选择
                break ;
            default:
                break ;
        }
    }
}

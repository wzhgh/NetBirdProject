package com.netbirdtech.android.app.jiaoyiquan.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netbirdtech.android.app.jiaoyiquan.R;

/**
 * 登录注册页面
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private TextView mBackTV ;
    private TextView mSwitchToRegTV ;
    private TextView mSwitchToLogTV ;
    private View mLoginPart ;
    private View mRegPart ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView() ;
    }

    private void initView(){
        mBackTV = (TextView)findViewById(R.id.backtomain_id) ;
        mBackTV.setOnClickListener(this);

        mSwitchToRegTV = (TextView)findViewById(R.id.switchtoreg_id) ;
        mSwitchToRegTV.setOnClickListener(this);

        mLoginPart = findViewById(R.id.login_part_id) ;
        mRegPart = findViewById(R.id.register_part_id) ;

        mSwitchToLogTV = (TextView)findViewById(R.id.switchtolog_id) ;
        mSwitchToLogTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backtomain_id :
                LoginActivity.this.finish();
                break ;
            case R.id.switchtoreg_id:
//                Toast.makeText(this,"duang duang duang.......",Toast.LENGTH_SHORT).show();
                mLoginPart.setVisibility(View.GONE);
                mRegPart.setVisibility(View.VISIBLE);
                break ;
            case R.id.switchtolog_id:
                mLoginPart.setVisibility(View.VISIBLE);
                mRegPart.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }




}

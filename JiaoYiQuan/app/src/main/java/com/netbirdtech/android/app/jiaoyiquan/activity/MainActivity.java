package com.netbirdtech.android.app.jiaoyiquan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.netbirdtech.android.app.jiaoyiquan.R;
import com.netbirdtech.android.app.jiaoyiquan.adapter.SideLVAdapter;
import com.netbirdtech.android.app.jiaoyiquan.entity.ItemBean;
import com.netbirdtech.android.app.jiaoyiquan.utils.ItemDataUtils;
import com.netbirdtech.android.app.jiaoyiquan.widget.DragLayout;
import com.nineoldandroids.view.ViewHelper;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.iwf.photopicker.utils.PhotoPickerIntent;

public class MainActivity extends BaseActivity {
    private Context mContext ;
    private DragLayout dragLayout;
    private ListView leftLV;
    private ImageView topLeftIV, topEditIV;
    private QuickAdapter<ItemBean> quickAdapter;
    private SideLVAdapter sideAdapter ;
    private String loginUserName ;
    //false:未登录，true：登录
    private Boolean loginFlag = false ;

    private FragmentManager mSupportFragmentMgr ;
    /**
     * 测试使用butterknife库
     @Bind(R.id.top_edit_iv_id)
     EditText uu ;
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //api>=19时起作用
        setStatusBar();
        initView() ;
        initDragLayout() ;
        loadData();
        //测试使用PhotoPicker库
        PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
    }

    private void initDragLayout() {
        dragLayout = (DragLayout) findViewById(R.id.main_draglayout_id);
        dragLayout.setDragListener(new DragLayout.DragListener() {
            //界面打开的时候
            @Override
            public void onOpen() {
            }

            //界面关闭的时候
            @Override
            public void onClose() {
            }

            //界面滑动的时候
            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(topLeftIV, 1 - percent);
            }
        });
    }

    private void initView() {
        mContext = MainActivity.this ;
        mSupportFragmentMgr = this.getSupportFragmentManager();
        //左边图标
        topLeftIV = (ImageView) findViewById(R.id.top_left_icon_id);
        topLeftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dragLayout.open();
            }
        }) ;
        //右边图标
        topEditIV = (ImageView)findViewById(R.id.top_edit_icon_id) ;
        topEditIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this,EditActivity.class) ;
                startActivity(it);
            }
        });
        //左侧View中的ListView
        leftLV = (ListView) findViewById(R.id.side_lv_id);
        quickAdapter=new QuickAdapter<ItemBean>(this,R.layout.sidelv_item_layout, ItemDataUtils.getItemBeans()) {
            @Override
            protected void convert(BaseAdapterHelper helper, ItemBean item) {
                helper.setImageResource(R.id.sidelv_item_img_id,item.getImg())
                        .setText(R.id.sidelv_item_tv_id,item.getTitle());
            }
        };

        leftLV.setAdapter(quickAdapter);

        leftLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

                switch (position) {
                    case 0:
                        if(!loginFlag){
                            //跳到登录注册页面
                            Intent it = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(it);
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        if(loginFlag){
                            Dialog.Builder builder = null;
                            builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog){
                                @Override
                                public void onPositiveActionClicked(DialogFragment fragment) {
                                    SharedPreferences spf = mContext.getSharedPreferences("UserInfoPre", mContext.MODE_PRIVATE) ;
                                    spf.edit().clear() ;
                                    loginFlag = false ;
                                    quickAdapter.getItem(0).setTitle("登录/注册");
                                    quickAdapter.notifyDataSetChanged();
                                    dragLayout.close();
                                    super.onPositiveActionClicked(fragment);
                                }
                                @Override
                                public void onNegativeActionClicked(DialogFragment fragment) {
                                    super.onNegativeActionClicked(fragment);
                                }
                            };
                            ((SimpleDialog.Builder)builder).message("        确定要退出吗 ？        ")
                                    .positiveAction("确定")
                                    .negativeAction("取消");
                            //显示出来
                            DialogFragment fragment = DialogFragment.newInstance(builder);
                            fragment.show(mSupportFragmentMgr, null);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 加载用户信息
     */
    private void loadData(){
        SharedPreferences spf = mContext.getSharedPreferences("UserInfoPre", mContext.MODE_PRIVATE) ;
        loginUserName = spf.getString("username","") ;

        if(loginUserName != ""){
            loginFlag = true ;
            //更新数据
            quickAdapter.getItem(0).setTitle(loginUserName);
            quickAdapter.notifyDataSetChanged();
        }else{
            loginFlag = false ;
        }
    }
}

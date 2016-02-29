package com.netbirdtech.android.app.jiaoyiquan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.netbirdtech.android.app.jiaoyiquan.R;
import com.netbirdtech.android.app.jiaoyiquan.adapter.SideLVAdapter;
import com.netbirdtech.android.app.jiaoyiquan.entity.ItemBean;
import com.netbirdtech.android.app.jiaoyiquan.utils.ItemDataUtils;
import com.netbirdtech.android.app.jiaoyiquan.widget.DragLayout;
import com.nineoldandroids.view.ViewHelper;

public class MainActivity extends BaseActivity {
    private DragLayout dragLayout;
    private ListView leftLV;
    private ImageView topLeftIV, topEditIV;
    private QuickAdapter<ItemBean> quickAdapter;
    private SideLVAdapter sideAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar();
        initDragLayout() ;
        initView() ;
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
        topLeftIV = (ImageView) findViewById(R.id.top_left_icon_id);
        topLeftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dragLayout.open();
            }
        }) ;


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
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                switch (position){
                    case 0:
                        //登录注册页面
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        break ;
                }
                Toast.makeText(MainActivity.this, "Click Item " + position, Toast.LENGTH_SHORT).show();
            }
        });

        topEditIV = (ImageView)findViewById(R.id.top_edit_iv_id) ;
        topEditIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this,EditActivity.class) ;
                startActivity(it);
            }
        });
    }

}

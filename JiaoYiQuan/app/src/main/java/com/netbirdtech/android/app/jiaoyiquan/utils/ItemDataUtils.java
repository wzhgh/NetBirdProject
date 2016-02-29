package com.netbirdtech.android.app.jiaoyiquan.utils;
import com.netbirdtech.android.app.jiaoyiquan.R;
import com.netbirdtech.android.app.jiaoyiquan.entity.ItemBean;

import java.util.ArrayList;
import java.util.List;


public class ItemDataUtils {
    public static List<ItemBean> getItemBeans(){
        List<ItemBean> itemBeans=new ArrayList<>();
        itemBeans.add(new ItemBean(R.mipmap.sidebar_loginuser,"登录/注册",false));
        itemBeans.add(new ItemBean(R.mipmap.sidebar_realuser,"申请认证",false));
        itemBeans.add(new ItemBean(R.mipmap.sidebar_exit,"退出",false));
        return  itemBeans;
    }

}

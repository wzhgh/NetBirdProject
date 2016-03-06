package com.netbirdtech.android.app.jiaoyiquan.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.netbirdtech.android.app.jiaoyiquan.R;

public class MainContentFragment extends android.app.Fragment {

    private View mView ;
    private PullToRefreshListView mRtfListView ;

    public MainContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main_content, container, false);
        mRtfListView = (PullToRefreshListView)mView.findViewById(R.id.rtf_lv_id) ;
        mRtfListView.setMode(PullToRefreshBase.Mode.BOTH);
        return mView ;
    }
}

package com.netbirdtech.android.app.jiaoyiquan.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.netbirdtech.android.app.jiaoyiquan.R;
import com.netbirdtech.android.app.jiaoyiquan.adapter.TalkAdapter;
import com.netbirdtech.android.app.jiaoyiquan.entity.Talk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainContentFragment extends Fragment {
    private Context mActivity ;
    private View mView ;
    private PullToRefreshListView mPtrListView ;
    private TalkAdapter mAdapter ;
    //先给mDataList初始化一个ArrayList,防止传null
    private List<Talk> mDataList = new ArrayList<>() ;

    public MainContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main_content, container, false);
        mActivity = getActivity() ;
        initView() ;
        initData() ;
        return mView ;
    }

    private void initView(){
        mPtrListView = (PullToRefreshListView)mView.findViewById(R.id.ptr_lv_id) ;
        mPtrListView.setMode(PullToRefreshBase.Mode.BOTH);
        //下拉刷新，刷新事件调用
        mPtrListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载数据
//                Toast.makeText(mContext, "没有数据了", Toast.LENGTH_SHORT).show();
//                mPtrListView.onRefreshComplete();

//                全部数据 下拉刷新
//                接口  /message/flush
//                参数  必要：msg_id(指定从哪个消息ID开始检查刷新)
//                可选：rn
//                方式 GET
//
//                全部数据 上拉加载
//                接口  /message/history
//                参数  必要：msg_id(指定从哪个消息ID开始检查刷新)
//                可选：rn
//                方式 GET

                String url = "http://192.168.1.253/message/flush" ;
                int msg_id =  mDataList.get(0).getmId();
                Map<String,String> paramMap  = new HashMap<>() ;
                paramMap.put("msg_id",msg_id+"") ;
                paramMap.put("rn","10") ;
                getDataByGet(url, paramMap, new DataListener() {
                    @Override
                    public void onResponse(ArrayList<Talk> list) {
                        mDataList.addAll(0,list) ;
                        mAdapter.notifyDataSetChanged();
                        mPtrListView.onRefreshComplete();
                    }

                    @Override
                    public void onErrorResponse() {
                        mPtrListView.onRefreshComplete();
                    }
                }) ;
            }
        });
        // 上拉加载，刷新事件调用
        mPtrListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                //加载数据
//                Toast.makeText(mContext, "End of List!", Toast.LENGTH_SHORT).show();
//                mPtrListView.onRefreshComplete();

                String url = "http://192.168.1.253/message/history" ;
                int msg_id =  mDataList.get(mDataList.size()-1).getmId() ;
                Map<String,String> paramMap  = new HashMap<>() ;
                paramMap.put("msg_id",msg_id+"") ;
                paramMap.put("rn", "10") ;
                getDataByGet(url, paramMap, new DataListener() {
                    @Override
                    public void onResponse(ArrayList<Talk> list) {
                        mDataList.addAll(mDataList.size()-1, list);
                        mAdapter.notifyDataSetChanged();
                        mPtrListView.onRefreshComplete();
                    }

                    @Override
                    public void onErrorResponse() {
                        mPtrListView.onRefreshComplete();
                    }
                }) ;
            }
        });
        //PullToRefreshListView里真实的ListView
        // You can also just use setListAdapter(mAdapter) or
        // mPullRefreshListView.setAdapter(mAdapter)
        ListView  actualListView = mPtrListView.getRefreshableView() ;
        mAdapter = new TalkAdapter(mActivity,mDataList) ;
        actualListView.setAdapter(mAdapter);
    }

    private void initData(){
//        Talk talk ;
//        for(int i = 0 ; i < 5 ; i++){
//            talk = new Talk() ;
//            mDataList.add(talk) ;
//        }
//        mAdapter.notifyDataSetChanged();

        //打开界面，加载全部数据
        String url = "http://192.168.1.253/message/latest" ;
        Map<String,String> paramMap  = new HashMap<>() ;
        paramMap.put("rn","10") ;
        getDataByGet(url, paramMap, new DataListener() {
            @Override
            public void onResponse(ArrayList<Talk> list) {
                mDataList.addAll(list) ;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorResponse() {

            }
        }) ;
    }

    private void getDataByGet(String url , Map<String,String> paramMap,final DataListener listener){
//        String url = "http://192.168.1.253/message/latest?rn=10" ;       //get方式,参数rn=10 表示返回数据的条数，不写就默认10条
        StringBuilder sb = new StringBuilder() ;
        if(paramMap != null && paramMap.size() > 0 ){
            for(Map.Entry<String,String> entry : paramMap.entrySet()){
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&") ;
            }
            sb.deleteCharAt(sb.length()-1) ;
        }
        String realUrl = url + "?" + sb.toString() ;

        RequestQueue requestQueue =  Volley.newRequestQueue(getContext()) ;
        JsonObjectRequest jor = new JsonObjectRequest(
                Request.Method.GET,
                realUrl,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        //获取到json数据
                       // {"code":0,"errMsg":"","data":[{"id":142,"user_id":12,"username":"hehe","content":null,"image_urls":["http:\/\/192.168.1.253\/upload\/20160229\/1456727904rhepgj.png"],"created_at":"2016-02-29 14:38:25"},{},...]}
                        try {
                            int code  = jsonObject.getInt("code") ;
                            if(code == 0){
                                ArrayList<Talk> list = parseJsonData(jsonObject) ;
//                                mDataList.addAll(list) ;
//                                mAdapter.notifyDataSetChanged();
                                listener.onResponse(list);
                            }else{
                                Toast.makeText(mActivity, "数据异常，获取数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(mActivity, "发生异常，获取数据失败！", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //获取出现异常
                        Toast.makeText(mActivity, "连接异常，获取数据失败！", Toast.LENGTH_SHORT).show();
                        listener.onErrorResponse();
                    }
                }) ;
        requestQueue.add(jor) ;
    }

    /**
     * 解析数据
     */
    private ArrayList<Talk> parseJsonData(JSONObject jsonObject){
        ArrayList<Talk> list = new ArrayList<Talk>() ;
        try {
            JSONArray dataJsonArray= jsonObject.getJSONArray("data") ;
            for(int i = 0 ; i < dataJsonArray.length() ; i++){
                Talk talk = new Talk() ;
                JSONObject itemJsonObject = dataJsonArray.getJSONObject(i) ;
                int msgId = itemJsonObject.getInt("id") ;                        //TODO id会不会非常大?
                int userId  =itemJsonObject.getInt("user_id");     //TODO  user_id会不会非常大?
                String userName = itemJsonObject.getString("username") ;
                String content = itemJsonObject.getString("content");          //TODO  参数是"content":null 需要判断Object==null情况
                content = (content == null|| content == "null" ? "" : content) ;
                String createdData = itemJsonObject.getString("created_at") ;
                List<String> imageUrlList = new ArrayList<>() ;
                //获取图片的url
                JSONArray imageUrlArray = itemJsonObject.getJSONArray("image_urls") ;
                for(int j = 0 ; j < imageUrlArray.length() ; j++){
                    imageUrlList.add(imageUrlArray.getString(j)) ;
                }

                talk.setmId(msgId);
                talk.setUserId(userId);
                talk.setUserName(userName);
                talk.setContent(content);
                talk.setCreatedData(createdData);
                talk.setImageUrlList(imageUrlList);

                list.add(talk) ;
            }

        } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(mActivity, "数据异常，获取数据失败！", Toast.LENGTH_SHORT).show();
                }
                return list ;
            }
}

interface DataListener{
    void onResponse(ArrayList<Talk> list) ;
    void onErrorResponse() ;
}

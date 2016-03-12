package com.netbirdtech.android.app.jiaoyiquan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netbirdtech.android.app.jiaoyiquan.R;
import com.netbirdtech.android.app.jiaoyiquan.activity.PhotoPreviewActivity;
import com.netbirdtech.android.app.jiaoyiquan.entity.Talk;
import com.netbirdtech.android.app.jiaoyiquan.utils.VolleySington;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPagerActivity;

/**
 * Created by Administrator on 2016/3/7 0007.
 */
public class TalkAdapter extends BaseAdapter{

    private Context mContext ;
    private Activity mActivity ;
    private LayoutInflater mInflater ;
    private List<Talk> mDataList ;
    public TalkAdapter(Context context, List<Talk> dataList) {
        mContext = context ;
        mActivity = (FragmentActivity)context ;
        mInflater = LayoutInflater.from(context) ;
        //dataList最好不要为null
        mDataList = (dataList == null ? new ArrayList<Talk>():dataList) ;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //ItemView缓存
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.talk_item,null);
            //视图缓存
            holder = new ViewHolder();
            holder.userNameTV = (TextView)convertView.findViewById(R.id.item_username_id) ;
            holder.attestIV = (ImageView)convertView.findViewById(R.id.item_attest_id) ;
            holder.attentionCB = (CheckBox)convertView.findViewById(R.id.item_attention_id) ;
            holder.publishTimeTV =  (TextView)convertView.findViewById(R.id.item_time_id) ;
            holder.contentTV = (TextView)convertView.findViewById(R.id.item_content_id) ;
            //ImageView视图集合
            ArrayList<ImageView> ivArr = new ArrayList<>() ;
            ivArr.add((ImageView)convertView.findViewById(R.id.item_pic_0)) ;
            ivArr.add((ImageView)convertView.findViewById(R.id.item_pic_1)) ;
            ivArr.add((ImageView)convertView.findViewById(R.id.item_pic_2)) ;
            ivArr.add((ImageView)convertView.findViewById(R.id.item_pic_3)) ;
            ivArr.add((ImageView)convertView.findViewById(R.id.item_pic_4)) ;
            ivArr.add((ImageView)convertView.findViewById(R.id.item_pic_5)) ;
            holder.ivArr = ivArr ;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        //获取数据，设置数据
        Talk data = mDataList.get(position) ;
        holder.userNameTV.setText(data.getUserName());
        holder.publishTimeTV.setText(data.getCreatedData());
        if(data.getContent() == ""){
            holder.contentTV.setVisibility(TextView.GONE);
        }else{
            holder.contentTV.setText(data.getContent());
        }
        ArrayList<String> imageUrlList = (ArrayList<String>)data.getImageUrlList() ;
        //过滤多余的ImageView
        if(imageUrlList.size() == 0 ){
            for(int i = 3 ; i < holder.ivArr.size() ; i++){
                ImageView  itemImageview = holder.ivArr.get(i) ;
                itemImageview.setVisibility(ImageView.GONE);
            }
        }else if(imageUrlList.size() - 3 <= 0){
            for(int i = 3 ; i < holder.ivArr.size() ; i++){
                ImageView  itemImageview = holder.ivArr.get(i) ;
                itemImageview.setVisibility(ImageView.GONE);
            }
        }

//        ImageClickListener listener = new ImageClickListener(imageUrlList);
//        for(int i = 0 ; i < imageUrlList.size() ; i++){
//            final ImageView  itemImageview = holder.ivArr.get(i) ;
//            String imageUrl = imageUrlList.get(i) ;
//            //使用Volley的ImageRequest来加载图片
//            ImageRequest request = new ImageRequest(imageUrl,
//                    new Response.Listener<Bitmap>() {
//                        @Override
//                        public void onResponse(Bitmap bitmap) {
//                            //320 * 480 = 2 : 3
//                            itemImageview.setImageBitmap(bitmap);
//                        }
//                    }, 0, 0, null,
//                    new Response.ErrorListener() {
//                        public void onErrorResponse(VolleyError error) {
//                        }
//                    });
//            VolleySington.getInstance(mContext).addToRequestQueue(request);
//            //为图片添加点击事件
//            itemImageview.setOnClickListener(listener);
//        }

        for(int i = 0 ; i < imageUrlList.size() ; i++){
            String imageUrl = imageUrlList.get(i) ;
            ImageView  itemImageview = holder.ivArr.get(i) ;
            //使用bumptech/glide 来加载图片
            Glide.with(mActivity).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().crossFade().into(itemImageview) ;
            //ImageView预览
            itemImageview.setOnClickListener(new ImageClickListener(mActivity,imageUrlList)) ;
        }
        return convertView;
    }

    public final class ViewHolder{
        public TextView userNameTV;
        public ImageView attestIV ;
        public CheckBox attentionCB ;
        public TextView publishTimeTV ;
        public TextView contentTV ;
        public ArrayList<ImageView> ivArr ;
    }

    class ImageClickListener implements View.OnClickListener{
        private Activity activity ;
        private ArrayList<String> mPhotoPaths ;
        private int currentItem = 0 ;

        public ImageClickListener(Activity activity,ArrayList<String> photoPaths) {
            this.activity =  activity;
            mPhotoPaths = photoPaths ;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.item_pic_0:
                    currentItem = 0 ;
                    break ;
                case R.id.item_pic_1:
                    currentItem = 1 ;
                    break ;
                case R.id.item_pic_2:
                    currentItem = 2 ;
                    break ;
                case R.id.item_pic_3:
                    currentItem = 3;
                    break ;
                case R.id.item_pic_4:
                    currentItem = 4 ;
                    break ;
                case R.id.item_pic_5:
                    currentItem = 5 ;
                    break ;
                default:
                    currentItem = 0 ;
                    break;
            }
            if(mPhotoPaths == null || mPhotoPaths.size() == 0 || currentItem < 0 || currentItem >= mPhotoPaths.size()){
                return ;
            }
            Intent intent = new Intent(activity, PhotoPreviewActivity.class);
            intent.putExtra(PhotoPreviewActivity.EXTRA_PHOTOS,mPhotoPaths);
            activity.startActivity(intent);
        }
    }
}
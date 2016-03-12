package com.netbirdtech.android.app.jiaoyiquan.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2016/3/10 0010.
 */
public class SimplePagerAdapter extends PagerAdapter {

    private Context mContext ;
    private ArrayList<String> photoPaths ;
    public SimplePagerAdapter(Context context,ArrayList<String> photoUrls) {
        mContext = context ;
        photoPaths = photoUrls ;
    }

    @Override
    public int getCount() {
        return photoPaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
     public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        String imageUrl = photoPaths.get(position) ;
        Glide.with(mContext).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().crossFade().into(photoView) ;

        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

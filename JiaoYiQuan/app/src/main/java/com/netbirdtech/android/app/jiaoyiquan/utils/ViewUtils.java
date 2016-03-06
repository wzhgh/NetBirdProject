package com.netbirdtech.android.app.jiaoyiquan.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netbirdtech.android.app.jiaoyiquan.R;
import com.rey.material.widget.ProgressView;

/**
 * Created by Administrator on 2016/3/2 0002.
 */
public class ViewUtils {

    /**
     * 自定义吐司
     *
     * @param context  上下文
     * @param text     吐司内容
     * @param duration 显示时长
     */
    public static void makeToast(Context context,
                                 String text, int duration) {
        /*Toast result = new Toast(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.toast_layout, null);
		TextView textView = (TextView) layout.findViewById(R.id.toast_text);
		textView.setText(text);
		result.setView(layout);
		result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		result.setDuration(duration);
		result.show();
		*/

//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.my_toast_layout, null);
//        TextView chapterNameTV = (TextView) view.findViewById(R.id.chapterName);
//        chapterNameTV.setText(text);
//        Toast toast = new Toast(context);
//        toast.setGravity(Gravity.TOP, 0, 0);
//        toast.setDuration(duration);
//        toast.setView(view);
//        toast.show();
    }

    /**
     * 得到自定义的progressDialog
     */
    public static Dialog createLoadingDialog(Context context, String msg, boolean isCancel) {
        /*
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        CircleProgressBar img = (CircleProgressBar) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        img.setColorSchemeResources(R.color.title_bg);

        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.MyProgressDialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(isCancel);// 可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;
        */

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);       // 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        ProgressView progressView = (ProgressView)v.findViewById(R.id.progress_pv_circular) ;

        tipTextView.setText(msg);       // 设置加载信息
        progressView.start();

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        // 可以用“返回键”取消
        loadingDialog.setCancelable(isCancel);
        //设置是否可以在dialog边框外取消;这里设置为不可以
        loadingDialog.setCanceledOnTouchOutside(false) ;
        // 设置布局，为什么用layout,而不用v,因为布局参数必须是父类
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }
}

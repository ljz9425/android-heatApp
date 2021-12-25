package com.whisht.heatapp.view.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whisht.heatapp.R;
import com.whisht.heatapp.constant.AppConstant;
import com.whisht.heatapp.view.dialog.LoadingDialog;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import butterknife.ButterKnife;

public abstract class HeatBaseView extends BaseView implements View.OnClickListener, IDataHandler {
    protected ImageView titleBack;
    protected TextView titleName;
    protected TextView titleNext;
    protected LinearLayout llTitleBack;
    protected LinearLayout llTitleNext;

    public interface  TitleOnClickListener {
        void onBackClick();
        void onNextClick();
    }
    private TitleOnClickListener titleOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onAfterCreate() {
        ScreenAdapterTools.getInstance().loadView(activity.getWindow().getDecorView());
        ButterKnife.bind(activity);
        titleBack = activity.findViewById(R.id.titleBack);
        if (null != titleBack) {
            titleBack.setOnClickListener(this);
        }
        titleName = activity.findViewById(R.id.titleName);
        titleName = activity.findViewById(R.id.titleName);
        titleNext = activity.findViewById(R.id.titleNext);
        llTitleNext = activity.findViewById(R.id.llTitleNext);
        llTitleBack = activity.findViewById(R.id.llTitleBack);

        if(titleNext != null)
            titleNext.setOnClickListener(this);
        if(llTitleNext != null)
            llTitleNext.setOnClickListener(this);
        if(llTitleBack != null)
            llTitleBack.setOnClickListener(this);
        showTitleNextOperator(false);
        setWindowStatusBarColor(R.color.common_title_back_color);
    }


    /**
     * 是否显示返回
     * @param visible
     */
    public void showTitleBackOperator(boolean visible){
        if(this.llTitleBack != null)
            this.llTitleBack.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }

    /**
     * 是否显示下一步
     * @param visiable
     */
    public void showTitleNextOperator(boolean visiable){
        if(llTitleNext != null) {
            this.llTitleNext.setVisibility(visiable ? View.VISIBLE : View.INVISIBLE);
            if(visiable)
                this.titleNext.setText("下一步");
        }
    }
    /**
     * 设置标题栏主题名称
     * @param name
     */
    public void setTitleName(int name){
        this.titleName.setText(name);
    }

    /**
     * 设置标题栏主题名称
     * @param name
     */
    public void setTitleName(String name){
        if(this.titleName != null)
            this.titleName.setText(name);
    }

    /**
     * 设置标题栏点击事件监听
     * @param titleOnClickListener
     */
    public void setTitleOnClickListener(TitleOnClickListener titleOnClickListener){
        this.titleOnClickListener = titleOnClickListener;
    }
    public void setTitleOnClickListener(TitleOnClickListener titleOnClickListener,int titlename){
        this.titleOnClickListener = titleOnClickListener;
        setTitleName(titlename);
    }
    public void setTitleOnClickListener(TitleOnClickListener titleOnClickListener,String titlename){
        this.titleOnClickListener = titleOnClickListener;
        setTitleName(titlename);
    }
    private LoadingDialog loadingDialog;

    public void showLoading() {
        if(loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity());
        }
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBack:
            case R.id.llTitleBack:
                if(titleOnClickListener != null)
                    titleOnClickListener.onBackClick();
                break;
            case R.id.llTitleNext:
            case R.id.titleNext:
                if(titleOnClickListener != null) {
                    titleOnClickListener.onNextClick();
                }
                break;
            default:
                break;
        }
    }

    public void sendMsg() {}
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    public void startReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstant.RECEIVE_ACTION);
        activity.registerReceiver(broadcastReceiver, filter);
    }

    public void startReceiver(Integer priority) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstant.RECEIVE_ACTION);
        filter.setPriority(priority);
        activity.registerReceiver(broadcastReceiver, filter);
    }

    public void stopReceiver() {
        try {
            activity.unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

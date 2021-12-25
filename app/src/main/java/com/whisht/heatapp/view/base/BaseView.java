package com.whisht.heatapp.view.base;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.whisht.heatapp.utils.PermissionHelper;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseView implements IBaseView {
    protected AtomicBoolean isReqPer = new AtomicBoolean(false);

    public AtomicBoolean getIsReqPer() {
        return isReqPer;
    }

    private PermissionHelper permissionHelper;
    protected int targetSdkVersion;
    protected boolean isShowBottomVKey = true;
    protected Activity activity;

    protected abstract Activity getActivity();
    public BaseView() {}

    protected void withShowBottomVKey(boolean isShow) { this.isShowBottomVKey = isShow; }
    protected void onCreate(Bundle savedInstanceState) {
        activity = getActivity();
    }
    protected void onResume() { requestPermission(); }

    protected void requestPermission() {
        requestPermission(true, getPermission(), Integer.MAX_VALUE);
    }
    public void requestPermission(boolean isMustNeed,String[][] permissions,int sign){
        if (permissions.length <= 0) {
            OnGranted();
            return;
        }
        if(isReqPer.compareAndSet(false,true)) {
            if(permissionHelper == null){
                permissionHelper = new PermissionHelper(getActivity(),this);
            }
            permissionHelper.requestPermission(isMustNeed,sign,permissions);
        }
    }
    public void requestPermission(String[][] permissions){
        requestPermission(true,permissions,Integer.MAX_VALUE);
    }
    protected void onDestroy() {
        ActivityManager.getInstance().popOneActivity(activity);
    }

    /**
     * @return判断是否需要隐藏虚拟按键
     */
    protected boolean isNeedHideVKey() {
        Point p = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(p);
        if(p.x == 720 && p.y < 1280 || p.x == 1080 && p.y < 1920)
            return true;
        return false;
    }
    /**
     * 隐藏虚拟按键，滑动显示
     */
    public void hideBottomUIMenu(boolean isHide) {
        if(!isHide){
            //显示虚拟按键，并且全屏
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) { // lower api
                View v = activity.getWindow().getDecorView();
                v.setSystemUiVisibility(View.VISIBLE);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //for new api versions.
                View decorView = activity.getWindow().getDecorView();
                int uiOptions = View.VISIBLE;
                decorView.setSystemUiVisibility(uiOptions);
            }
            return;
        }
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 设置顶部状态栏颜色
     * @param colorResId
     */
    public  void setWindowStatusBarColor(int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 是否显示状态栏
     * @param visiable
     */
    private void setTranslucentVisiable(boolean visiable){
        if(!visiable)
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        else
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
    }
    /**
     * 状态栏透明
     * @param on
     */
    private void setTranslucentStatus(boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void showToastMsg(String msg){
        Toast toast = Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM,0,-600);
        toast.show();
    }

    public void showToastMsg(String msg, int duration){
        Toast toast = Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM,0,-600);
        toast.show();
    }

    public void showToastMsg(int resId){
        Toast.makeText(getActivity(),resId,Toast.LENGTH_SHORT).show();
    }
}

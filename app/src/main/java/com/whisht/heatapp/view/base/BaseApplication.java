package com.whisht.heatapp.view.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.concurrent.atomic.AtomicInteger;

public class BaseApplication extends Application {
    public static AtomicInteger notificationIndex = new AtomicInteger(0);
    private static Context mContext = null;
    private static BaseApplication sInstance;

    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        mContext = getApplicationContext();
        ScreenAdapterTools.init(this);
        //初始化缓存配置，包括磁盘缓存路径，缓存大小，内存缓存大小，加密策略等。
        // 最后调用.install(this)方法完成初始化
        //TODO RxCache
//        CacheInstaller.get()
//                .configDiskCache("WGZFCache", 200 * 1024 * 1024, 1)
//                .install(this);
//        if(Util.isApkInDebug(this))
//            JPushInterface.setDebugMode(true);  // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);            // 初始化 JPush
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
            @Override public void onActivityStarted(Activity activity) {
            }
            @Override public void onActivityResumed(Activity activity) {
                ActivityManager.getInstance().setCurrentActivity(activity);
            }
            @Override public void onActivityPaused(Activity activity) {
            }
            @Override public void onActivityStopped(Activity activity) {
            }

            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override public void onActivityDestroyed(Activity activity) {
            }
        });

//        LogToFileUtils.init(this); //初始化.
        initScreenSize();
    }

    public void setWifiDormancy(){
        int wifiSleepValue= Settings.System.getInt(getContentResolver(),Settings.System.WIFI_SLEEP_POLICY,
                Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
        //设定为“总是”
        Settings.System.putInt(getContentResolver(),Settings.System.WIFI_SLEEP_POLICY,Settings.System.WIFI_SLEEP_POLICY_NEVER);
    }



    @Override
    public void onTerminate() {
        Log.i("CollectFeesAll","app onTerminate");
        //releaseWakeLock();
        super.onTerminate();
    }

    public static Context getAppContext() {
        return sInstance;
    }

    /**
     * 初始化当前设备屏幕宽高
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }
}

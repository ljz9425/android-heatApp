package com.whisht.heatapp.view.base;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.os.Handler;

import com.whisht.heatapp.utils.CommonUtils;

import java.util.List;


public abstract class BaseService extends Service {
    protected String TAG = getClass().getSimpleName();
    protected Handler handler = new Handler();

    //在进程中去寻找当前APP的信息，判断是否在前台运行
    private boolean isAppOnForeground() {
        android.app.ActivityManager activityManager =(android.app.ActivityManager)getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName =getApplicationContext().getPackageName();
        List<android.app.ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    protected void showToastMsg(String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isAppOnForeground()) {
                    CommonUtils.showToastMsg(BaseService.this, msg);
                }
            }
        });
    };
}

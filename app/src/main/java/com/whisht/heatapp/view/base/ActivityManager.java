package com.whisht.heatapp.view.base;

import android.app.Activity;

import com.whisht.heatapp.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.Stack;

public class ActivityManager {
    private String TAG = this.getClass().getSimpleName();
    private static ActivityManager instance;
    private Stack<Activity> activityStack;//activity栈
    private WeakReference<Activity> sCurrentActivityWeakRef;
    private ActivityManager() {
    }
    //单例模式
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

    //把一个activity压入栈中
    public void pushOneActivity(Activity actvity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(actvity);
        LogUtils.d(TAG,"size = " + activityStack.size());
    }
    //获取栈顶的activity，先进后出原则
    public Activity getLastActivity() {
        return activityStack.lastElement();
    }
    //移除一个activity
    public void popOneActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
                activity = null;
            }
        }
    }
    //退出所有activity
    public void finishAllActivity() {
        if (activityStack != null) {
            boolean isServiceStop = false;
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null) break;
                popOneActivity(activity);
            }
        }
    }
}

package com.whisht.heatapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.hardware.camera2.CameraManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.whisht.heatapp.view.base.BaseApplication;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.TELEPHONY_SERVICE;

public class CommonUtils {
    private static boolean isHasVKey = false;
    private static boolean isCheckVK = false;
    public static boolean isHasVKey(){
        if(!isCheckVK){
            isHasVKey = checkDeviceHasNavigationBar();
            isCheckVK = true;
        }
        return isHasVKey;
    }
    /**
     * imageview ????????????
     * @param iv
     * @param isGray true ?????? false ??????
     */
    public static void imageViewGray(ImageView iv, boolean isGray){
        ColorMatrixColorFilter filter = null;
        if(isGray) {
            //???ImageView????????????
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(isGray ? 0 : 50);//????????? 0?????? 100???????????????50??????
            filter = new ColorMatrixColorFilter(matrix);
        }
        iv.setColorFilter(filter);
    }
    private static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = BaseApplication.getContext().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.e("HBAndroidUtil",e.getMessage(),e);
        }
        return hasNavigationBar;
    }
    /**
     * ????????????????????????
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (TextUtils.isEmpty(ServiceName)) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
//            System.out.println(runningService.get(i).service.getClassName());
            if (runningService.get(i).service.getClassName().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getIMEI(Context context) {
        String imei = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        //????????????SIM????????????
        int phoneCount = tm.getPhoneCount();
        //????????????SIM?????????
        @SuppressLint("MissingPermission")
        int activeSubscriptionInfoCount = SubscriptionManager.from(context).getActiveSubscriptionInfoCount();
        @SuppressLint("MissingPermission")
        List<SubscriptionInfo> activeSubscriptionInfoList = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
        if(activeSubscriptionInfoList == null){
            return imei;
        }
        for(SubscriptionInfo subInfo : activeSubscriptionInfoList){
            try {
                Method method = tm.getClass().getMethod("getImei",int.class);
                imei = (String) method.invoke(tm,subInfo.getSimSlotIndex());
                if(!TextUtils.isEmpty(imei))
                    break;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        return imei;
    }

    /**
     * ???????????????????????????debug??????
     */
    public static boolean isApkInDebug() {
        try {
            ApplicationInfo info = BaseApplication.getContext().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> Map<String,String> convertBaseEntityToMap(T t){
        return convertBaseEntityToMap(t,false);
    }
    public static <T> Map<String,String> convertBaseEntityToMap(T t, boolean isEmpty){
        Map<String,String> result = new HashMap<String,String>();
        Class c = t.getClass();
        Field[] fields = c.getDeclaredFields();
        for(Field f : fields) {
            String name = f.getName();
//            String type = f.getGenericType().toString();
            f.setAccessible(true);
            try {
                if(f.get(t) == null || "".equals(f.get(t).toString())) {
                    if(isEmpty)
                        result.put(name.toUpperCase(), "");
                }
                else
                    result.put(name.toUpperCase(), f.get(t).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    /**
     * ??????app?????????
     * @return
     */
    public static String getAppVersionName(){
        try {
            PackageManager manager = BaseApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(BaseApplication.getContext().getPackageName(),
                    0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * ????????????????????????
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context)
    {
        // ????????????????????????????????????????????????wi-fi,net?????????????????????
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null){
            return false;
        }
        else{
            // ??????NetworkInfo??????
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    // ?????????????????????????????????????????????
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * ????????????
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath){
        File file = new File(filePath);
        if(file.exists())
            return file.delete();
        return false;
    }

    /**
     * ??????
     * @return
     */
    public static String getSystemFactory(){
        return android.os.Build.BRAND;
    }

    /**
     * ??????
     * @return
     */
    public static String getSystemMode(){
        return android.os.Build.MODEL;
    }

    /**
     * ??????android??????
     * @return
     */
    public static String getSystemVersion(){
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * ????????????
     * @return
     */
    public static String getDeviceName(){
        return android.os.Build.DEVICE;
    }

    /**
     * ???????????????
     * @param context
     * @param isOn
     */
    public static void setSpeek(Context context,boolean isOn){
        if(isOn){
            ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).setSpeakerphoneOn(true);
//            ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).setMode(AudioManager.MODE_NORMAL);
        }
        else{
            ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).setSpeakerphoneOn(false);
//            ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).setMode(AudioManager.MODE_IN_COMMUNICATION);
        }
    }

    /***
     * ??????????????????
     */
    public static int getCameraCount(Context context) {
        try {
            CameraManager manager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
            return  manager.getCameraIdList().length;
        } catch (Exception e) {
            // TODO handle error properly or pass it on
            LogUtils.e("Open CameraError???",e.getMessage(),e);
            return 0;
        }
    }
    /**
     * ???????????????????????????
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isInCall(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int state = mTelephonyManager.getCallState();
        return state != TelephonyManager.CALL_STATE_IDLE;
    }

    /**
     * ??????WIFI??????????????????
     * @param context
     * @return
     */
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * ??????????????????????????????
     * @param context
     * @return
     */
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * ???????????????????????????????????????
     * @param context
     * @return ConnectivityManager.
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    public static Map<String,Integer> getPermissionOp(){
        Map<String,Integer> ops = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager manager = (AppOpsManager)BaseApplication.getContext().getSystemService(Context.APP_OPS_SERVICE);
            Field[] f = manager.getClass().getDeclaredFields();

            for (Field field : f) {
//                System.out.println("))))))))))))"+field.getName()+":"+field.getType());
                if(field.getType() != int.class) {
                    continue;
                }
                if(!Modifier.isStatic(field.getModifiers()))
                    continue;
                field.setAccessible(true);
                try {
                    ops.put(field.getName(),field.getInt(manager));
//                    System.out.println("public static final int "+field.getName()+" = "+field.get(manager) + ";");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return ops;
    }

    /**
     * ??????????????????????????????????????????
     * @param context
     * @return
     */
    public static boolean isLight(Context context){
        //???????????????????????????
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isInteractive();
    }
    /**
     * ??????????????????
     * @param context
     * @return
     */
    public static boolean isLock(Context context){
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return km.isKeyguardLocked();
    }
    /**
     * ??????????????????
     * @param context
     */
    public static void requireUnLock(Context context){
        //??????????????????????????????
        KeyguardManager km= (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //??????
        kl.disableKeyguard();
    }
    public static void requireLock(Context context){
        //??????????????????????????????
        KeyguardManager km= (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //??????
        kl.reenableKeyguard();
    }
    public static int[] getScreenSize(Activity activity){
        int[] size = new int[2];
        Rect r = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        size[0] = r.right;
        size[1] = r.bottom;
        return size;
    }
    public static void showToastMsg(Context context,String msg){
        Toast toast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM,0,300);
        toast.show();
    }
    public static int[] getViewVisableSize(View v){
        int[] size = new int[2];
        Rect r = new Rect();
        v.getWindowVisibleDisplayFrame(r);
        size[0] = r.right;
        size[1] = r.bottom;
        return size;
    }
    //????????????
    public static void wakeUp(Context context){
        //???????????????????????????
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //??????PowerManager.WakeLock??????,???????????????|???????????????????????????,????????????LogCat?????????Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, "com.hanbon.collectfeesv1:bright");
        //????????????
        wl.acquire();
        //??????
        wl.release();
    }
    public static void printStaticInt(Class c){
        Field[] f = c.getDeclaredFields();
        for (Field field : f) {
            if(field.getType() != int.class) {
                continue;
            }
            if(!Modifier.isStatic(field.getModifiers()))
                continue;
            field.setAccessible(true);
//            try {
////                System.out.println("public static final int "+field.getName()+" = "+field.get(c) + ";");
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
        }
    }
    public static void printPermissionOp(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager manager = (AppOpsManager)BaseApplication.getContext().getSystemService(Context.APP_OPS_SERVICE);
            Field[] f = manager.getClass().getDeclaredFields();

            for (Field field : f) {
                if(field.getType() != int.class) {
                    continue;
                }
                if(!Modifier.isStatic(field.getModifiers()))
                    continue;
                field.setAccessible(true);
//                try {
////                    System.out.println("public static final int "+field.getName()+" = "+field.get(manager) + ";");
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(int op){
        int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager)BaseApplication.getContext().getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class managerClass = manager.getClass();
                Method method = managerClass.getDeclaredMethod(
                        "checkOp",
                        int.class,
                        int.class,
                        String.class
                );
                method.setAccessible(true);
                int isAllowNum =
                        (int)method.invoke(manager, op, Binder.getCallingUid(), BaseApplication.getContext().getPackageName());

                return AppOpsManager.MODE_ALLOWED == isAllowNum;
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isActivityTop(Context context,Class c){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
        if(taskInfos.size() <= 0)
            return false;
        ComponentName cn = taskInfos.get(0).topActivity;
        if(cn == null)
            return false;
        return cn.getClassName().equalsIgnoreCase(c.getName());
    }

    /**
     * ???????????????????????????
     * @param context
     * @return
     */
    public static boolean isApkOnForeground(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        boolean isForeground = false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED) {
                    isForeground = false;
                } else if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    isForeground = true;
                } else {
                    isForeground = false;
                }
            }
        }
        Log.i("HBAndroidUtil","app run on foreground:"+isForeground);
        return isForeground;
    }

    /**
     * ????????????????????????
     * @param context
     * @return
     */
    public static boolean isHeadSet(Context context){
        AudioManager audioManager = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
            for (AudioDeviceInfo deviceInfo : audioDevices) {
                if (deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                        || deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                    return true;
                }
            }
            return false;
        } else {
            return audioManager.isWiredHeadsetOn();
        }
    }

    public static void playSystemRing(){
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(BaseApplication.getContext(), RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String YMD = "yyyy-MM-dd";
    public static String YM = "yyyy-MM";
    public static String Y = "yyyy";

    public static String currentDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        return sdf.format(new Date());
    }

    public static String firstDateStrForMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        return sdf.format(calendar.getTime());
    }

    public static String currentMonthStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(YM);
        return sdf.format(new Date());
    }

    public static String firstMonthStrForYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONDAY, 0);
        SimpleDateFormat sdf = new SimpleDateFormat(YM);
        return sdf.format(calendar.getTime());
    }

    public static String currentYearStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(Y);
        return sdf.format(new Date());
    }
}

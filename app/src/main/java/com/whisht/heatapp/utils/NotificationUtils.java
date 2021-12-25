package com.whisht.heatapp.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.whisht.heatapp.R;
import com.whisht.heatapp.view.activity.MidSkipActivity;
import com.whisht.heatapp.view.base.BaseApplication;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

public class NotificationUtils {
    public final static int NOTIFICATION_TYPE_TIP = -1;
    public final static int NOTIFICATION_TYPE_URL = 0;
    public final static int NOTIFICATION_TYPE_ACTIVITY = 1;
    public final static int NOTIFICATION_TYPE_DRAFT = 2;
    public final static int NOTIFICATION_TYPE_GOVER = 3;
    public final static int NOTIFICATION_TYPE_REPORT = 4;
    public final static int NOTIFICATION_TYPE_ROADINFO = 5;

    public static void checkNotificationPermission(Context context){
        AndPermission.with(context)
                .notification()
                .permission()
                .rationale(new Rationale<Void>() {
                    @Override
                    public void showRationale(Context c, Void d, RequestExecutor e) {
                        // 没有权限会调用该访问，开发者可以在这里弹窗告知用户无权限。
                        // 启动设置: e.execute();
                        // 取消启动: e.cancel();
                        Intent intent = new Intent();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
                        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                            intent.putExtra("app_package", context.getPackageName());
                            intent.putExtra("app_uid", context.getApplicationInfo().uid);
                        }else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + context.getPackageName()));
                        }
                        context.startActivity(intent);
                    }
                })
                .start();
    }

    /**
     * 发送通知工具方法，点击后中转到 {@link MidSkipActivity}
     * @param notificationType 通知类型 {@link NotificationUtil}
     * @param title 通知标题
     * @param content 通知内容
     * @param channelId 渠道id
     * @param channelName 渠道名称
     * @param data 通知数据 url地址、activity类全名
     * @param extData 附属数据 activity设置extData附属数据
     */
    public static void sendNotification(int notificationType,String title,String content,String channelId,String channelName,String data,String extData){
        Intent intent=new Intent(BaseApplication.getContext(), MidSkipActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putInt("notificationType",notificationType);
        mBundle.putString("data",data);
        mBundle.putString("extData",extData);
        intent.putExtras(mBundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.getContext(), (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        sendNotification(BaseApplication.getContext(),pendingIntent,title,content,BaseApplication.notificationIndex.getAndIncrement(),channelId,channelName);
    }

    public static void sendNotification(PendingIntent pendingIntent, String title, String content,int id, String channelId, String channelName){
        sendNotification(BaseApplication.getContext(),pendingIntent,title,content,id,channelId,channelName,NotificationManager.IMPORTANCE_HIGH);
    }
    public static void sendNotification(Context context, PendingIntent pendingIntent, String title, String content,int id, String channelId, String channelName){
        sendNotification(context,pendingIntent,title,content,id,channelId,channelName,NotificationManager.IMPORTANCE_HIGH);
    }
    /**
     * 发送系统通知
     * @param context
     * @param pendingIntent
     * @param title
     * @param content
     * @param channelId
     * @param channelName
     */
    public static void sendNotification(Context context, PendingIntent pendingIntent, String title, String content,int id, String channelId, String channelName,int importance){
        Notification notification = buildNotification(context,pendingIntent,title,content,channelId,channelName,importance);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }
    public static Notification buildNotification(String title,String content,String channelId,String channelName,Class<?> cls){
        return buildNotification(title,content,channelId,channelName,cls,true);
    }
    public static Notification buildNotification(String title,String content,String channelId,String channelName,Class<?> cls,boolean sound){
        return buildNotification(title,content,channelId,channelName,cls,sound,false);
    }
    public static Notification buildNotification(String title,String content,String channelId,String channelName,Class<?> cls,boolean sound,boolean vibration){
        Intent intent=new Intent(BaseApplication.getContext(), cls);
        Bundle mBundle = new Bundle();
        intent.putExtras(mBundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.getContext(), (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return buildNotification(BaseApplication.getContext(),pendingIntent,title,content,channelId,channelName,NotificationManager.IMPORTANCE_HIGH,sound,vibration);
    }
    public static void cancleNotification(int id){
        NotificationManager notificationManager = (NotificationManager) BaseApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
    public static Notification buildNotification(Context context, PendingIntent pendingIntent, String title, String content,String channelId, String channelName,int importance){
        return buildNotification(context,pendingIntent,title,content,channelId,channelName,importance,true);
    }
    public static Notification.Builder getNotificationBuilder(Context context, PendingIntent pendingIntent,boolean isFull, String title, String content,String channelId, String channelName,int importance){
        return getNotificationBuilder(context,pendingIntent,isFull,title,content,channelId,channelName,importance,true);
    }
    public static Notification buildNotification(Context context, PendingIntent pendingIntent, String title, String content,String channelId, String channelName,int importance,boolean sound){
        return buildNotification(context,pendingIntent,title,content,channelId,channelName,importance,sound,false);
    }
    public static Notification.Builder getNotificationBuilder(Context context, PendingIntent pendingIntent,boolean isFull, String title, String content,String channelId, String channelName,int importance,boolean sound){
        return getNotificationBuilder(context,pendingIntent,isFull,title,content,channelId,channelName,importance,sound,false);
    }
    public static Notification buildNotification(Context context, PendingIntent pendingIntent, String title, String content,String channelId, String channelName,int importance,boolean sound,boolean vibration){
        Notification notification;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, channelName, importance, notificationManager,sound);
            notification = new Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
        } else {
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(content)

                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            if(!sound){
                builder.setSound(null);
            }else{
                builder.setDefaults(Notification.DEFAULT_ALL);
            }

            notification = builder.build();
        }
        return notification;
    }
    public static Notification.Builder getNotificationBuilder(Context context, PendingIntent pendingIntent,boolean isFull, String title, String content,String channelId, String channelName,int importance,boolean sound,boolean vibration){
        Notification.Builder builder = null;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, channelName, importance, notificationManager,sound);
            builder = new Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(content)
                    .setAutoCancel(true);
            if(pendingIntent != null) {
                if (isFull) {
                    builder.setFullScreenIntent(pendingIntent, true);
                } else {
                    builder.setContentIntent(pendingIntent);
                }
            }
        } else {
            builder = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(content)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            if(!sound)
                builder.setSound(null);
        }
        builder.setOnlyAlertOnce(true);
        return builder;
    }
    @TargetApi(26)
    private static void createNotificationChannel(String channelId, String channelName, int importance, NotificationManager notificationManager,boolean isSound){
        createNotificationChannel(channelId,channelName,importance,notificationManager,isSound,false);
    }
    @TargetApi(26)
    private static void createNotificationChannel(String channelId, String channelName, int importance, NotificationManager notificationManager,boolean isSound,boolean vibration) {
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        AudioManager am = (AudioManager)BaseApplication.getAppContext().getSystemService(Context.AUDIO_SERVICE);
        final int ringerMode = am.getRingerMode();
        if (ringerMode == AudioManager.RINGER_MODE_SILENT) {
            //静音，无声音和震动
        }
        else if (ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
            notificationChannel.enableVibration(vibration);
            if(vibration) {
                long v1[] = { 0, 100, 200, 300 }; // 震动频率
                notificationChannel.setVibrationPattern(v1);
            }
            notificationChannel.enableLights(true);
        }
        else if (ringerMode == AudioManager.RINGER_MODE_NORMAL) {
            notificationChannel.enableVibration(vibration);
            if(vibration){
                long v1[] = { 0, 100, 200, 300 }; // 震动频率
                notificationChannel.setVibrationPattern(v1);
            }
            notificationChannel.enableLights(true);
            if(isSound){
                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationChannel.setSound(sound, null);
            }
        }
        if(!isSound) {
            notificationChannel.setSound(null,null);
        }
        notificationManager.createNotificationChannel(notificationChannel);
    }

}

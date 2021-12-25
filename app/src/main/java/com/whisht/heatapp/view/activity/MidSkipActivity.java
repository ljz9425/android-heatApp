package com.whisht.heatapp.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.utils.NotificationUtils;
import com.whisht.heatapp.view.base.BaseActivity;


/**
 * 通知跳转类 {@link com.whisht.heatapp.utils.NotificationUtils#sendNotification(int, String, String, String, String, String, String)}
 * @Description:(这里用一句话描述这个类的作用)
 * @author: LQY
 * @time: 2020-02-25 16:16
 */
public class MidSkipActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int notificationType = getIntent().getIntExtra("notificationType", NotificationUtils.NOTIFICATION_TYPE_TIP);
        //通知数据 url地址、activity类全名
        String data = getIntent().getStringExtra("data");
        String extData = getIntent().getStringExtra("extData");
        Intent intent = new Intent();
        if(notificationType == NotificationUtils.NOTIFICATION_TYPE_TIP){

        }else if(notificationType == NotificationUtils.NOTIFICATION_TYPE_URL){

        }else if(notificationType == NotificationUtils.NOTIFICATION_TYPE_ACTIVITY
                || notificationType==NotificationUtils.NOTIFICATION_TYPE_DRAFT
                || notificationType==NotificationUtils.NOTIFICATION_TYPE_GOVER
                || notificationType==NotificationUtils.NOTIFICATION_TYPE_REPORT
                || notificationType==NotificationUtils.NOTIFICATION_TYPE_ROADINFO
        ){
            Class c = null;
            try{
                c = Class.forName(data);
                intent.setClass(this,c);
                intent.putExtra("extData",extData);
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        finish();
    }

    @Override
    public void success(int processId, BaseResp result) {

    }
}

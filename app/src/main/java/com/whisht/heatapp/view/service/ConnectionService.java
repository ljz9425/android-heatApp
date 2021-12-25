package com.whisht.heatapp.view.service;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.view.base.BaseService;

public class ConnectionService extends BaseService {
    private static boolean isOnline  = true;
    private BasePresenter basePresenter;
    private String sessionId;
    private final static int NOTIFICATION_ID = android.os.Process.myPid();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean getOnline() {
        return isOnline;
    }
}

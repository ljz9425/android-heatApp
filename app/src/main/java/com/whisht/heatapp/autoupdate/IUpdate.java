package com.whisht.heatapp.autoupdate;

import android.net.Uri;

public interface IUpdate {
    public int isNeedUpdate();
    public void downloadApk();
    public void start();
    public void stop();
    public void installApk(Uri downloadApkUri);
}

package com.whisht.heatapp.view.receiver;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.whisht.heatapp.utils.CommonUtils;
import com.whisht.heatapp.utils.DownloadUtils;
import com.whisht.heatapp.utils.FileUtils;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;

public class DownloadBroadcastReceiver extends BroadcastReceiver {
    @Override
    @SuppressLint("NewApi")
    public void onReceive(Context context, Intent intent) {
        long myDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long reference = DownloadUtils.getUpdateDownloadId(context);
        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = downloadManager.getUriForDownloadedFile(myDownloadId);
        PackageManager packageManager = context.getPackageManager();
        String path = DownloadUtils.getRealFilePath(context, uri);
        PackageInfo info = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (null == null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            DownloadUtils.setUpdateDownloadId(context, -1);
            downloadManager.remove(reference);
        } else {
            installApk(context, uri);
        }
    }

    public void installApk(Context context, Uri uri) {
        File toInstall = new File(FileUtils.getPath(context, uri));
        AndPermission.with(context).install().file(toInstall).start();
    }

    public static File queryUpdateDownloadApk(Context context) {
        File targetApkFile = null;
        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = DownloadUtils.getUpdateDownloadId(context);
        if (-1 != downloadId) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cursor = downloadManager.query(query);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cursor.close();
            }
        }
        return targetApkFile;
    }

    private static void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        String type = getMimeType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            CommonUtils.showToastMsg(context, "没有找到打开此类文件的程序");
        }
    }

    private static String getMimeType(File file) {
        String var1 = file.getName();
        String var2 = var1.substring(var1.lastIndexOf(".") + 1, var1.length()).toLowerCase();
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(var2);
    }
}

package com.whisht.heatapp.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class DownloadUtils {
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String result = null;
        if (null == scheme) {
            result = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            result = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[] {MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (idx > -1) {
                        result = cursor.getString(idx);
                    }
                }
            }
        }
        return result;
    }

    public static String getAppVersionName(Context context) {
        try {
           return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static int getAppVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void setUpdateDownloadId(Context context, long reference) {
        SharedPreferences.Editor editor = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE).edit();
        editor.putLong("updateDownloadId", reference);
        editor.commit();
    }
    public static long getUpdateDownloadId(Context context) {
        SharedPreferences editor = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        return editor.getLong("updateDownloadId", -1);
    }
}

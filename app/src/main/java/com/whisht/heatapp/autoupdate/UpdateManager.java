package com.whisht.heatapp.autoupdate;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.whisht.heatapp.utils.CommonUtils;
import com.whisht.heatapp.utils.DownloadUtils;
import com.whisht.heatapp.utils.FileUtils;
import com.whisht.heatapp.utils.HttpUtil;
import com.whisht.heatapp.view.receiver.DownloadBroadcastReceiver;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;

public class UpdateManager implements IUpdate, Runnable {
    public interface UpdateCallback {
        void showUpdateTipDialog(boolean isMust);

        void showUpdateFailed(Exception e);

        void showInstallDialog(Uri downloadApkUri, boolean isMust);

        void showNotNeedUpdate();
    }

    private int serverVersionCode = 0;
    private UpdateCallback updateCallback = null;
    private Thread thread = null;
    private String TAG = UpdateManager.class.getName();
    private boolean working = true;
    private String checkUrl = "";
    private Activity context;
    private String tmpName;
    private String apkUrl;

    private boolean isTmpFileExist() {
        File file = DownloadBroadcastReceiver.queryUpdateDownloadApk(context);
        return file.exists();
    }

    private void deleteTmpFile(long id) {
        File file = DownloadBroadcastReceiver.queryUpdateDownloadApk(context);
        if (file != null && file.exists()) {
            file.delete();
        }
        file = DownloadBroadcastReceiver.queryUpdateDownloadApk(context);
        if (file != null && file.exists())
            file.delete();
        if (id != 0) {
            DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            downloader.remove(id);
        }
    }

    /**
     * 获取app数据路径
     *
     * @param context
     * @return
     */
    private String getDataPath(Context context, String subPath) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            cachePath = context.getExternalFilesDir(subPath).getPath();
        } else {
            //外部存储不可用
            cachePath = context.getFilesDir().getPath() + File.separator + subPath;
        }
        File f = new File(cachePath);
        if (!f.exists())
            f.mkdirs();
        return cachePath;
    }

    protected int[] getServerVersionCode(){
        if(checkUrl.equals(""))
            return new int[]{0,0};
        String version = HttpUtil.sendGet(checkUrl,null,null);
        String[] tmp = version.split("[|]");
        if(tmp.length < 1 || tmp[0].equals("")) {
            Log.e(TAG,"http get version faild");
            return new int[]{0,0};
        }
        try {
            int v = Integer.parseInt(tmp[0]);
            int n = Integer.parseInt(tmp[1]);
            return new int[]{v,n};
        }catch (Exception e){
            return new int[]{0,0};
        }
    }

    /**
     * 提示更新
     * @param isMust 是否必须更新
     */
    private void tipDownload(boolean isMust) {
        deleteTmpFile(0);
        int netType = CommonUtils.getConnectedType(context);
        if (netType == ConnectivityManager.TYPE_WIFI) {
            downloadApk();
        } else {
            if (null != updateCallback) {
                context.runOnUiThread(() -> {
                    updateCallback.showUpdateTipDialog(isMust);
                });
            }
        }
    }

    public static IUpdate newInstance(Activity context, String checkUrl, String apkUrl, String tmpAppFileName, UpdateCallback updateCallback) {
        return new UpdateManager(context, checkUrl, apkUrl, tmpAppFileName, updateCallback);
    }

    private UpdateManager(Activity context, String checkUrl, String apkUrl, String tmpName, UpdateCallback updateCallback) {
        this.checkUrl = checkUrl;
        this.context = context;
        this.tmpName = tmpName;
        this.updateCallback = updateCallback;
        this.apkUrl = apkUrl;
    }

    /**
     * 是否需要更新
     * @return 0 不需要 1 需要 -1 网络异常 2 必须升级
     */
    @Override
    public int isNeedUpdate() {
        int[] tmp = getServerVersionCode();
        serverVersionCode = tmp[0];
        if(serverVersionCode == 0)
            return -1;
        if(serverVersionCode > DownloadUtils.getAppVersionCode(context)){
            if(tmp[1] > 0)
                return 2;
            else
                return 1;
        }else{
            return 0;
        }
    }

    @Override
    public void downloadApk() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                CommonUtils.showToastMsg(context, "开始下载更新");
            }
        });
        try {
            final DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(apkUrl + tmpName.replace(".apk", "-" + serverVersionCode + ".apk"));
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.allowScanningByMediaScanner();
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setMimeType("application/vnd.android.package-archive");
            deleteTmpFile(0);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, tmpName);
            long reference = downloadManager.enqueue(request);
            DownloadUtils.setUpdateDownloadId(context, reference);
        } catch (Exception e) {
            if (null != updateCallback) {
                context.runOnUiThread(() -> {
                    updateCallback.showUpdateFailed(e);
                });
            }
        }
    }

    @Override
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void stop() {
        updateCallback = null;
        working = false;
    }

    @Override
    public void installApk(Uri downloadApkUri) {
        File toInstall = new File(FileUtils.getPath(context, downloadApkUri));
        AndPermission.with(context).install().file(toInstall).start();
    }

    @Override
    public void run() {
        int update = isNeedUpdate();
        if (0 == update) {
            if (null != updateCallback) {
                context.runOnUiThread(() -> {
                    updateCallback.showNotNeedUpdate();
                });
            }
            //不需要升级
            long id = DownloadUtils.getUpdateDownloadId(context);
            if (-1 != id) {
                //存在下载队列
                DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                Cursor cursor = downloadManager.query(query);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        deleteTmpFile(id);
                    }
                }
                cursor.close();
                DownloadUtils.setUpdateDownloadId(context, -1);
            }
        } else if (update > 0) {
            boolean isMust = update == 2;
            long id = DownloadUtils.getUpdateDownloadId(context);
            if (-1 == id) {
                context.runOnUiThread(() -> {
                    tipDownload(isMust);
                });
                return;
            }
            DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = downloadManager.query(query);
            if (null == cursor) {
                //不存在下载
                tipDownload(isMust);
            } else {
                if (cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        //已下载完成
                        final Uri uri = downloadManager.getUriForDownloadedFile(id);
                        PackageManager packageManager = context.getPackageManager();
                        String path = DownloadUtils.getRealFilePath(context, uri);
                        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                        if (null == packageInfo) {
                            //下载中没有下载信息
                            deleteTmpFile(id);
                            tipDownload(isMust);
                        } else {
                            int newVersion = packageInfo.versionCode;
                            if (serverVersionCode > newVersion) {
                                //服务器上版本比较新，删除本地，重新下载
                                deleteTmpFile(id);
                                tipDownload(isMust);
                            } else {
                                //本地下载完成的已是最新版本
                                if (newVersion > DownloadUtils.getAppVersionCode(context)) {
                                    //需要更新
                                    if (isTmpFileExist()) {
                                        if (null != updateCallback) {
                                            context.runOnUiThread(()->{updateCallback.showInstallDialog(uri, isMust);});
                                        }
                                    } else {
                                        deleteTmpFile(id);
                                        tipDownload(isMust);
                                    }
                                } else {
                                    //不需要更新，删除文件
                                    deleteTmpFile(id);
                                }
                            }
                        }
                    } else if (status != DownloadManager.STATUS_RUNNING){
                        //状态不是正在下载
                        deleteTmpFile(id);
                        tipDownload(isMust);
                    }
                } else {
                    //查找任务失败
                    deleteTmpFile(id);
                    tipDownload(isMust);
                }
                cursor.close();
            }
        }
    }

}

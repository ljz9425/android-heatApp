package com.whisht.heatapp.presenter.base;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.whisht.heatapp.R;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.database.dao.ConfigDao;
import com.whisht.heatapp.database.data.ConfigData;
import com.whisht.heatapp.entity.LoginInfo;
import com.whisht.heatapp.entity.Weather;
import com.whisht.heatapp.entity.http.resp.LoginResp;
import com.whisht.heatapp.model.base.ObjectModel;
import com.whisht.heatapp.utils.SharedPreferencesHelper;
import com.whisht.heatapp.utils.StringUtils;
import com.whisht.heatapp.view.base.BaseApplication;
import com.whisht.heatapp.view.base.IBaseView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;

public class BasePresenter {
    public interface DownloadFileCallback {
        void success();
        void fail();
    }
    protected String TAG = getClass().getSimpleName();
    protected Context mContext;
    protected ObjectModel objectModel;
    protected SharedPreferencesHelper mspHelper;
    protected IBaseView mBaseView;

    public String getDevId(){
        String devid = mspHelper.getValue("dev_id","").toString();
        //TODO 极光
//        if(StringUtils.isEmpty(devid)){
//            String rid = JPushInterface.getRegistrationID(mContext);
//            if(!StringUtils.isEmpty(rid)){
//                setDevId(rid);
//                devid = rid;
//            }
//        }
        return devid;
    }
    public void setDevId(String devId){
        mspHelper.putValue("dev_id",devId);
    }
    public BasePresenter(){
        this(null);
    }
    public BasePresenter(IBaseView baseView){
        this.mBaseView = baseView;
        this.mContext = BaseApplication.getContext();
        this.mspHelper = new SharedPreferencesHelper(mContext, mContext.getString(R.string.file_account));
        this.objectModel = new ObjectModel();
    }

    public LoginInfo getLoginInfo(){
        LoginInfo loginInfo = new LoginInfo();
        return (LoginInfo) mspHelper.getValue(SharedPreferencesHelper.MSP_USERINFO,loginInfo);
    }

    /**
     * 读取sessionId
     * @return
     */
    public String getSessionId() {
        String sessionId = (String)mspHelper.getValue(SharedPreferencesHelper.MSP_SESSION, null);
        if(!StringUtils.isEmpty(sessionId)){
            NetConstant.setSessionId(sessionId);
        }
        return sessionId;
    }

    /**
     * 读取用户ID
     * @return
     */
    public String getUserId(){
        return (String)mspHelper.getValue(SharedPreferencesHelper.MSP_USERID, null);
    }

    /**
     * 设置锁屏显示
     */
    public void setLockShow(boolean isLockVisible){
        ConfigDao dao = new ConfigDao(BaseApplication.getContext());
        ContentValues cv = new ContentValues();
        cv.put("confName",SharedPreferencesHelper.MSP_LOCKSHOW);
        cv.put("confValue",isLockVisible?"1":"0");
        if(dao.update(cv,"confName=?",new String[]{SharedPreferencesHelper.MSP_LOCKSHOW}) <= 0){
            dao.insert(cv);
        }
        dao.close();
    }

    /**
     * 是否设置锁屏显示
     * @return
     */
    public boolean isLockShow(){
        ConfigDao dao = new ConfigDao(BaseApplication.getContext());
        List<ConfigData> configDataList = dao.queryList(new String[]{"*"},"confName=?",new String[]{SharedPreferencesHelper.MSP_LOCKSHOW});
        dao.close();
        if(configDataList.size() <= 0)
            return false;
        return configDataList.get(0).getConfValue().equals("1");
    }

    /**
     * 设置自启动
     */
    public void setAutoBoot(boolean isAutoBoot){
        ConfigDao dao = new ConfigDao(BaseApplication.getContext());
        ContentValues cv = new ContentValues();
        cv.put("confName",SharedPreferencesHelper.MSP_AUTO_BOOT);
        cv.put("confValue",isAutoBoot?"1":"0");
        if(dao.update(cv,"confName=?",new String[]{SharedPreferencesHelper.MSP_AUTO_BOOT}) <= 0){
            dao.insert(cv);
        }
        dao.close();
    }

    /**
     * 是否设置锁屏显示
     * @return
     */
    public boolean isAutoBoot(){
        ConfigDao dao = new ConfigDao(BaseApplication.getContext());
        List<ConfigData> configDatas = dao.queryList(new String[]{"*"},"confName=?",new String[]{SharedPreferencesHelper.MSP_AUTO_BOOT});
        dao.close();
        if(configDatas.size() <= 0)
            return false;
        return configDatas.get(0).getConfValue().equals("1");
    }
    /**
     * 设置sessionid
     * @param loginResp
     */
    public void setSession(LoginResp loginResp) {
        mspHelper.putValue(SharedPreferencesHelper.MSP_SESSION,loginResp.getSessionId());
        mspHelper.putValue(SharedPreferencesHelper.MSP_USERID,loginResp.getUserId());
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUnitLevel(loginResp.getUnitLevel());
        loginInfo.setLoginName(loginResp.getLoginName());
        loginInfo.setSessionId(loginResp.getSessionId());
        loginInfo.setUnit(loginResp.getUnit());
        loginInfo.setUnitId(loginResp.getUnitId());
        loginInfo.setUserId(loginResp.getUserId());
        loginInfo.setUserName(loginResp.getUserName());
        loginInfo.setMapLevel(loginResp.getMapLevel());
        loginInfo.setMapCenter(loginResp.getMapCenter());
        mspHelper.putValue(SharedPreferencesHelper.MSP_USERINFO,loginInfo);
        NetConstant.setSessionId(loginResp.getSessionId());
    }
    /**
     * 检查网络是否连接
     */
    protected boolean isNetworkConnect(Context context){
        //Context context = ActivityManager.getInstance().getLastActivity();
        boolean isNetWorkConnect = isNetworkAvailable(context);
        if(!isNetWorkConnect){
            mBaseView.fail(NetConstant.CODE_SYS_EXCEPTION,NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_NET_ERROR);
        }
        return isNetWorkConnect;
    }

    /**
     * 检测当前网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public  boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public void clearSession() {
        NetConstant.setSessionId("");
        mspHelper.clear();
    }

    /**
     * GET下载
     * @param downloadUrl   下载地址
     * @return
     */
    public Subscription downloadFileByUrl(String downloadUrl, File file, final DownloadFileCallback callback){
        //调用下载app接口
        return objectModel.downloadFileByUrl(downloadUrl, file).subscribe(new Observer<InputStream>() {

            @Override
            public void onNext(InputStream inputStream) {
            }

            @Override
            public void onCompleted() {
                callback.success();
            }

            @Override
            public void onError(Throwable e) {
                callback.fail();
            }
        });
    }
}

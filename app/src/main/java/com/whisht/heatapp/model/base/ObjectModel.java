package com.whisht.heatapp.model.base;

import android.content.Intent;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.utils.FileUtils;
import com.whisht.heatapp.utils.LogUtils;
import com.whisht.heatapp.utils.StringUtils;
import com.whisht.heatapp.utils.netutils.base.NetWorkService;
import com.whisht.heatapp.utils.netutils.base.RetrofitServiceManager;
import com.whisht.heatapp.view.activity.LoginActivity;
import com.whisht.heatapp.view.base.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.whisht.heatapp.constant.NetConstant.ERR_CODE_INVALIDSESSION;

public class ObjectModel<T> {
    protected String TAG = ObjectModel.class.getSimpleName();
    protected String devId = "";
	private NetWorkService mNetworkService;
    /**
     * 构造方法，实例化接口访问类
     */
    public ObjectModel(){
        mNetworkService = RetrofitServiceManager.getInstance().create(NetWorkService.class);
    }
    /**
     * 获取接口访问实例
     * @return  接口访问实例
     */
    protected NetWorkService getNetworkService(){
        return mNetworkService;
    }

    /**
     *  设置订阅线程
     * @param observable
     * @param <T>
     * @return
     */
    private <T> Observable<T> observe(Observable<T> observable){
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    protected Observable<byte[]> heatObserve(Observable<T> observable, final boolean isBase64){
        return observe(observable).map((Func1<? super T, ? extends byte[]>) new Func1<ResponseBody, byte[]>() {
            @Override
            public byte[] call(ResponseBody body) {
                MediaType mediaType = body.contentType();
                if("image".equals(mediaType.type())){
                    try {
                        return body.bytes();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                else {
                    StringBuilder sb = new StringBuilder();
                    BaseResp baseResult = null;
                    if(isBase64){
                        baseResult = responseBase64Decode(body,sb);
                    }else{
                        baseResult = responseDecode(body,sb);
                    }
                    if(baseResult.getResultCode() == ERR_CODE_INVALIDSESSION){
                        BasePresenter basePresenter = new BasePresenter();
                        basePresenter.clearSession();
                        Intent intent = new Intent(BaseApplication.getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        BaseApplication.getContext().startActivity(intent);
                        Map<String,String> tmp = new HashMap<>();
                        tmp.put("ResultCode","1");
                        tmp.put("ResultMsg","请重新登录");
                        Gson gson = new Gson();
                        try {
                            return gson.toJson(tmp).getBytes("UTF-8");
                        }catch (Exception e){
                            return null;
                        }
                    }
                    try {
                        return sb.toString().getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        });
    }
    protected Observable<byte[]> heatObserve(Observable<T> observable){
        return heatObserve(observable,true);
    }
    private BaseResp responseBase64Decode(ResponseBody responseBody, StringBuilder sb){
        BaseResp baseResult = new BaseResp();
        try {
            String rspBase64 = responseBody.string();
            byte[] rspStrByte = Base64.decode(rspBase64, Base64.DEFAULT);
            String rspStr = new String(rspStrByte, "UTF-8");
            sb.append(rspStr);
            if (!StringUtils.isEmpty(rspStr)) {
                JsonObject returnData = new JsonParser().parse(rspStr).getAsJsonObject();
                if(returnData.has("resultCode"))
                    baseResult.setResultCode(returnData.get("resultCode").getAsInt());
                else
                    baseResult.setResultCode(Integer.parseInt(returnData.get("Code").getAsString()));
                baseResult.setResultMsg(returnData.get("resultMsg").getAsString());
            }
        }catch (Exception e){
            baseResult = null;
        }
        return baseResult;
    }
    private BaseResp responseDecode(ResponseBody responseBody, StringBuilder sb){
        BaseResp baseResult = new BaseResp();
        try {
            String rspStr = responseBody.string();
            sb.append(rspStr);
            if (!StringUtils.isEmpty(rspStr)) {
                JsonObject returnData = new JsonParser().parse(rspStr).getAsJsonObject();
                if(returnData.has("ResultCode"))
                    baseResult.setResultCode(Integer.parseInt(returnData.get("ResultCode").getAsString()));
                else
                    baseResult.setResultCode(Integer.parseInt(returnData.get("Code").getAsString()));
                if(returnData.has("ResultMsg"))
                    baseResult.setResultMsg(returnData.get("ResultMsg").getAsString());
            }
        }catch (Exception e){
            baseResult = null;
        }
        return baseResult;
    }
    protected BaseResp responseDecode(byte[] byts, Type type){
        BaseResp netResult = null;
        try {
            String rspStr = new String(byts,"UTF-8");
//            LogUtils.i(TAG,"响应回参：" + rspStr);
            if (!StringUtils.isEmpty(rspStr)) {
                Gson gson = new Gson();
                netResult = (BaseResp) gson.fromJson(rspStr, type);
            }else{
                netResult = (BaseResp) Class.forName(((Class)type).getName()).newInstance();
                netResult.setResultCode(NetConstant.CODE_SYS_EXCEPTION);
                netResult.setResultMsg(NetConstant.MSG_TIP_SYSTEM_ERROR);
            }
        }catch (Exception e){
//            LogUtils.e(TAG,"回参参数转换出错，错误信息：" + e.getMessage());
            netResult = new BaseResp();
            netResult.setResultCode(NetConstant.CODE_SYS_EXCEPTION);
            netResult.setResultMsg(NetConstant.MSG_TIP_SYSTEM_ERROR);
        }
//        LogUtils.i(TAG,"回参VO：resultCode="+ netResult.getResultCode() + "--resultDesc=" + netResult.getResultMsg());
        return netResult;
    }
    protected BaseResp responseDecode(ResponseBody responseBody, Type type){
        BaseResp netResult = null;
        try {
            String rspStr = responseBody.string();
//            LogUtils.i(TAG,"响应回参：" + rspStr);
            if (!StringUtils.isEmpty(rspStr)) {
                Gson gson = new Gson();
                netResult = (BaseResp) gson.fromJson(rspStr, type);
            }else{
                netResult = (BaseResp) Class.forName(((Class)type).getName()).newInstance();
                netResult.setResultCode(NetConstant.CODE_SYS_EXCEPTION);
                netResult.setResultMsg(NetConstant.MSG_TIP_SYSTEM_ERROR);
            }
        }catch (Exception e){
//            LogUtils.e(TAG,"回参参数转换出错，错误信息：" + e.getMessage());
            netResult = null;
            netResult.setResultCode(NetConstant.CODE_SYS_EXCEPTION);
            netResult.setResultMsg(NetConstant.MSG_TIP_SYSTEM_ERROR);
        }
//        LogUtils.i(TAG,"回参VO：resultCode="+ netResult.getResultCode() + "--resultDesc=" + netResult.getResultMsg());
        return netResult;
    }
    /**
     * 响应消息体解base64
     * @param responseBody      响应消息体
     * @param type              需要转换的结果类型
     * @return
     */
    protected BaseResp responseBase64Decode(ResponseBody responseBody, Type type){
        BaseResp netResult = null;
        try {
            String rspBase64 = responseBody.string();
//            LogUtils.i(TAG,"响应回参：" + rspBase64);
            if (!StringUtils.isEmpty(rspBase64)) {
                byte[] rspStrByte = Base64.decode(rspBase64, Base64.DEFAULT);
                String rspStr = new String(rspStrByte, "UTF-8");
//                LogUtils.i(TAG,"响应回参解base64：" + rspStr);
                Gson gson = new Gson();
                netResult = (BaseResp) gson.fromJson(rspStr, type);
            }else{
                netResult = (BaseResp) Class.forName(((Class)type).getName()).newInstance();
                netResult.setResultCode(NetConstant.CODE_SYS_EXCEPTION);
                netResult.setResultMsg(NetConstant.MSG_TIP_SYSTEM_ERROR);
            }
        }catch (Exception e){
//            LogUtils.e(TAG,"回参参数转换出错，错误信息：" + e.getMessage());
            netResult = null;
            netResult.setResultCode(NetConstant.CODE_SYS_EXCEPTION);
            netResult.setResultMsg(NetConstant.MSG_TIP_SYSTEM_ERROR);
        }
//        LogUtils.i(TAG,"回参VO：resultCode="+ netResult.getResultCode() + "--resultDesc=" + netResult.getResultMsg());
        return netResult;
    }

    /**
     * 获取公共参数转换方法实现
     * @return  公共接口转换方法实现
     */
    protected Func1<byte[], BaseResp> commonResultMap(){
        return new Func1<byte[], BaseResp>() {
            @Override
            public BaseResp call(byte[] respBody) {
                return  responseDecode(respBody, BaseResp.class);
            }
        };
    }

    /**
     * GET下载
     * @param downloadUrl
     * @return
     */
    public Observable<InputStream> downloadFileByUrl(String downloadUrl, final File file){
        return getNetworkService().downloadGetByUrl(downloadUrl)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        FileUtils.writeFile(inputStream, file);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}

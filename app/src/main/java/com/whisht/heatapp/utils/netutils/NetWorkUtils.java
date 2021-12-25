package com.whisht.heatapp.utils.netutils;

import android.util.Base64;

import com.google.gson.Gson;
import com.whisht.heatapp.entity.base.BaseReq;
import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.utils.LogUtils;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class NetWorkUtils {
    private static String devId = "";
    private static String sessionId = "";

    public static RequestBody createRequestBody() {
        return createRequestBody(null);
    }

    public static RequestBody createRequestBody(BaseReq req) {
        BasePresenter basePresenter = null;
        if (devId == null || "".equals(devId)) {
            if (basePresenter == null)
                basePresenter = new BasePresenter(null);
            devId = basePresenter.getDevId();
        }
        sessionId = req.getSessionId();

        if (sessionId == null || "".equals(sessionId)) {
            if (basePresenter == null)
                basePresenter = new BasePresenter(null);
            sessionId = basePresenter.getSessionId();
        }
        RequestBody body = null;
        try {
            if (req == null) {
                req = new BaseReq();
            }
            if (!devId.equals("")) req.setDevId(devId);
            req.setSessionId(sessionId);
            Gson gson = new Gson();
            String obj = gson.toJson(req);
            if (obj.length() < 2000) {
//                System.out.println("post请求json字符串：" + obj);
            }
            String objBase64 = Base64.encodeToString(obj.getBytes(), Base64.DEFAULT);
            if (obj.length() < 2000) {
//                System.out.println("post请求json base64后字符串：" + objBase64);
            }
            body = RequestBody.create(MediaType.parse("application/json"), objBase64);
        } catch (Exception e) {
            LogUtils.e("NetWorkUtils", e.getMessage());
        }
        return body;
    }

}

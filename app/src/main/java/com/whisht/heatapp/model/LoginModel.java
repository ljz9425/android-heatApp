package com.whisht.heatapp.model;


import com.whisht.heatapp.entity.base.BaseReq;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.req.LoginReq;
import com.whisht.heatapp.entity.http.resp.LoginResp;
import com.whisht.heatapp.model.base.ObjectModel;
import com.whisht.heatapp.utils.netutils.NetWorkUtils;

import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

public class LoginModel extends ObjectModel {

    private Func1<byte[],LoginResp> loginMap = new Func1<byte[], LoginResp>() {
        @Override
        public LoginResp call(byte[] body) {
            return (LoginResp)responseDecode(body,LoginResp.class);
        }
    };
    private Func1<byte[],BaseResp> logoutMap = new Func1<byte[], BaseResp>() {
        @Override
        public BaseResp call(byte[] body) {
            return (BaseResp)responseDecode(body,BaseResp.class);
        }
    };
    public Observable<LoginResp> login(String userName, String passWord){
        LoginReq loginReq = new LoginReq();
        loginReq.setPassWord(passWord);
        loginReq.setUserName(userName);
        RequestBody requestBody = NetWorkUtils.createRequestBody(loginReq);
        return heatObserve(getNetworkService().login(requestBody))
                .map(loginMap);
    }

    public Observable<BaseResp> logout(){
        BaseReq baseReq = new BaseReq();
        RequestBody requestBody = NetWorkUtils.createRequestBody(baseReq);
        return heatObserve(getNetworkService().logout(requestBody))
                .map(logoutMap);
    }
}

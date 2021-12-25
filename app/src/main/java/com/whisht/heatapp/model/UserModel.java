package com.whisht.heatapp.model;


import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.req.UpdatePasswordReq;
import com.whisht.heatapp.model.base.ObjectModel;
import com.whisht.heatapp.utils.netutils.NetWorkUtils;

import okhttp3.RequestBody;
import rx.Observable;

public class UserModel extends ObjectModel {

    public Observable<BaseResp> updatePassWord(String oldPassword, String newPassword){
        UpdatePasswordReq params = new UpdatePasswordReq(oldPassword,newPassword);
        RequestBody requestBody = NetWorkUtils.createRequestBody(params);
        return heatObserve(getNetworkService().updatePassWord(requestBody))
                .map(commonResultMap());
    }

}

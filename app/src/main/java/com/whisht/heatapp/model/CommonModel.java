package com.whisht.heatapp.model;

import com.whisht.heatapp.entity.base.BaseReq;
import com.whisht.heatapp.entity.http.req.DeviceReq;
import com.whisht.heatapp.entity.http.resp.DeviceResp;
import com.whisht.heatapp.entity.http.resp.IndexStatusResp;
import com.whisht.heatapp.model.base.ObjectModel;
import com.whisht.heatapp.utils.netutils.NetWorkUtils;

import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

public class CommonModel extends ObjectModel {
    private Func1<byte[], IndexStatusResp> indexStatusRespFunc1 = new Func1<byte[], IndexStatusResp>() {
        @Override
        public IndexStatusResp call(byte[] body) {
            return (IndexStatusResp) responseDecode(body, IndexStatusResp.class);
        }
    };

    public Observable<IndexStatusResp> queryIndexStatus() {
        BaseReq baseReq = new BaseReq();
        RequestBody requestBody = NetWorkUtils.createRequestBody(baseReq);
        return heatObserve(getNetworkService().queryStatusForApp(requestBody)).map(indexStatusRespFunc1);
    }
}

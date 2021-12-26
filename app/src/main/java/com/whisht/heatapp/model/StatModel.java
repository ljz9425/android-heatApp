package com.whisht.heatapp.model;

import com.whisht.heatapp.entity.StatDayInfo;
import com.whisht.heatapp.entity.http.req.DeviceReq;
import com.whisht.heatapp.entity.http.req.StatReq;
import com.whisht.heatapp.entity.http.resp.AlarmResp;
import com.whisht.heatapp.entity.http.resp.DeviceResp;
import com.whisht.heatapp.entity.http.resp.StatDayResp;
import com.whisht.heatapp.model.base.ObjectModel;
import com.whisht.heatapp.utils.StringUtils;
import com.whisht.heatapp.utils.netutils.NetWorkUtils;

import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

public class StatModel extends ObjectModel {

    private Func1<byte[], StatDayResp> statDayMap = new Func1<byte[], StatDayResp>() {
        @Override
        public StatDayResp call(byte[] body) {
            return (StatDayResp) responseDecode(body, StatDayResp.class);
        }
    };

    public Observable<StatDayResp> statDay(int pageIndex, int pageSize, String startDate, String stopDate) {
        StatReq statReq = new StatReq();
        statReq.setPageIndex(pageIndex);
        statReq.setPageSize(pageSize);
        statReq.setStartDate(startDate);
        statReq.setStopDate(stopDate);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statReq);
        return heatObserve(getNetworkService().statDay(requestBody)).map(statDayMap);
    }

    public Observable<StatDayResp> statDayList(int pageIndex, int pageSize, String unitCode, String startDate, String stopDate) {
        StatReq statReq = new StatReq();
        statReq.setPageIndex(pageIndex);
        statReq.setPageSize(pageSize);
        statReq.setUnitCode(unitCode);
        statReq.setStartDate(startDate);
        statReq.setStopDate(stopDate);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statReq);
        return heatObserve(getNetworkService().statDayList(requestBody)).map(statDayMap);
    }

    public Observable<StatDayResp> statMonth(int pageIndex, int pageSize, String startDate, String stopDate) {
        StatReq statReq = new StatReq();
        statReq.setPageIndex(pageIndex);
        statReq.setPageSize(pageSize);
        statReq.setStartDate(startDate);
        statReq.setStopDate(stopDate);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statReq);
        return heatObserve(getNetworkService().statMonth(requestBody)).map(statDayMap);
    }

    public Observable<StatDayResp> statMonthList(int pageIndex, int pageSize, String unitCode, String startDate, String stopDate) {
        StatReq statReq = new StatReq();
        statReq.setPageIndex(pageIndex);
        statReq.setPageSize(pageSize);
        statReq.setUnitCode(unitCode);
        statReq.setStartDate(startDate);
        statReq.setStopDate(stopDate);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statReq);
        return heatObserve(getNetworkService().statMonthList(requestBody)).map(statDayMap);
    }

    public Observable<StatDayResp> statYear(int pageIndex, int pageSize, String startDate, String stopDate) {
        StatReq statReq = new StatReq();
        statReq.setPageIndex(pageIndex);
        statReq.setPageSize(pageSize);
        statReq.setStartDate(startDate);
        statReq.setStopDate(stopDate);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statReq);
        return heatObserve(getNetworkService().statYear(requestBody)).map(statDayMap);
    }

    public Observable<StatDayResp> statYearList(int pageIndex, int pageSize, String unitCode, String startDate, String stopDate) {
        StatReq statReq = new StatReq();
        statReq.setPageIndex(pageIndex);
        statReq.setPageSize(pageSize);
        statReq.setUnitCode(unitCode);
        statReq.setStartDate(startDate);
        statReq.setStopDate(stopDate);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statReq);
        return heatObserve(getNetworkService().statYearList(requestBody)).map(statDayMap);
    }

}

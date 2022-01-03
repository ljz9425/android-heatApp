package com.whisht.heatapp.model;

import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.req.DeviceConfigReq;
import com.whisht.heatapp.entity.http.req.DeviceReq;
import com.whisht.heatapp.entity.http.req.DeviceStatusReq;
import com.whisht.heatapp.entity.http.resp.AlarmResp;
import com.whisht.heatapp.entity.http.resp.DeviceConfigResp;
import com.whisht.heatapp.entity.http.resp.DeviceResp;
import com.whisht.heatapp.entity.http.resp.DeviceStatusResp;
import com.whisht.heatapp.entity.http.resp.OperatorResp;
import com.whisht.heatapp.entity.http.resp.RoomResp;
import com.whisht.heatapp.model.base.ObjectModel;
import com.whisht.heatapp.utils.netutils.NetWorkUtils;

import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

public class DeviceModel extends ObjectModel {


    private Func1<byte[], BaseResp> baseMap = new Func1<byte[], BaseResp>() {
        @Override
        public BaseResp call(byte[] body) {
            return (BaseResp) responseDecode(body, BaseResp.class);
        }
    };

    private Func1<byte[], DeviceResp> deviceMap = new Func1<byte[], DeviceResp>() {
        @Override
        public DeviceResp call(byte[] body) {
            return (DeviceResp) responseDecode(body, DeviceResp.class);
        }
    };

    private Func1<byte[], DeviceStatusResp> deviceStatusMap = new Func1<byte[], DeviceStatusResp>() {
        @Override
        public DeviceStatusResp call(byte[] body) {
            return (DeviceStatusResp) responseDecode(body, DeviceStatusResp.class);
        }
    };

    private Func1<byte[], DeviceConfigResp> deviceConfigMap = new Func1<byte[], DeviceConfigResp>() {
        @Override
        public DeviceConfigResp call(byte[] body) {
            return (DeviceConfigResp) responseDecode(body, DeviceConfigResp.class);
        }
    };

    public Func1<byte[], OperatorResp> operatorMap = body -> (OperatorResp) responseDecode(body, OperatorResp.class);


    public Func1<byte[], AlarmResp> alarmMap = body -> (AlarmResp) responseDecode(body, AlarmResp.class);
    public Func1<byte[], RoomResp> roomMap = body -> (RoomResp) responseDecode(body, RoomResp.class);

    public Observable<DeviceResp> queryDeviceList(int pageIndex, int pageSize, String param) {
        DeviceReq deviceReq = new DeviceReq();
        deviceReq.setPageIndex(pageIndex);
        deviceReq.setPageSize(pageSize);
        deviceReq.setUnitCode(param);
        RequestBody requestBody = NetWorkUtils.createRequestBody(deviceReq);
        return heatObserve(getNetworkService().queryDeviceList(requestBody)).map(deviceMap);
    }

    public Observable<DeviceStatusResp> queryDeviceStatus(String unitCode) {
        DeviceStatusReq statusReq = new DeviceStatusReq(unitCode);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statusReq);
        return heatObserve(getNetworkService().queryDeviceStatus(requestBody)).map(deviceStatusMap);
    }

    public Observable<BaseResp> queryDeviceConfig(String unitCode) {
        DeviceStatusReq req = new DeviceStatusReq(unitCode);
        RequestBody requestBody = NetWorkUtils.createRequestBody(req);
        return heatObserve(getNetworkService().queryDeviceConfig(requestBody)).map(deviceConfigMap);
    }

    public Observable<BaseResp> saveDeviceConfig(DeviceConfigReq configReq) {
        RequestBody requestBody = NetWorkUtils.createRequestBody(configReq);
        return heatObserve(getNetworkService().saveDeviceConfig(requestBody)).map(baseMap);
    }

    public Observable<BaseResp> openHost(String unitCode) {
        DeviceStatusReq statusReq = new DeviceStatusReq(unitCode);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statusReq);
        return heatObserve(getNetworkService().openHost(requestBody)).map(baseMap);
    }

    public Observable<BaseResp> closeHost(String unitCode) {
        DeviceStatusReq statusReq = new DeviceStatusReq(unitCode);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statusReq);
        return heatObserve(getNetworkService().closeHost(requestBody)).map(baseMap);
    }

    public Observable<BaseResp> setTemp(String unitCode, String temp) {
        DeviceStatusReq statusReq = new DeviceStatusReq(unitCode);
        statusReq.setSetTemp(temp);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statusReq);
        return heatObserve(getNetworkService().setTemp(requestBody)).map(baseMap);
    }

    public Observable<OperatorResp> queryOperator(int pageIndex, int pageSize, String unitCode) {
        DeviceReq deviceReq = new DeviceReq();
        deviceReq.setPageIndex(pageIndex);
        deviceReq.setPageSize(pageSize);
        deviceReq.setUnitCode(unitCode);
        RequestBody requestBody = NetWorkUtils.createRequestBody(deviceReq);
        return heatObserve(getNetworkService().queryOperator(requestBody)).map(operatorMap);
    }

    public Observable<AlarmResp> queryAlarm(int pageIndex, int pageSize, String unitCode) {
        DeviceReq deviceReq = new DeviceReq();
        deviceReq.setPageIndex(pageIndex);
        deviceReq.setPageSize(pageSize);
        deviceReq.setUnitCode(unitCode);
        RequestBody requestBody = NetWorkUtils.createRequestBody(deviceReq);
        return heatObserve(getNetworkService().queryAlarm(requestBody)).map(alarmMap);
    }


    public Observable<DeviceResp> queryDeviceListForMap(String param) {
        DeviceReq deviceReq = new DeviceReq();
        deviceReq.setUnitCode(param);
        RequestBody requestBody = NetWorkUtils.createRequestBody(deviceReq);
        return heatObserve(getNetworkService().queryDeviceListForMap(requestBody)).map(deviceMap);
    }


    public Observable<RoomResp> queryRoomList(int pageIndex, int pageSize, String unitCode) {
        DeviceReq deviceReq = new DeviceReq();
        deviceReq.setPageIndex(pageIndex);
        deviceReq.setPageSize(pageSize);
        deviceReq.setUnitCode(unitCode);
        RequestBody requestBody = NetWorkUtils.createRequestBody(deviceReq);
        return heatObserve(getNetworkService().queryRoomList(requestBody)).map(roomMap);
    }

    public Observable<BaseResp> openRoom(String unitCode, String roomId, String roomName) {
        DeviceStatusReq statusReq = new DeviceStatusReq(unitCode);
        statusReq.setRoomId(roomId);
        statusReq.setRoomName(roomName);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statusReq);
        return heatObserve(getNetworkService().openRoom(requestBody)).map(baseMap);
    }

    public Observable<BaseResp> closeRoom(String unitCode, String roomId, String roomName) {
        DeviceStatusReq statusReq = new DeviceStatusReq(unitCode);
        statusReq.setRoomId(roomId);
        statusReq.setRoomName(roomName);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statusReq);
        return heatObserve(getNetworkService().closeRoom(requestBody)).map(baseMap);
    }

    public Observable<BaseResp> setRoomTemp(String unitCode, String roomId, String roomName, String temp) {
        DeviceStatusReq statusReq = new DeviceStatusReq(unitCode);
        statusReq.setRoomId(roomId);
        statusReq.setRoomName(roomName);
        statusReq.setSetTemp(temp);
        RequestBody requestBody = NetWorkUtils.createRequestBody(statusReq);
        return heatObserve(getNetworkService().setRoomTemp(requestBody)).map(baseMap);
    }

}

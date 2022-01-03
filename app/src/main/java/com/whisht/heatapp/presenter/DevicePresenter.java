package com.whisht.heatapp.presenter;

import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.http.req.DeviceConfigReq;
import com.whisht.heatapp.model.DeviceModel;
import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.view.base.IBaseView;

public class DevicePresenter extends BasePresenter {
    private String TAG = getClass().getSimpleName();
    private DeviceModel deviceModel;

    public DevicePresenter(IBaseView baseView) {
        super(baseView);
        deviceModel = new DeviceModel();
    }

    public void queryDeviceList(int pageIndex, int pageSize, String param) {
        deviceModel.queryDeviceList(pageIndex, pageSize, param).subscribe((deviceResult)-> {
            if (deviceResult.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_QUERY_DEVICE_LIST, deviceResult);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_QUERY_DEVICE_LIST, deviceResult.getResultCode(), deviceResult.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_QUERY_DEVICE_LIST, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void queryDeviceStatus(String unitCode) {
        deviceModel.queryDeviceStatus(unitCode).subscribe((deviceStatusResult)-> {
            if (deviceStatusResult.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_QUERY_DEVICE_STATUS, deviceStatusResult);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_QUERY_DEVICE_STATUS, deviceStatusResult.getResultCode(), deviceStatusResult.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_QUERY_DEVICE_STATUS, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void queryDeviceConfig(String unitCode) {
        deviceModel.queryDeviceConfig(unitCode).subscribe((result)-> {
            if (result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_QUERY_DEVICE_CONFIG, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_QUERY_DEVICE_CONFIG, result.getResultCode(), result.getResultMsg());
            }
        }, (e) -> {
            mBaseView.fail(NetConstant.APP_MSG_QUERY_DEVICE_CONFIG, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void saveDeviceConfig(DeviceConfigReq configReq) {
        deviceModel.saveDeviceConfig(configReq).subscribe((result)-> {
            if (result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_SAVE_DEVICE_STATUS, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_SAVE_DEVICE_STATUS, result.getResultCode(), result.getResultMsg());
            }
        }, (e) -> {
            mBaseView.fail(NetConstant.APP_MSG_SAVE_DEVICE_STATUS, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void openHost(String unitCode) {
        deviceModel.openHost(unitCode).subscribe((result)-> {
            if (result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_OPEN_HOST, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_OPEN_HOST, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_OPEN_HOST, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void closeHost(String unitCode) {
        deviceModel.closeHost(unitCode).subscribe((result)->{
            if (result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_CLOSE_HOST, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_CLOSE_HOST, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_CLOSE_HOST, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }


    public void setTemp(String unitCode, String temp) {
        deviceModel.setTemp(unitCode, temp).subscribe((result)->{
            if (result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_SET_TEMP, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_SET_TEMP, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_SET_TEMP, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void queryOperator(int pageIndex, int pageSize, String unitCode) {
        deviceModel.queryOperator(pageIndex, pageSize, unitCode).subscribe((result)->{
            if(result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_QUERY_OPERATOR_LOG, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_QUERY_OPERATOR_LOG, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_QUERY_OPERATOR_LOG, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void queryAlarm(int pageIndex, int pageSize, String unitCode) {
        deviceModel.queryAlarm(pageIndex, pageSize, unitCode).subscribe((result)->{
            if(result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_QUERY_ALARM, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_QUERY_ALARM, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_QUERY_ALARM, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }


    public void queryDeviceListForMap(String param) {
        deviceModel.queryDeviceListForMap(param).subscribe((deviceResult)-> {
            if (deviceResult.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_QUERY_DEVICE_LIST_FOR_MAP, deviceResult);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_QUERY_DEVICE_LIST_FOR_MAP, deviceResult.getResultCode(), deviceResult.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_QUERY_DEVICE_LIST_FOR_MAP, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }


    public void queryRoomList(int curPage, int pageSize, String unitCode) {
        deviceModel.queryRoomList(curPage, pageSize, unitCode).subscribe((result->{
            if (result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_QUERY_ROOM_LIST, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_QUERY_ROOM_LIST, result.getResultCode(), result.getResultMsg());
            }
        }), (e)->{
            mBaseView.fail(NetConstant.APP_MSG_QUERY_ROOM_LIST, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void openRoom(String unitCode, String roomId, String roomName) {
        deviceModel.openRoom(unitCode, roomId, roomName).subscribe((result)-> {
            if (result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_OPEN_ROOM, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_OPEN_ROOM, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_OPEN_ROOM, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void closeRoom(String unitCode, String roomId, String roomName) {
        deviceModel.closeRoom(unitCode, roomId, roomName).subscribe((result)->{
            if (result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_CLOSE_ROOM, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_CLOSE_ROOM, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_CLOSE_ROOM, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }


    public void setRoomTemp(String unitCode, String roomId, String roomName, String temp) {
        deviceModel.setRoomTemp(unitCode, roomId, roomName, temp).subscribe((result)->{
            if (result.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_SET_ROOM_TEMP, result);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_SET_ROOM_TEMP, result.getResultCode(), result.getResultMsg());
            }
        }, (e)->{
            mBaseView.fail(NetConstant.APP_MSG_SET_ROOM_TEMP, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }
}

package com.whisht.heatapp.view.base;

import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.utils.PermissionHelper;

public interface IBaseView extends PermissionHelper.PermissionCallBack, ILoading {
    /**
     * http成功调用
     * @param processId
     * @param result
     */
    void success(int processId, BaseResp result);

    /**
     * http失败调用
     * @param processId
     * @param errCode
     * @param errMsg
     */
    void fail(int processId, int errCode, String errMsg);

    /**
     * 提示信息
     * @param msg
     */
    void showToastMsg(String msg);

    /**
     * 获取需要授权信息,返回值为Permission.Group下
     * @return
     */
    String[][] getPermission();
}

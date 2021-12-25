package com.whisht.heatapp.entity.base;

import java.io.Serializable;

/**
 * Created by luqiye on 2018/12/8.
 */

public class BaseResp implements Serializable {
    /**
     * 发送结果 0 成功 其他失败
     */
    private int resultCode;
    //发送结果描述
    private String resultMsg;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }


}

package com.whisht.heatapp.constant;

import android.content.Context;
import android.content.res.Resources;

public class ErrCode {
    public final static int SUCCESS = 1000;
    public final static int E_NOSDCARD = 1001;

    public final static int ERR_SUCCESS = 0;
    public final static int ERR_USER_NOT_LOGIN = 2001;

    public static String getErrorInfo(Context vContext, int vType) throws Resources.NotFoundException {
        switch (vType) {
            case SUCCESS:
                return "success";
            case ERR_USER_NOT_LOGIN:
                return "用户未登录";
            default:
                return "未知错误";
        }
    }
}

package com.whisht.heatapp.constant;

public class NetConstant {

    private static String sessionId = "";

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        NetConstant.sessionId = sessionId;
    }

    //虚拟机
    public static final String SERVER_DIP1 = "39.98.49.31";//192.168.2.2";//""192.168.1.132";
    public static final String SERVER_DIP2 = "192.168.2.9";
    public static String SERVER_IP1 = "39.98.49.31";
    public static String SERVER_IP2 = "39.98.49.31";
    public static final int SERVER_PORT = 8082;
    public static final int SOCKET_PORT = 8081;
    public static final int SOCKET_HEART_TIME = 60;
    public static String SERVER_IP = "";
    public static String SERVER_URL = "";

    //region 自动更新
    //检测更新url，为一个txt文件，version|0或1   0非必须更新，1必须更新,version为整数
    public static String APP_CHECK_URL() {
        return String.format("http://%s:%d/appupdate/heatapp/heatapp.html", SERVER_IP, SERVER_PORT);
    }
    //app最终下载地址 APP_DOWNLOAD_URL+"-"+serverVersionCode+".apk"
    public static String APP_DOWNLOAD_URL() {
        return String.format("http://%s:%d/appupdate/heatapp/", SERVER_IP, SERVER_PORT);
    }
    //检测更新url，为一个txt文件，version|0或1   0非必须更新，1必须更新,version为整数
    public static String APP_CHECK_URL_DEBUG() {
        return String.format("http://%s:%d/appupdate/heatapp/heatapp.html", SERVER_IP, SERVER_PORT);
    }
    //app最终下载地址 APP_DOWNLOAD_URL+"-"+serverVersionCode+".apk"
    public static String APP_DOWNLOAD_URL_DEBUG() {
        return String.format("http://%s:%d/appupdate/heatapp/", SERVER_IP, SERVER_PORT);
    }

    //****************************funcid************************************//
    public static final byte FUN_REGISTER = 0;
    //*****************************错误码************************************//
    public static final int CODE_SYS_EXCEPTION = 9999;          //系统异常
    //操作成功
    public final static int ERR_CODE_SUCCESS = 0;
    //用户不存在
    public final static int ERR_CODE_USERNOTEXIST = 1;
    //系统错误
    public final static int ERR_CODE_SYSTEMERROR = 2;
    //用户名或密码错误
    public final static int ERR_CODE_USERPWDERROR = 3;
    //密码不能为空
    public final static int ERR_CODE_USERPWDNOTNULL = 4;
    //ajax请求，暂未处理
    public final static int ERR_CODE_AJAXNOTHANDLE = 5;
    //无效Sessionid
    public final static int ERR_CODE_INVALIDSESSION = 6;
    //参数解析失败
    public final static int ERR_CODE_INVALIDPARAMS = 7;

    //*****************************错误消息**********************************//
    public static final String MSG_TIP_SYSTEM_ERROR = "系统繁忙，请稍后重试";
    public static final String MSG_TIP_NET_ERROR = "网络连接失败";
    //*****************************消息标志**********************************//
    //登录操作
    public static final int APP_MSG_LOGIN = 0;
    //退出操作
    public static final int APP_MSG_LOGOUT = 1;
    //修改密码
    public static final int APP_UPDATE_PASSWORD = 2;
    //
    public static final int APP_MSG_QUERY_INDEX_STATUS = 100;
    public static final int APP_MSG_QUERY_DEVICE_LIST = 101;
    public static final int APP_MSG_QUERY_DEVICE_STATUS = 102;
    public static final int APP_MSG_OPEN_HOST = 103;
    public static final int APP_MSG_CLOSE_HOST = 104;
    public static final int APP_MSG_SET_TEMP = 105;
    public static final int APP_MSG_QUERY_DEVICE_CONFIG = 106;
    public static final int APP_MSG_SAVE_DEVICE_STATUS = 107;
    public static final int APP_MSG_QUERY_OPERATOR_LOG = 108;
    public static final int APP_MSG_QUERY_ALARM = 108;
    public static final int APP_MSG_QUERY_DEVICE_LIST_FOR_MAP = 109;
}

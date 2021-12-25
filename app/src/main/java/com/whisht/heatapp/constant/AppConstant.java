package com.whisht.heatapp.constant;

public class AppConstant {
    /**
     * 从服务器接收数据，并通过广播RECEIVE_ACTION发送
     */
    public static final String RECEIVE_ACTION = "com.whisht.heatapp.receive";
    /**
     * 在任何地方通知sessionid变化
     */
    public static final String SESSION_SET_ACTION = "com.whisht.heatapp.setsession";


    public final static String BROADCAST_RECEIVER_TYPE = "NOTICE_TYPE";
}
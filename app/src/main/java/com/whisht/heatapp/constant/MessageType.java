package com.whisht.heatapp.constant;

public enum MessageType {
    MSG_KEEP_ALIVE(100),
    MSG_REGISTER(101);

    //必须增加一个构造函数,变量,得到该变量的值
    private int mState = 0;

    private MessageType(int value) {
        mState = value;
    }

    /**
     * @return 枚举变量实际返回值
     */
    public int getState() {
        return mState;
    }
}

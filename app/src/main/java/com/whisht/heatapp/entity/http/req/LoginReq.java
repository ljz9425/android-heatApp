package com.whisht.heatapp.entity.http.req;


import com.whisht.heatapp.entity.base.BaseReq;

public class LoginReq extends BaseReq {
    private String userName;
    private String passWord;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}

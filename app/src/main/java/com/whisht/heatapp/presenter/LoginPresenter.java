package com.whisht.heatapp.presenter;


import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.model.LoginModel;
import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.utils.LogUtils;
import com.whisht.heatapp.view.base.IBaseView;

public class LoginPresenter extends BasePresenter {
    private String TAG = LoginPresenter.class.getSimpleName();
    private LoginModel loginModel;

    public LoginPresenter(IBaseView baseView) {
        super(baseView);
        loginModel = new LoginModel();
    }

    public void login(String userName, String passWord) {
        loginModel.login(userName, passWord).subscribe((loginResult) -> {
            if (loginResult.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_LOGIN, loginResult);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_LOGIN, loginResult.getResultCode(), loginResult.getResultMsg());
            }
        }, (e) -> {
            LogUtils.e(TAG, "登录接口调用失败，异常信息：" + e.getMessage(), e);
            mBaseView.fail(NetConstant.APP_MSG_LOGIN, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }

    public void logout() {
        loginModel.logout().subscribe((logoutResult)->{
            if (logoutResult.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                mBaseView.success(NetConstant.APP_MSG_LOGOUT, logoutResult);
            } else {
                mBaseView.fail(NetConstant.APP_MSG_LOGOUT, logoutResult.getResultCode(), logoutResult.getResultMsg());
            }
        }, (e)->{
            LogUtils.e(TAG,"注销接口调用失败，异常信息：" + e.getMessage(),e);
            mBaseView.fail(NetConstant.APP_MSG_LOGOUT, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
        });
    }


}

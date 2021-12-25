package com.whisht.heatapp.presenter;

import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.model.LoginModel;
import com.whisht.heatapp.model.UserModel;
import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.view.base.IBaseView;

import rx.functions.Action1;

public class UserPresenter  extends BasePresenter {
    private String TAG = UserPresenter.class.getSimpleName();
    private UserModel userModel;

    public UserPresenter(IBaseView baseView) {
        super(baseView);
        userModel = new UserModel();
    }

    public void updatePassWord(String oldPassword, String newPassword) {
        userModel.updatePassWord(oldPassword, newPassword).subscribe(new Action1<BaseResp>() {
            @Override
            public void call(BaseResp baseResp) {
                if (baseResp.getResultCode() == NetConstant.ERR_CODE_SUCCESS) {
                    mBaseView.success(NetConstant.APP_UPDATE_PASSWORD, baseResp);
                } else {
                    mBaseView.fail(NetConstant.APP_UPDATE_PASSWORD, baseResp.getResultCode(), baseResp.getResultMsg());
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mBaseView.fail(NetConstant.APP_UPDATE_PASSWORD, NetConstant.CODE_SYS_EXCEPTION, NetConstant.MSG_TIP_SYSTEM_ERROR);
            }
        });
    }
}

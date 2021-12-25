package com.whisht.heatapp.view.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import com.whisht.heatapp.R;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.presenter.LoginPresenter;
import com.whisht.heatapp.presenter.UserPresenter;
import com.whisht.heatapp.utils.StringUtils;
import com.whisht.heatapp.view.activity.LoginActivity;
import com.whisht.heatapp.view.base.BaseActivity;
import com.whisht.heatapp.view.base.HeatBaseView;

import butterknife.BindView;
import butterknife.OnClick;


public class UpdatePassWordActivity extends BaseActivity implements HeatBaseView.TitleOnClickListener  {

    @BindView(R.id.et_oldPassword)
    EditText et_oldPassword;
    @BindView(R.id.et_newPassword)
    EditText et_newPassword;
    @BindView(R.id.et_enterNewPassword)
    EditText et_enterNewPassword;

    UserPresenter userPresenter;
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_update_password);

        baseView.onAfterCreate();
        baseView.showTitleBackOperator(true);
        baseView.setTitleOnClickListener(this,"密码修改");

        userPresenter = new UserPresenter(this);
        loginPresenter = new LoginPresenter(this);
    }

    @Override
    public void success(int processId, BaseResp result) {
        BaseResp br = (BaseResp)result;
        switch (processId){
            case NetConstant.APP_UPDATE_PASSWORD:
                dismissLoading();
                et_oldPassword.setText("");
                et_newPassword.setText("");
                et_enterNewPassword.setText("");
                showToastMsg(br.getResultMsg());
                loginPresenter.logout();
                break;
            case NetConstant.APP_MSG_LOGOUT:
                loginPresenter.clearSession();
                Intent intent = new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {
        if (processId == NetConstant.APP_UPDATE_PASSWORD){
            dismissLoading();
        }
        showToastMsg(errMsg);
    }

    @OnClick({R.id.btn_save})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                String oldPassword = et_oldPassword.getText()+"";
                String newPassword = et_newPassword.getText()+"";
                if(StringUtils.isEmpty(oldPassword)
                        ||StringUtils.isEmpty(newPassword)
                        ||StringUtils.isEmpty(et_enterNewPassword.getText()+"") ){
                    showToastMsg("请输入全修改信息");
                    return;
                }else if(!newPassword.equals(et_enterNewPassword.getText()+"")){
                    showToastMsg("两次输入的密码不一致");
                    return;
                }else if(newPassword.equals(oldPassword)){
                    showToastMsg("新旧密码不能一样");
                    return;
                }
                userPresenter.updatePassWord(oldPassword,newPassword);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onNextClick() {

    }
}

package com.whisht.heatapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.whisht.heatapp.R;
import com.whisht.heatapp.constant.AppConstant;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.resp.LoginResp;
import com.whisht.heatapp.presenter.LoginPresenter;
import com.whisht.heatapp.utils.CommonUtils;
import com.whisht.heatapp.utils.SoftKeyBoardListener;
import com.whisht.heatapp.utils.StringUtils;
import com.whisht.heatapp.view.base.BaseActivity;
import com.yanzhenjie.permission.runtime.Permission;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    private LoginPresenter loginPresenter;
    @BindView(R.id.versionEdit)
    TextView versionEdit;
    @BindView(R.id.loginUserName)
    EditText etUserName;
    @BindView(R.id.loginPasswd)
    EditText etPassWord;
    @BindView(R.id.activityLoginView)
    View activityLoginView;

    private SoftKeyBoardListener softKeyBoardListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter = new LoginPresenter(baseView);
//        //判断是否已登录，跳转主页
        if (!StringUtils.isEmpty(loginPresenter.getSessionId())) {
            jumpToMain();
            return;
        }

        setContentView(R.layout.activity_login);
        baseView.onAfterCreate();
//        ButterKnife.bind(this);
        String ver = "版本号：" + CommonUtils.getAppVersionName();
        if (CommonUtils.isApkInDebug()) {
            etUserName.setText("admin");
            etPassWord.setText("admin@123");
            ver += " debug";
        }
        versionEdit.setText(ver);

//        Intent intent = new Intent(this, ConnectionService.class);
//        stopService(intent);
        softKeyBoardListener = new SoftKeyBoardListener(this, R.id.login_button, R.id.activityLoginView);
        softKeyBoardListener.setOnKeyBoardStatusChangeListener(new SoftKeyBoardListener.OnKeyBoardStatusChangeListener() {
            @Override
            public void OnKeyBoardPop(int offset) {
                activityLoginView.setVisibility(View.GONE);
            }

            @Override
            public void OnKeyBoardClose(int offset) {
                activityLoginView.setVisibility(View.VISIBLE);
            }
        });
        softKeyBoardListener.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        softKeyBoardListener.onResume();
    }

    private void jumpToMain() {
        Intent intent = new Intent(this,MainActivity.class);
        LoginActivity.this.startActivity(intent);
        this.finish();
    }

    @OnClick(R.id.login_button)
    void OnLoginClick(View v){
        if ("".equals(etUserName.getText().toString())) {
            showToastMsg("请输入用户名");
            etUserName.requestFocus();
            return;
        }
        if ("".equals(etPassWord.getText().toString())) {
            showToastMsg("请输入密码");
            etPassWord.requestFocus();
            return;
        }
        loginPresenter.login(etUserName.getText().toString(), etPassWord.getText().toString());
    }

    @Override
    public String[][] getPermission() {
        return new String[][]{
                Permission.Group.STORAGE,
                new String[]{}
        };
    }

    @Override
    protected void onDestroy() {
        if (null != softKeyBoardListener) {
            softKeyBoardListener.onDestory();
        }
        super.onDestroy();
    }
    @Override
    public void OnGranted() {
        super.OnGranted();
    }

    @Override
    public void OnGranted(int sign) {

    }

    @Override
    public void OnExit(boolean isMustNeed) {
        super.OnExit(isMustNeed);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void success(int processId, BaseResp result) {
        LoginResp resp = (LoginResp)result;
        loginPresenter.setSession(resp);
        Intent intent = new Intent();
        intent.setAction(AppConstant.SESSION_SET_ACTION);
        intent.putExtra("sessionId", resp.getSessionId());
        sendBroadcast(intent);
        jumpToMain();
    }

}

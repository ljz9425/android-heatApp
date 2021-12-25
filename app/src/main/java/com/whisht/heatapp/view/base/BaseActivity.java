package com.whisht.heatapp.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.whisht.heatapp.R;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.utils.PermissionHelper;
import com.whisht.heatapp.view.dialog.LoadingDialog;
import com.yanzhenjie.permission.AndPermission;

public abstract class BaseActivity extends Activity implements IBaseView, IDataHandler {

    protected long scoreDelayTime = 4000;
    protected String TAG = this.getClass().getSimpleName();
    protected HeatBaseView baseView = new HeatBaseView() {

        @Override
        protected Activity getActivity() {
            return BaseActivity.this;
        }

        @Override
        public void success(int processId, BaseResp result) {
            BaseActivity.this.success(processId, result);
        }

        @Override
        public void fail(int processId, int errCode, String errMsg) {
            BaseActivity.this.fail(processId, errCode, errMsg);
        }

        @Override
        public String[][] getPermission() {
            return BaseActivity.this.getPermission();
        }

        @Override
        public void OnGranted() {
            isReqPer.compareAndSet(true,false);
            BaseActivity.this.OnGranted();
        }

        @Override
        public void OnGranted(int sign) {
            isReqPer.compareAndSet(true, false);
            BaseActivity.this.OnGranted(sign);
        }

        @Override
        public void OnExit(boolean isMustNeed) {
            isReqPer.compareAndSet(true,false);
            BaseActivity.this.OnExit(isMustNeed);
        }

        @Override
        public void OnExit(boolean isMustNeed, int sign) {
            isReqPer.compareAndSet(true,false);
            BaseActivity.this.OnExit(isMustNeed,sign);
        }
    };


    private LoadingDialog loadingDialog;

    @Override
    public void showLoading() {
        if (null == loadingDialog) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (null != loadingDialog) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {
        showToastMsg(errMsg);
        dismissLoading();
    }

    public void showToastMsg(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, -600);
        toast.show();
    }

    /**
     * 需要获取的权限
     * @return 返回值为Permission.Group下
     */
    public String[][] getPermission(){
        return new String[0][];
    }
    public void showToastMsg(String msg){
        baseView.showToastMsg(msg);
    }
    public void showToastMsg(String msg,int dur){
        baseView.showToastMsg(msg,dur);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseView.onCreate(savedInstanceState);
    }
    @Override
    protected void onResume() {
        baseView.onResume();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        baseView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void OnExit(boolean isMustNeed) {
        if(isMustNeed)
            finish();
    }
    /**
     * 授权成功
     */
    public void OnGranted(){
    }
    @Override
    public void OnGranted(int sign) {

    }
    @Override
    public void OnExit(boolean isMustNeed, int sign) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PermissionHelper.REQ_CODE_PERMISSION:
                if (AndPermission.hasPermissions(this, getPermission())) {
                    // 有对应的权限
                    OnGranted();
                } else {
                    // 没有对应的权限
                    baseView.requestPermission();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 设置标题
     * @param title
     */
    protected void withTitle(String title) {
        TextView titleView = findViewById(R.id.titleName);
        if(titleView != null) {
            titleView.setText(title);
        }
    }

    /**
     * 标题右功能
     * @param clickListener
     * @param func
     */
    protected void withFun(View.OnClickListener clickListener, String func){
        LinearLayout ll = findViewById(R.id.llTitleNext);
        ll.setVisibility(View.VISIBLE);
        ll.setOnClickListener(clickListener);
        TextView tv = findViewById(R.id.titleNext);
        tv.setText(func);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 设置是否包含返回按钮
     * @param isHas
     */
    protected void withBackBtn(boolean isHas){
        LinearLayout lb = findViewById(R.id.llTitleBack);
        ImageView btn = findViewById(R.id.titleBack);
        lb.setVisibility(isHas? View.VISIBLE:View.INVISIBLE);
        if(isHas){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity.this.onBackPressed();
                }
            });
        }
    }

}

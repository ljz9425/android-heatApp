package com.whisht.heatapp.view.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.whisht.heatapp.R;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.utils.PermissionHelper;
import com.whisht.heatapp.utils.RomUtil;
import com.whisht.heatapp.utils.StringUtils;
import com.whisht.heatapp.view.dialog.LoadingDialog;
import com.yanzhenjie.permission.AndPermission;

public class BaseFragmentActivity extends FragmentActivity implements IBaseView, IDataHandler {
    protected HeatBaseView baseView = new HeatBaseView() {
        @Override
        protected Activity getActivity() {
            return BaseFragmentActivity.this;
        }

        @Override
        public void success(int processId, BaseResp result) {
            BaseFragmentActivity.this.success(processId, result);
        }

        @Override
        public void fail(int processId, int errCode, String errMsg) {
            isReqPer.compareAndSet(true,false);
            BaseFragmentActivity.this.fail(processId, errCode, errMsg);
        }

        @Override
        public String[][] getPermission() {
            return BaseFragmentActivity.this.getPermission();
        }

        @Override
        public void OnGranted() {
            isReqPer.compareAndSet(true,false);
            BaseFragmentActivity.this.OnGranted();
        }

        @Override
        public void OnGranted(int sign) {
            isReqPer.compareAndSet(true,false);
            BaseFragmentActivity.this.OnGranted(sign);
        }

        @Override
        public void OnExit(boolean isMustNeed) {
            isReqPer.compareAndSet(true,false);
            BaseFragmentActivity.this.OnExit(isMustNeed);
        }

        @Override
        public void OnExit(boolean isMustNeed, int sign) {
            isReqPer.compareAndSet(true,false);
            BaseFragmentActivity.this.OnExit(isMustNeed,sign);
        }
    };

    public HeatBaseView getBaseView() { return  baseView; }

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseView.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        baseView.onResume();
    }

    @Override
    protected void onDestroy() {
        baseView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PermissionHelper.REQ_CODE_PERMISSION:
                baseView.getIsReqPer().compareAndSet(true, false);
                if (AndPermission.hasPermissions(this, getPermission())) {
                    OnGranted();
                } else {
                    baseView.requestPermission();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void success(int processId, BaseResp result) {

    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {

    }

    @Override
    public void showToastMsg(String msg) {
        baseView.showToastMsg(msg);
    }

    @Override
    public String[][] getPermission() {
        return new String[0][];
    }

    @Override
    public void OnGranted() {

    }

    @Override
    public void OnGranted(int sign) {

    }

    @Override
    public void OnExit(boolean isMustNeed) {
        if(isMustNeed) {
            finish();
        }
    }

    @Override
    public void OnExit(boolean isMustNeed, int sign) {
        if(isMustNeed) {
            finish();
        }
    }

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

    public String isNeedShowPermission() {
        String content = "";
        return content;
    }
    public String isNeedShowVideoChatPermission(){
        String content = "";
        String per = "";
        String step = "";
        if(RomUtil.isMIMU()) {
            per = "?????????????????????????????????????????????";
            step = "1????????????????????????<br>2?????????????????????????????????????????????????????????????????????????????????????????????";
        }else if(RomUtil.isOPPO()){
            //per = "??????????????????";
            //step = "1??????????????????->???????????????->???????????????";
        }else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                if (RomUtil.isHUAWEI()) {
                    per = "??????????????????";
                    step = "1??????????????????->???????????????->???????????????";
                }
            }
        }
        if(!per.equals("")){
            content = "????????????<font color='#ff0000'>"+per+"</font>??????????????????????????????<br>";
            step = step.replace("???","<font color='#ff0000'>");
            step = step.replace("???","</font>");
            content += step;
            content += "<br>???????????????????????????????????????????????????<font color='#52b9fe'>?????????</font>,?????????????????????<font color='#dedede'>????????????</font>";
        }
        return content;
    }

    protected AlertDialog alertDialog;
    public void showAppSetting(String content, boolean isCancel) {
        if (StringUtils.isEmpty(content)) { return; }
        BasePresenter basePresenter = new BasePresenter();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("??????")
                .setMessage(Html.fromHtml(content))
                .setPositiveButton("?????????", (d, which)-> {
                    RomUtil.GoToAppSetting(this);
                    alertDialog.dismiss();
                    alertDialog = null;
                })
                .setNegativeButton("????????????", (d, which)-> {
                    basePresenter.setLockShow(true);
                    alertDialog.dismiss();
                    alertDialog = null;

                }).setCancelable(isCancel);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#52b9fe"));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
    }
}

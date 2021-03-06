package com.whisht.heatapp.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.whisht.heatapp.R;
import com.whisht.heatapp.autoupdate.IUpdate;
import com.whisht.heatapp.autoupdate.UpdateManager;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.LoginInfo;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.presenter.LoginPresenter;
import com.whisht.heatapp.presenter.UserPresenter;
import com.whisht.heatapp.utils.CommonUtils;
import com.whisht.heatapp.utils.LogUtils;
import com.whisht.heatapp.view.activity.LoginActivity;
import com.whisht.heatapp.view.activity.personal.UpdatePassWordActivity;
import com.whisht.heatapp.view.base.BaseFragment;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivityFragmentUser extends BaseFragment {

    @BindView(R.id.ll_person)
    LinearLayout ll_person;
    @BindView(R.id.ll_update_info)
    LinearLayout ll_update_info;
    @BindView(R.id.titleName)
    TextView tv_title;
    @BindView(R.id.iv_head_img)
    ImageView iv_head_img;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_unit)
    TextView tv_unit;
    @BindView(R.id.versionEdit)
    TextView versionEdit;
    @BindView(R.id.tv_is_have_new)
    TextView tv_is_have_new;

    LoginPresenter loginPresenter;
    UserPresenter userPresenter;

    int isNeedUpdate = 0;
    IsHaveNewTask isHaveNewTask;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main_user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ScreenAdapterTools.getInstance().loadView(view);
        unbinder = ButterKnife.bind(this, view);
        loginPresenter = new LoginPresenter(this);
        userPresenter = new UserPresenter(this);
        tv_title.setText(R.string.main_menu_index_title);
        iv_head_img.setImageResource(R.mipmap.ico_account);
        LoginInfo loginInfo = loginPresenter.getLoginInfo();
        tv_name.setText(loginInfo.getLoginName());

        String verStr = "??????????????????" + CommonUtils.getAppVersionName();
        if (CommonUtils.isApkInDebug()) {
            verStr += " debug";
        }
        versionEdit.setText(verStr);
        isPrepared = true;
//        lazyLoad();
        return view;
    }

    @OnClick({
            R.id.ll_person, R.id.titleBack, R.id.btn_logout, R.id.detailSettings,
            R.id.ll_xitongshezhi, R.id.devInfo, R.id.ll_update_info
    })
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.devInfo:
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                int[] size = CommonUtils.getScreenSize(getActivity());
                String content = "???????????????:" + size[0] + "*" + size[1] + "<br>";
                content += "????????????:" + Build.VERSION.SDK_INT + "<br>";
                content += "??????????????????:android" + Build.VERSION.RELEASE + "<br>";
                content += "????????????:" + Build.MANUFACTURER + "<br>";
                content += "????????????:" + Build.MODEL;
                dialog = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                        .setTitle("??????")
                        .setMessage(Html.fromHtml(content))
                        .setPositiveButton("??????", (d, which) -> {
                            dialog.dismiss();
                        })
                        .setCancelable(true).create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#52b9fe"));
                break;
            case R.id.detailSettings:
                activity.showAppSetting(activity.isNeedShowVideoChatPermission(), true);
                break;
            case R.id.ll_person:
//                Intent stationstatus = new Intent(MainActivityFragmentUser.this.getActivity(), PersonInfoActivity.class);
//                startActivity(stationstatus);
                break;
            case R.id.ll_xitongshezhi:
                Intent xitongshezhiIntent = new Intent(MainActivityFragmentUser.this.getActivity(), UpdatePassWordActivity.class);
                startActivity(xitongshezhiIntent);
                break;
            case R.id.ll_update_info:
                Log.i(TAG, "checkUpdate: start");

                if (!NetConstant.APP_CHECK_URL().equals("") && !NetConstant.APP_DOWNLOAD_URL().equals("")) {
                    if (CommonUtils.isApkInDebug()) {
                        update = UpdateManager.newInstance(this.getActivity(),
                                NetConstant.APP_CHECK_URL_DEBUG(), NetConstant.APP_DOWNLOAD_URL_DEBUG(),
                                "heatapp-debug.apk", updateCallBack);
                    } else {
                        update = UpdateManager.newInstance(this.getActivity(),
                                NetConstant.APP_CHECK_URL(), NetConstant.APP_DOWNLOAD_URL(),
                                "heatapp.apk", updateCallBack);
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    GetUpdateInfo getUpdateInfo = new GetUpdateInfo();
                    Thread thread = new Thread(getUpdateInfo);
                    thread.start();
                }
                break;
            case R.id.titleBack:
                break;
            case R.id.btn_logout:
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                dialog = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                        .setTitle("??????")
                        .setMessage("??????????????????????????????")
                        .setPositiveButton("??????", (d, which) -> {
                            dialog.dismiss();
                            loginPresenter.logout();
                        })
                        .setNegativeButton("??????", (d, which) -> {
                            dialog.dismiss();
                        }).setCancelable(false).create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#52b9fe"));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
                break;
        }

    }

    @Override
    public void lazyLoad() {
//        if (!isVisible || !isPrepared) {
//            return;
//        }
//        isHaveNewTask = new IsHaveNewTask();
//        isHaveNewTask.execute();
    }

//    @Override
//    public void init() {
//        isHaveNewTask = new IsHaveNewTask();
//        isHaveNewTask.execute();
//    }

    @Override
    public void hide() {
        if (null != isHaveNewTask && isHaveNewTask.getStatus() == AsyncTask.Status.RUNNING) {
            isHaveNewTask.cancel(true);
            isHaveNewTask = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        userPresenter.queryUserInfo();
//        scorePresenter.queryScoreTotal();
        isHaveNewTask = new IsHaveNewTask();
        isHaveNewTask.execute();
    }

    @Override
    public void OnGranted() {
//        userPresenter.queryUserInfo();
//        scorePresenter.queryScoreTotal();
        isHaveNewTask = new IsHaveNewTask();
        isHaveNewTask.execute();
    }

    @Override
    public void success(int processId, BaseResp result) {
        switch (processId) {
            case NetConstant.APP_MSG_LOGOUT:
                loginPresenter.clearSession();
                Intent intent = new Intent(getActivity(), LoginActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                break;
            default:
        }

    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {
        showToastMsg(errMsg);
    }

    @Override
    public String[][] getPermission() {
        return new String[0][];
    }

    @Override
    public void OnGranted(int sign) {

    }

    @Override
    public void OnExit(boolean isMustNeed) {

    }

    @Override
    public void OnExit(boolean isMustNeed, int sign) {

    }

    private AlertDialog dialog;
    private IUpdate update = null;
    int versionCode = 0;

    private UpdateManager.UpdateCallback updateCallBack = new UpdateManager.UpdateCallback() {
        @Override
        public void showUpdateTipDialog(boolean isMust) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityFragmentUser.this.getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                        .setTitle("??????")
                        .setMessage("?????????????????????????????????????????????")
                        .setPositiveButton("??????", (d, which) -> {
                            dialog.dismiss();
                            update.downloadApk();
                        })
                        .setCancelable(false);
                if (!isMust) {
                    dialog = builder.setNegativeButton("??????", (d, which) -> {
                        dialog.dismiss();
                    }).create();
                } else {
                    dialog = builder.create();
                }
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#52b9fe"));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void showUpdateFailed(Exception e) {
            LogUtils.e(TAG, e.getMessage(), e);
            showToastMsg("????????????");
        }

        @Override
        public void showInstallDialog(final Uri downloadFileUri, boolean isMust) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityFragmentUser.this.getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                        .setTitle("??????")
                        .setMessage("?????????????????????????????????????????????????????????")
                        .setPositiveButton("??????", (d, which) -> {
                            dialog.dismiss();
                            update.installApk(downloadFileUri);
                        })
                        .setCancelable(false);
                if (isMust) {
                    dialog = builder.create();
                } else {
                    dialog = builder.setNegativeButton("??????", (d, which) -> {
                        dialog.dismiss();
                    }).create();
                }
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#52b9fe"));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void showNotNeedUpdate() {

        }
    };


    class IsHaveNewTask extends AsyncTask<String, Integer, Void> {
        //????????????????????????
        protected Void doInBackground(String... params) {
            IUpdate isHaveNew = null;
            if (!isCancelled()) {
                if (!NetConstant.APP_CHECK_URL().equals("") && !NetConstant.APP_DOWNLOAD_URL().equals("")) {
                    if (CommonUtils.isApkInDebug()) {
                        isHaveNew = UpdateManager.newInstance(MainActivityFragmentUser.this.getActivity(),
                                NetConstant.APP_CHECK_URL_DEBUG(), NetConstant.APP_DOWNLOAD_URL_DEBUG(),
                                "heatapp_debug.apk", updateCallBack);
                    } else {
                        isHaveNew = UpdateManager.newInstance(MainActivityFragmentUser.this.getActivity(),
                                NetConstant.APP_CHECK_URL(), NetConstant.APP_DOWNLOAD_URL(),
                                "heatapp.apk", updateCallBack);
                    }
                    versionCode = isHaveNew.isNeedUpdate();
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (1 == versionCode || 2 == versionCode) {
                tv_is_have_new.setVisibility(View.VISIBLE);
            } else {
                tv_is_have_new.setVisibility(View.INVISIBLE);
            }
        }
    }

    //??????????????????????????????
    public class GetUpdateInfo implements Runnable {
        public void run() {
            isNeedUpdate = update.isNeedUpdate();
            Looper.prepare();
            switch (isNeedUpdate) {
                case 0:
                    dialog = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                            .setTitle("????????????")
                            .setMessage("???????????????????????????????????????")
                            .setPositiveButton("??????", (d, which) -> {
                                dialog.dismiss();
                            }).setCancelable(false).create();
                    break;
                case 1:
                    dialog = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                            .setTitle("????????????")
                            .setMessage("???????????????????????????????????????")
                            .setPositiveButton("??????", (d, which) -> {
                                dialog.dismiss();
                                update.start();
                            })
                            .setNegativeButton("??????", (d, which) -> {
                                dialog.dismiss();
                            }).setCancelable(false).create();
                    break;
                case -1:
                    dialog = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                            .setTitle("????????????")
                            .setMessage("??????????????????????????????")
                            .setPositiveButton("??????", (d, which) -> {
                                dialog.dismiss();
                            }).setCancelable(false).create();
                    break;
                case 2:
                    dialog = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                            .setTitle("????????????")
                            .setMessage("?????????????????????????????????")
                            .setPositiveButton("??????", (d, which) -> {
                                dialog.dismiss();
                                update.start();
                            }).setCancelable(false).create();
                    break;
                default:
                    break;
            }
            dialog.show();
            Looper.loop();
        }
    }
}

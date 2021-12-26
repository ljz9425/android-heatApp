package com.whisht.heatapp.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.whisht.heatapp.R;
import com.whisht.heatapp.autoupdate.IUpdate;
import com.whisht.heatapp.autoupdate.UpdateManager;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.presenter.IndexPresenter;
import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.utils.CommonUtils;
import com.whisht.heatapp.utils.LogUtils;
import com.whisht.heatapp.utils.NotificationUtils;
import com.whisht.heatapp.view.adapter.MainFragmentAdapter;
import com.whisht.heatapp.view.base.BaseFragment;
import com.whisht.heatapp.view.base.BaseFragmentActivity;
import com.whisht.heatapp.view.fragment.MainActivityFragmentIndex;
import com.whisht.heatapp.view.fragment.MainActivityFragmentMap;
import com.whisht.heatapp.view.fragment.MainActivityFragmentStat;
import com.whisht.heatapp.view.fragment.MainActivityFragmentUser;
import com.whisht.heatapp.view.service.ConnectionService;
import com.yanzhenjie.permission.AndPermission;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import butterknife.BindView;

public class MainActivity extends BaseFragmentActivity {

    private final String TAG = getClass().getSimpleName();
    private IndexPresenter indexPresenter;


    String loginName = "";

    /**
     * 主页底部菜单标题
     */
    private final int[] TAB_TITLES = new int[]{
            R.string.main_menu_index,
            R.string.main_menu_map,
            R.string.main_menu_stat,
            R.string.main_menu_user
    };
    /**
     * 主页底部菜单图片
     */
    private final int[] TAB_IMAGES = new int[]{
            R.drawable.tab_main_home_selector,
            R.drawable.tab_main_map_selector,
            R.drawable.tab_main_stat_selector,
            R.drawable.tab_main_user_selector
    };

    /**
     * 页卡适配器
     */
    public MainFragmentAdapter adapter;
    private BaseFragment currentFragment = null;

    @BindView(R.id.main_tablayout)
    TabLayout mainTabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;
    @BindView(R.id.fl_index_image)
    FrameLayout fl_index_image;
    @BindView(R.id.iv_index)
    ImageView iv_index;
    @BindView(R.id.iv_close)
    ImageView iv_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseView.onAfterCreate();
        initPages();
        setTabs();
        new BasePresenter().getSessionId();
        AndPermission.with(this).notification().permission().start();
        baseView.startReceiver();
        indexPresenter = new IndexPresenter(this);
        loginName = indexPresenter.getLoginInfo().getLoginName();
    }

    private void initPages() {
        adapter = new MainFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainActivityFragmentIndex());
        adapter.addFragment(new MainActivityFragmentMap());
        adapter.addFragment(new MainActivityFragmentStat());
        adapter.addFragment(new MainActivityFragmentUser());
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOffscreenPageLimit(4);
        //关联切换
        mainViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainTabLayout));
        mainTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainViewpager.setCurrentItem(tab.getPosition());
                currentFragment = adapter.getCurrentFragment(tab.getPosition());
                if(tab.getPosition()==0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currentFragment.init();
                        }
                    }, 1000);
                }else{
                    currentFragment.init();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                currentFragment.hide();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTabs() {
        for (int i = 0; i < TAB_IMAGES.length; i++) {
            TabLayout.Tab tab = mainTabLayout.newTab();
            View view = getLayoutInflater().inflate(R.layout.layout_main_item_menu, null);
            ScreenAdapterTools.getInstance().loadView(view);
            tab.setCustomView(view);
            TextView tvTitle = (TextView) view.findViewById(R.id.txt_tab);
            tvTitle.setText(TAB_TITLES[i]);
            ImageView ivTitle = (ImageView) view.findViewById(R.id.img_tab);
            ivTitle.setImageResource(TAB_IMAGES[i]);
//            if (2 == i) {
//                LinearLayout ll_govern_work_num = (LinearLayout) view.findViewById(R.id.ll_govern_work_num);
//                View num = this.getLayoutInflater().inflate(R.layout.layout_governwork_num, null);
//                ll_govern_work_num.setVisibility(View.VISIBLE);
//                ll_govern_work_num.addView(num);
//            }
            mainTabLayout.addTab(tab);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationUtils.checkNotificationPermission(this);
    }

    private IUpdate update = null;
    @Override
    public void OnGranted() {
        autoCheckUpdate();
        String content = isNeedShowPermission();
        BasePresenter basePresenter = new BasePresenter();
        if(!content.equals("")){
            if(!basePresenter.isLockShow()){
                showAppSetting(content,false);
            }
        }
        Intent intent = new Intent(this, ConnectionService.class);
        startService(intent);
    }

    private void autoCheckUpdate() {
        Log.i(TAG, "autoCheckUpdate: start");
        if(!NetConstant.APP_CHECK_URL().equals("") && !NetConstant.APP_DOWNLOAD_URL().equals("")) {
            if(CommonUtils.isApkInDebug()){
                update = UpdateManager.newInstance(this,
                        NetConstant.APP_CHECK_URL_DEBUG(), NetConstant.APP_DOWNLOAD_URL_DEBUG(),
                        "heatapp-debug.apk", updateCallback);
            }else{
                update = UpdateManager.newInstance(this,
                        NetConstant.APP_CHECK_URL(), NetConstant.APP_DOWNLOAD_URL(),
                        "heatapp.apk", updateCallback);
            }
            update.start();
        }
    }

    private AlertDialog alertDialog;
    private UpdateManager.UpdateCallback updateCallback = new UpdateManager.UpdateCallback() {
        @Override
        public void showUpdateTipDialog(boolean isMust) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                        .setTitle("提示")
                        .setMessage("有新的更新可用，是否确定更新？")
                        .setPositiveButton("确定", (d,which)->{
                            alertDialog.dismiss();
                            update.downloadApk();
                        })
                        .setCancelable(false);
                if(!isMust){
                    alertDialog = builder.setNegativeButton("取消", (d,which)->{
                        alertDialog.dismiss();
                    }).create();
                }else{
                    alertDialog = builder.create();
                }
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#52b9fe"));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void showUpdateFailed(Exception e) {
            LogUtils.e(TAG,e.getMessage(),e);
            baseView.showToastMsg("更新失败");
        }

        @Override
        public void showInstallDialog(Uri downloadApkUri, boolean isMust) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                        .setTitle("提示")
                        .setMessage("新的安装包已经下载完成，是否确定安装？")
                        .setPositiveButton("确定", (d,which)->{
                            alertDialog.dismiss();
                            update.installApk(downloadApkUri);
                        })
                        .setCancelable(false);
                if(isMust){
                    alertDialog = builder.create();
                }else{
                    alertDialog = builder.setNegativeButton("取消", (d,which)->{
                        alertDialog.dismiss();
                    }).create();
                }
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#52b9fe"));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void showNotNeedUpdate() {

        }
    };

    /**
     * 授权成功
     */
    public void OnGranted(int sign){
        if(currentFragment == null)
            return;
        currentFragment.OnGranted(sign);
    }

    private long last = 0;
    @Override
    public void onBackPressed() {
        if(currentFragment != null){
            if(currentFragment.onBackPressed()){
                return;
            }
        }
        long cur = System.currentTimeMillis();
        if(cur - last > 700){
            last = System.currentTimeMillis();
            showToastMsg("再按一次退出");
        }else{
            super.onBackPressed();
        }
    }

    /**
     * 授权失败
     */
    public void OnExit(boolean isMustNeed){
        super.OnExit(isMustNeed);
        if(currentFragment == null)
            return;
        currentFragment.OnExit(isMustNeed);
    }

    /**
     * 授权失败
     */
    public void OnExit(boolean isMustNeed,int sign){
        super.OnExit(isMustNeed,sign);
        if(currentFragment == null)
            return;
        currentFragment.OnExit(isMustNeed,sign);
    }

    @Override
    protected void onDestroy() {
        baseView.stopReceiver();
        super.onDestroy();
    }


    @Override
    public void success(int processId, BaseResp result) {
        switch (processId) {
            default:
                if (null != currentFragment) {
                    currentFragment.success(processId, result);
                }
        }
    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {
        if (null != currentFragment) {
            currentFragment.fail(processId, errCode, errMsg);
        }
    }
}
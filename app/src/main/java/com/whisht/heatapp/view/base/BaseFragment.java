package com.whisht.heatapp.view.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.whisht.heatapp.constant.AppConstant;
import com.whisht.heatapp.presenter.IndexPresenter;
import com.whisht.heatapp.view.dialog.LoadingDialog;
import com.whisht.heatapp.view.service.ConnectionService;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements IBaseView, ILoading, IDataHandler {
    protected BaseFragmentActivity activity;
    protected Unbinder unbinder;
    protected String TAG = getClass().getSimpleName();
    protected abstract int getLayoutResource();
    public IndexPresenter indexPresenter;
    protected CommonReceiver commonReceiver;
    protected View mFragmentView = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == activity) {
            activity = (BaseFragmentActivity)getActivity();
        }
        mFragmentView = inflater.inflate(getLayoutResource(), container, false);
        ScreenAdapterTools.getInstance().loadView(mFragmentView);
        unbinder = ButterKnife.bind(this, mFragmentView);
        indexPresenter = new IndexPresenter(activity);
        return mFragmentView;
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstant.BROADCAST_RECEIVER_TYPE);
        commonReceiver = new CommonReceiver();
        context.registerReceiver(commonReceiver, filter);
    }

    private class CommonReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            if (ConnectionService.getOnline()) {
                //处理接收服务器的消息
            }
        }
    };

    @Override
    public void showToastMsg(String msg) {
        activity.baseView.showToastMsg(msg);
    }

    public void showToastMsg(String msg, int duration) {
        activity.baseView.showToastMsg(msg, duration);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        if (null != commonReceiver) {
            activity.unregisterReceiver(commonReceiver);
        }
        super.onDestroyView();
    }

    private LoadingDialog loadingDialog;

    @Override
    public void showLoading() {
        if(loadingDialog == null) {
            loadingDialog = new LoadingDialog(this.getContext());
        }
        loadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if(loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * @return true fragment已经处理
     */
    public boolean onBackPressed(){
        return false;
    }
    public void init() {}
    public abstract void hide();
    public void loadMessageData(){};

}

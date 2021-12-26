package com.whisht.heatapp.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.whisht.heatapp.R;
import com.whisht.heatapp.autoupdate.IUpdate;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.AlarmInfo;
import com.whisht.heatapp.entity.DeviceInfo;
import com.whisht.heatapp.entity.OperatorInfo;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.resp.AlarmResp;
import com.whisht.heatapp.entity.http.resp.OperatorResp;
import com.whisht.heatapp.presenter.DevicePresenter;
import com.whisht.heatapp.view.adapter.AlarmItemAdapter;
import com.whisht.heatapp.view.adapter.OperatorItemAdapter;
import com.whisht.heatapp.view.base.BaseFragment;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentAlarm extends BaseFragment {

    @BindView(R.id.swipe_alarm)
    SwipeRefreshLayout swipeRefreshAlarm;
    @BindView(R.id.rv_alarm)
    RecyclerView rvAlarm;

    private String unitCode = "";
    List<AlarmInfo> alarmInfoList;
    private DevicePresenter devicePresenter;
    private AlarmItemAdapter alarmItemAdapter;
    private int lastVisibleItem;
    boolean canLoad = false;
    int curPage = 1;
    int pageSize = 20;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_alarm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ScreenAdapterTools.getInstance().loadView(view);
        unbinder = ButterKnife.bind(this, view);
        devicePresenter = new DevicePresenter(this);
        Bundle bundle = getActivity().getIntent().getExtras().getBundle("info");;
        unitCode = bundle.getString("unitCode");
        //设置下拉刷新
        swipeRefreshAlarm.setColorSchemeColors(getResources().getColor(R.color.colorLightBlue),
                getResources().getColor(R.color.colorBule));
        swipeRefreshAlarm.setOnRefreshListener(() -> intData());
        //设置列表排列方向
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mFragmentView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAlarm.setLayoutManager(mLayoutManager);

        alarmItemAdapter = new AlarmItemAdapter(mFragmentView.getContext());

        rvAlarm.setAdapter(alarmItemAdapter);
        rvAlarm.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 这里在加入判断，判断是否滑动到底部
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == alarmItemAdapter.getItemCount()) {
                    //滚动到底部，加载下一页
                    queryNextPageData();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        return view;
    }

    private void intData() {
        curPage = 1;
        canLoad = true;
        if (alarmInfoList != null && alarmInfoList.size() > 0 && null != alarmItemAdapter) {
            alarmItemAdapter.notifyItemRangeRemoved(0, alarmItemAdapter.getItemCount());
            alarmInfoList.clear();
        }
        queryNextPageData();
    }

    private void queryNextPageData() {
        if (canLoad) {
            devicePresenter.queryAlarm(curPage, pageSize, unitCode);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            intData();
        }
    }

    @Override
    public void init() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void success(int processId, BaseResp result) {
        switch (processId) {
            case NetConstant.APP_MSG_QUERY_ALARM:
                AlarmResp resp = (AlarmResp)result;
                List<AlarmInfo> infoList = resp.getDataList();
                canLoad = curPage++ < resp.getMaxPageSize();
                if (null == infoList || infoList.size() <= 0) {
                    swipeRefreshAlarm.setRefreshing(false);
                    showToastMsg("暂无数据");
                    return;
                }
                int itemPosition;
                if (null == alarmInfoList || alarmInfoList.size() <= 0) {
                    itemPosition = 0;
                    alarmInfoList = infoList;
                    alarmItemAdapter.setAlarmInfoList(alarmInfoList);
                } else {
                    itemPosition = infoList.size();
                    alarmInfoList.addAll(infoList);
                }
                alarmItemAdapter.notifyItemRangeChanged(itemPosition, alarmInfoList.size());
                //关闭下拉加载框
                swipeRefreshAlarm.setRefreshing(false);
                break;
            default:
        }

    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {
        //关闭下拉加载框
        swipeRefreshAlarm.setRefreshing(false);
        showToastMsg(errMsg);
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

    }

    @Override
    public void OnExit(boolean isMustNeed, int sign) {

    }

    private AlertDialog dialog;
    private IUpdate update = null;
    int versionCode = 0;


}

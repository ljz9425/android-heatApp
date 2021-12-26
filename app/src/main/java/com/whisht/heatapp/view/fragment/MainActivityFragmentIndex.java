package com.whisht.heatapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.whisht.heatapp.R;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.DeviceInfo;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.resp.DeviceResp;
import com.whisht.heatapp.entity.http.resp.IndexStatusResp;
import com.whisht.heatapp.presenter.CommonPresenter;
import com.whisht.heatapp.presenter.DevicePresenter;
import com.whisht.heatapp.view.activity.DetailActive;
import com.whisht.heatapp.view.adapter.DeviceItemAdapter;
import com.whisht.heatapp.view.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivityFragmentIndex extends BaseFragment {

//    private boolean autoSliderFlag = false;
//    private boolean isRunning = false;
    private boolean isGranted = false;

    @BindView(R.id.index_top_area)
    TextView tv_Area;
    @BindView(R.id.index_top_count)
    TextView tv_Count;
    @BindView(R.id.index_top_host)
    TextView tv_Host;
    @BindView(R.id.index_top_open)
    TextView tv_Open;
    @BindView(R.id.index_top_close)
    TextView tv_Close;
    @BindView(R.id.index_top_offline)
    TextView tv_Offline;

    @BindView(R.id.index_swipe_device)
    SwipeRefreshLayout swipeRefreshDevice;
    @BindView(R.id.index_rv_device)
    RecyclerView rvDevice;
    @BindView(R.id.titleName)
    TextView tv_title;

    private String param = "";
    List<DeviceInfo> deviceInfoList;
    CommonPresenter commonPresenter;
    DevicePresenter devicePresenter;
    DeviceItemAdapter deviceItemAdapter;
    private int lastVisibleItem;
    boolean canLoad = false;
    int curPage = 1;
    int pageSize = 20;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main_index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        tv_title.setText(R.string.main_menu_index_title);
        register(activity);
//        initView();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        //更新通知
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(AppConstant.BROADCASTRECEIVER_TYPE);
//        zhuYeReceiver = new ZhuYeReceiver();
//        this.getActivity().registerReceiver(zhuYeReceiver,filter);
//
        commonPresenter = new CommonPresenter(this);
        devicePresenter = new DevicePresenter(this);
        //设置下拉刷新
        swipeRefreshDevice.setColorSchemeColors(getResources().getColor(R.color.colorLightBlue),
                getResources().getColor(R.color.colorBule));
        swipeRefreshDevice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDeviceData();
            }
        });
        //设置列表排列方向
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mFragmentView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDevice.setLayoutManager(mLayoutManager);

        deviceItemAdapter = new DeviceItemAdapter(mFragmentView.getContext(), new DeviceItemAdapter.ViewDeviceClick() {
            @Override
            public void onClick(DeviceInfo deviceInfo) {
                Bundle bundle = new Bundle();
                bundle.putString("unitCode", deviceInfo.getUnitCode());
                bundle.putString("unitName", deviceInfo.getUnitName());
                //查看详情
                Intent intent = new Intent(MainActivityFragmentIndex.this.getActivity(), DetailActive.class);
                intent.putExtra("info", bundle);
                startActivity(intent);
            }
        });

        rvDevice.setAdapter(deviceItemAdapter);
        rvDevice.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 这里在加入判断，判断是否滑动到底部
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == deviceItemAdapter.getItemCount()) {
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
        initDeviceData();
        return view;
    }

    private void initDeviceData() {
        curPage = 1;
        canLoad = true;
        if (deviceInfoList != null && deviceInfoList.size() > 0) {
            deviceItemAdapter.notifyItemRangeRemoved(0, deviceItemAdapter.getItemCount());
            deviceInfoList.clear();
        }
        commonPresenter.queryIndexStatus();
        queryNextPageData();
    }

    private void queryNextPageData() {
        if (canLoad) {
            devicePresenter.queryDeviceList(curPage, pageSize, param);
        }
    }

    @OnClick({
            R.id.index_ll_count, R.id.index_ll_open, R.id.index_ll_close, R.id.index_ll_offline
    })
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.index_ll_count:
                param = "";
                break;
            case R.id.index_ll_open:
                param = "启动";
                break;
            case R.id.index_ll_close:
                param = "停机";
                break;
            case R.id.index_ll_offline:
                param = "离线";
                break;
        }
        initDeviceData();
    }

    @Override
    public void hide() {

    }

    @Override
    public void success(int processId, BaseResp result) {
        switch (processId) {
            case NetConstant.APP_MSG_QUERY_INDEX_STATUS:
                IndexStatusResp statusResp = (IndexStatusResp)result;
                tv_Area.setText(statusResp.getArea());
                tv_Count.setText(statusResp.getCount());
                tv_Host.setText(statusResp.getHost());
                tv_Open.setText(statusResp.getOpenCount());
                tv_Close.setText(statusResp.getCloseCount());
                tv_Offline.setText(statusResp.getOfflineCount());
                break;
            case NetConstant.APP_MSG_QUERY_DEVICE_LIST:
                DeviceResp deviceResp = (DeviceResp)result;
                List<DeviceInfo> infoList = deviceResp.getDataList();
                canLoad = curPage++ < deviceResp.getMaxPageSize();
                if (null == infoList || infoList.size() <= 0) {
                    swipeRefreshDevice.setRefreshing(false);
                    return;
                }
                int itemPosition;
                if (null == deviceInfoList || deviceInfoList.size() <= 0) {
                    itemPosition = 0;
                    deviceInfoList = infoList;
                    deviceItemAdapter.setDeviceInfoList(deviceInfoList);
                } else {
                    itemPosition = infoList.size();
                    deviceInfoList.addAll(infoList);
                }
//                Log.d(TAG, "deviceInfoList.size: " + deviceInfoList.size());
                deviceItemAdapter.notifyItemRangeChanged(itemPosition, deviceInfoList.size());
                //关闭下拉加载框
                swipeRefreshDevice.setRefreshing(false);
                break;
            default:
                break;
        }

    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {
        //关闭下拉加载框
        swipeRefreshDevice.setRefreshing(false);
        showToastMsg(errMsg);
    }

    @Override
    public String[][] getPermission() {
        return new String[0][];
    }

    @Override
    public void OnGranted() {
        isGranted = true;
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
}

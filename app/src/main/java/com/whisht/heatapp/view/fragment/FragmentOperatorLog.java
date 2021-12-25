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
import com.whisht.heatapp.entity.DeviceInfo;
import com.whisht.heatapp.entity.OperatorInfo;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.resp.DeviceResp;
import com.whisht.heatapp.entity.http.resp.OperatorResp;
import com.whisht.heatapp.presenter.CommonPresenter;
import com.whisht.heatapp.presenter.DevicePresenter;
import com.whisht.heatapp.view.activity.DetailActive;
import com.whisht.heatapp.view.adapter.DeviceItemAdapter;
import com.whisht.heatapp.view.adapter.OperatorItemAdapter;
import com.whisht.heatapp.view.base.BaseFragment;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentOperatorLog extends BaseFragment {

    @BindView(R.id.op_swipe_log)
    SwipeRefreshLayout swipeRefreshOperator;
    @BindView(R.id.op_rv_log)
    RecyclerView rvOperator;

    private String unitCode = "";
    List<OperatorInfo> operatorInfoList;
    private DevicePresenter devicePresenter;
    private OperatorItemAdapter operatorItemAdapter;
    private int lastVisibleItem;
    boolean canLoad = false;
    int curPage = 1;
    int pageSize = 20;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_operator_log;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ScreenAdapterTools.getInstance().loadView(view);
        unbinder = ButterKnife.bind(this, view);
        devicePresenter = new DevicePresenter(this);
        Intent intent = getActivity().getIntent();
        DeviceInfo deviceInfo = (DeviceInfo) intent.getExtras().getSerializable("info");
        if (null != deviceInfo) {
            unitCode = deviceInfo.getUnitCode();
        }
        //设置下拉刷新
        swipeRefreshOperator.setColorSchemeColors(getResources().getColor(R.color.colorLightBlue),
                getResources().getColor(R.color.colorBule));
        swipeRefreshOperator.setOnRefreshListener(() -> intData());
        //设置列表排列方向
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mFragmentView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOperator.setLayoutManager(mLayoutManager);

        operatorItemAdapter = new OperatorItemAdapter(mFragmentView.getContext());

        rvOperator.setAdapter(operatorItemAdapter);
        rvOperator.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 这里在加入判断，判断是否滑动到底部
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == operatorItemAdapter.getItemCount()) {
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
        if (operatorInfoList != null && operatorInfoList.size() > 0) {
            operatorItemAdapter.notifyItemRangeRemoved(0, operatorItemAdapter.getItemCount());
            operatorInfoList.clear();
        }
        queryNextPageData();
    }

    private void queryNextPageData() {
        if (canLoad) {
            devicePresenter.queryOperator(curPage, pageSize, unitCode);
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
            case NetConstant.APP_MSG_QUERY_OPERATOR_LOG:
                OperatorResp resp = (OperatorResp)result;
                List<OperatorInfo> infoList = resp.getDataList();
                canLoad = curPage++ < resp.getMaxPageSize();
                if (null == infoList || infoList.size() <= 0) {
                    swipeRefreshOperator.setRefreshing(false);
                    showToastMsg("暂无数据");
                    return;
                }
                int itemPosition;
                if (null == operatorInfoList || operatorInfoList.size() <= 0) {
                    itemPosition = 0;
                    operatorInfoList = infoList;
                    operatorItemAdapter.setOperatorInfoList(operatorInfoList);
                } else {
                    itemPosition = infoList.size();
                    operatorInfoList.addAll(infoList);
                }
                operatorItemAdapter.notifyItemRangeChanged(itemPosition, operatorInfoList.size());
                //关闭下拉加载框
                swipeRefreshOperator.setRefreshing(false);
                break;
            default:
        }

    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {
        //关闭下拉加载框
        swipeRefreshOperator.setRefreshing(false);
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

package com.whisht.heatapp.view.fragment;

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
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.OperatorInfo;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.resp.OperatorResp;
import com.whisht.heatapp.presenter.DevicePresenter;
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
        Bundle bundle = getActivity().getIntent().getExtras().getBundle("info");;
        unitCode = bundle.getString("unitCode");
        //??????????????????
        swipeRefreshOperator.setColorSchemeColors(getResources().getColor(R.color.colorLightBlue),
                getResources().getColor(R.color.colorBule));
        swipeRefreshOperator.setOnRefreshListener(() -> initData());
        //????????????????????????
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mFragmentView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOperator.setLayoutManager(mLayoutManager);

        operatorItemAdapter = new OperatorItemAdapter(mFragmentView.getContext());

        rvOperator.setAdapter(operatorItemAdapter);
        rvOperator.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // ???????????????????????????????????????????????????
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == operatorItemAdapter.getItemCount()) {
                    //?????????????????????????????????
                    queryNextPageData();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        isPrepared = true;
        initData();
        return view;
    }

    @Override
    public void lazyLoad() {
//        if (!isVisible || !isPrepared) {
//            return;
//        }
//        initData();
    }

    private void initData() {
        curPage = 1;
        canLoad = true;
        if (operatorInfoList != null && operatorInfoList.size() > 0 && null != operatorItemAdapter) {
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
                    if (isVisible) {
                        showToastMsg("????????????");
                    }
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
                //?????????????????????
                swipeRefreshOperator.setRefreshing(false);
                break;
            default:
        }

    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {
        //?????????????????????
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

}

package com.whisht.heatapp.view.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.whisht.heatapp.R;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.StatDayInfo;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.resp.StatDayResp;
import com.whisht.heatapp.presenter.StatPresenter;
import com.whisht.heatapp.view.adapter.AlarmItemAdapter;
import com.whisht.heatapp.view.adapter.StatDayItemAdapter;
import com.whisht.heatapp.view.base.BaseActivity;
import com.whisht.heatapp.view.base.HeatBaseView;

import java.util.List;

import butterknife.BindView;

public class StatActivity extends BaseActivity implements HeatBaseView.TitleOnClickListener  {

    @BindView(R.id.swipe_stat)
    SwipeRefreshLayout swipeRefreshStat;
    @BindView(R.id.rv_stat)
    RecyclerView rvStat;

    private String statType;
    private String unitCode;
    private String startDate;
    private String stopDate;
    private StatPresenter statPresenter;
    private StatDayItemAdapter dayItemAdapter;
    private List<StatDayInfo> dayInfoList;
    private int lastVisibleItem;
    boolean canLoad = false;
    int curPage = 1;
    int pageSize = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        baseView.onAfterCreate();
        baseView.showTitleBackOperator(true);
        Bundle bundle = this.getIntent().getExtras();
        if (null != bundle) {
            statType = bundle.getString("statType");
            unitCode = bundle.getString("unitCode");
            startDate = bundle.getString("startDate");
            stopDate = bundle.getString("stopDate");
            baseView.setTitleOnClickListener(this,bundle.getString("unitName"));
        }
        statPresenter = new StatPresenter(this);
        //设置下拉刷新
        swipeRefreshStat.setColorSchemeColors(getResources().getColor(R.color.colorLightBlue),
                getResources().getColor(R.color.colorBule));
        swipeRefreshStat.setOnRefreshListener(() -> initData());
        //设置列表排列方向
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getWindow().getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvStat.setLayoutManager(mLayoutManager);

        dayItemAdapter = new StatDayItemAdapter(this.getWindow().getContext(), false, statType.equals("day"), dayInfo -> {});

        rvStat.setAdapter(dayItemAdapter);
        rvStat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 这里在加入判断，判断是否滑动到底部
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == dayItemAdapter.getItemCount()) {
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
        initData();
    }

    private void initData() {
        curPage = 1;
        canLoad = true;
        if (dayInfoList != null && dayInfoList.size() > 0) {
            dayItemAdapter.notifyItemRangeRemoved(0, dayItemAdapter.getItemCount());
            dayInfoList.clear();
        }
        queryNextPageData();
    }

    private void queryNextPageData() {
        if (canLoad && null != statPresenter) {
            if (statType.equals("day")) {
                statPresenter.statDayList(curPage, pageSize, unitCode, startDate, stopDate);
            } else if (statType.equals("month")) {
                statPresenter.statMonthList(curPage, pageSize, unitCode, startDate, stopDate);
            } else if (statType.equals("year")) {
                statPresenter.statYearList(curPage, pageSize, unitCode, startDate, stopDate);
            }
        }
    }

    @Override
    public void success(int processId, BaseResp result) {
        switch (processId) {
            case NetConstant.APP_MSG_STAT_DAY_UNIT:
            case NetConstant.APP_MSG_STAT_MONTH_UNIT:
            case NetConstant.APP_MSG_STAT_YEAR_UNIT:
                StatDayResp resp = (StatDayResp)result;
                List<StatDayInfo> infoList = resp.getDataList();
                canLoad = curPage++ < resp.getMaxPageSize();
                if (null == infoList || infoList.size() <= 0) {
                    swipeRefreshStat.setRefreshing(false);
                    showToastMsg("暂无数据");
                    return;
                }
                int itemPosition;
                if (null == dayInfoList || dayInfoList.size() <= 0) {
                    itemPosition = 0;
                    dayInfoList = infoList;
                    dayItemAdapter.setDayInfoList(dayInfoList);
                } else {
                    itemPosition = infoList.size();
                    dayInfoList.addAll(infoList);
                }
                dayItemAdapter.notifyItemRangeChanged(itemPosition, dayInfoList.size());
                break;
        }
        //关闭下拉加载框
        swipeRefreshStat.setRefreshing(false);
    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {
        //关闭下拉加载框
        swipeRefreshStat.setRefreshing(false);
        showToastMsg(errMsg);
    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onNextClick() {

    }
}

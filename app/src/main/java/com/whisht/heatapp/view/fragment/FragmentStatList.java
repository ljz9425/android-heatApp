package com.whisht.heatapp.view.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.whisht.heatapp.entity.StatDayInfo;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.resp.StatDayResp;
import com.whisht.heatapp.presenter.StatPresenter;
import com.whisht.heatapp.utils.CommonUtils;
import com.whisht.heatapp.view.activity.DetailActive;
import com.whisht.heatapp.view.activity.StatActivity;
import com.whisht.heatapp.view.adapter.DeviceItemAdapter;
import com.whisht.heatapp.view.adapter.StatDayItemAdapter;
import com.whisht.heatapp.view.base.BaseFragment;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentStatList extends BaseFragment {

    @BindView(R.id.sdi_start)
    TextView tv_start;
    @BindView(R.id.sdi_stop)
    TextView tv_stop;
    @BindView(R.id.swipe_stat_day)
    SwipeRefreshLayout swipeRefreshStat;
    @BindView(R.id.rv_stat_day)
    RecyclerView rvStat;

    private StatPresenter statPresenter;
    private StatDayItemAdapter dayItemAdapter;
    private List<StatDayInfo> dayInfoList;
    private int lastVisibleItem;
    boolean canLoad = false;
    int curPage = 1;
    int pageSize = 20;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_stat_list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ScreenAdapterTools.getInstance().loadView(view);
        unbinder = ButterKnife.bind(this, view);
        tv_start.setText(CommonUtils.firstDateStrForMonth());
        tv_stop.setText(CommonUtils.currentDateStr());
        statPresenter = new StatPresenter(this);
        //设置下拉刷新
        swipeRefreshStat.setColorSchemeColors(getResources().getColor(R.color.colorLightBlue),
                getResources().getColor(R.color.colorBule));
        swipeRefreshStat.setOnRefreshListener(() -> initData());
        //设置列表排列方向
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mFragmentView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvStat.setLayoutManager(mLayoutManager);

        dayItemAdapter = new StatDayItemAdapter(mFragmentView.getContext(), true, false, dayInfo -> {
            Intent intent = new Intent(FragmentStatList.this.getActivity(), StatActivity.class);
            intent.putExtra("statType", "day");
            intent.putExtra("unitCode", dayInfo.getUnitCode());
            intent.putExtra("unitName", dayInfo.getUnitName());
            intent.putExtra("startDate", tv_start.getText().toString());
            intent.putExtra("stopDate", tv_stop.getText().toString());
            startActivity(intent);
        });

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
        return view;
    }

    @OnClick({R.id.sdi_start, R.id.sdi_stop, R.id.sdi_btn_stat})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.sdi_start:
                editDate(tv_start);
                break;
            case R.id.sdi_stop:
                editDate(tv_stop);
                break;
            case R.id.sdi_btn_stat:
                initData();
                break;
        }
    }

    private void editDate(TextView textView) {
        try {
            String value = textView.getText().toString();
            int y = Integer.parseInt(value.substring(0, 4));
            int m = Integer.parseInt(value.substring(5, 7)) - 1;
            int d = Integer.parseInt(value.substring(8, 10));
            DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                String mon = ""+(month+1);
                if (mon.length() == 1) {
                    mon = "0" + mon;
                }
                String day = "" + dayOfMonth;
                if (day.length() == 1) {
                    day = "0" + day;
                }
                textView.setText(String.format("%s-%s-%s", year, mon, day));
            }, y, m, d);
            pickerDialog.getDatePicker().setCalendarViewShown(true);
            pickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            statPresenter.statDay(curPage, pageSize, tv_start.getText().toString(), tv_stop.getText().toString());
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
//        initData();
    }


    @Override
    public void success(int processId, BaseResp result) {
        switch (processId) {
            case NetConstant.APP_MSG_STAT_DAY:
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
            default:
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

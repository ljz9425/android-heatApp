package com.whisht.heatapp.view.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.whisht.heatapp.R;
import com.whisht.heatapp.view.adapter.TabFragmentAdapter;
import com.whisht.heatapp.view.base.BaseFragmentActivity;
import com.whisht.heatapp.view.base.HeatBaseView;
import com.whisht.heatapp.view.fragment.FragmentAlarm;
import com.whisht.heatapp.view.fragment.FragmentDetailStatus;
import com.whisht.heatapp.view.fragment.FragmentDeviceConfig;
import com.whisht.heatapp.view.fragment.FragmentOperatorLog;
import com.whisht.heatapp.view.fragment.FragmentRoomStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DetailActive extends BaseFragmentActivity implements HeatBaseView.TitleOnClickListener {

    @BindView(R.id.detail_tabLayout)
    TabLayout tabLayoutDetail;
    @BindView(R.id.detail_viewPager)
    ViewPager viewPagerDetail;
    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        baseView.onAfterCreate();
        baseView.showTitleBackOperator(true);
//        DeviceInfo deviceInfo = (DeviceInfo)intent.getExtras().getSerializable("info");
        Bundle bundle = getIntent().getExtras().getBundle("info");
        baseView.setTitleName(bundle.getString("unitName"));
        initView();
    }

    private void initView() {
        String[] titles = {"实时状态", "控制配置", "风盘状态", "操作日志", "报警数据"};
        TabFragmentAdapter adapter = new TabFragmentAdapter(this, getSupportFragmentManager(), titles);
        fragmentList.add(new FragmentDetailStatus());
        fragmentList.add(new FragmentDeviceConfig());
        fragmentList.add(new FragmentRoomStatus());
        fragmentList.add(new FragmentOperatorLog());
        fragmentList.add(new FragmentAlarm());
//        fragmentList.add(new FragmentHistory());
//        fragmentList.add(new FragmentHistory());
        adapter.setFragments(fragmentList);
        this.viewPagerDetail.setAdapter(adapter);
        this.tabLayoutDetail.setupWithViewPager(this.viewPagerDetail);
        for (int i = 0; i < tabLayoutDetail.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayoutDetail.getTabAt(i);
            if (null != tab) {
                tab.setCustomView(adapter.getTabView(i));
            }
        }
    }

    @Override
    protected void onDestroy() {
        for(Fragment fragment: fragmentList) {
            fragment.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onNextClick() {

    }
}

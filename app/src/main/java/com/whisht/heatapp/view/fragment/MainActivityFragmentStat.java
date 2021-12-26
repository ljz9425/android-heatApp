package com.whisht.heatapp.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.whisht.heatapp.R;
import com.whisht.heatapp.autoupdate.IUpdate;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.view.adapter.TabFragmentAdapter;
import com.whisht.heatapp.view.base.BaseFragment;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityFragmentStat extends BaseFragment {
    @BindView(R.id.stat_tabLayout)
    TabLayout tabLayoutStat;
    @BindView(R.id.stat_viewPager)
    ViewPager viewPagerStat;
    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main_stat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ScreenAdapterTools.getInstance().loadView(view);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        String[] titles = {"能耗日报", "能耗月报", "能耗年报", "能耗日分析", "能耗月分析"};
        TabFragmentAdapter adapter = new TabFragmentAdapter(getContext(), getActivity().getSupportFragmentManager(), titles);
        fragmentList.add(new FragmentStatList());
        fragmentList.add(new FragmentStatMonth());
        fragmentList.add(new FragmentStatYear());
        fragmentList.add(new FragmentHistory());
        fragmentList.add(new FragmentHistory());
        adapter.setFragments(fragmentList);
        this.viewPagerStat.setAdapter(adapter);
        this.tabLayoutStat.setupWithViewPager(this.viewPagerStat);
        for (int i = 0; i < tabLayoutStat.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayoutStat.getTabAt(i);
            if (null != tab) {
                tab.setCustomView(adapter.getTabView(i));
            }
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
            default:
        }

    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {

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

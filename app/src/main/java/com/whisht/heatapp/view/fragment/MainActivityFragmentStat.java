package com.whisht.heatapp.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.whisht.heatapp.R;
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
    @BindView(R.id.titleName)
    TextView tv_title;

    private BaseFragment currentFragment;
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
        tv_title.setText(R.string.main_menu_index_title);
        initView();
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    public void lazyLoad() {

    }

    private void initView() {
        String[] titles = {"能耗日报", "能耗月报"};
        TabFragmentAdapter adapter = new TabFragmentAdapter(getContext(), getActivity().getSupportFragmentManager(), titles);
        fragmentList.add(new FragmentStatList());
        fragmentList.add(new FragmentStatMonth());
//        fragmentList.add(new FragmentHistory());
//        fragmentList.add(new FragmentHistory());
        adapter.setFragments(fragmentList);
        this.viewPagerStat.setAdapter(adapter);
        this.tabLayoutStat.setupWithViewPager(this.viewPagerStat);
        for (int i = 0; i < tabLayoutStat.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayoutStat.getTabAt(i);
            if (null != tab) {
                tab.setCustomView(adapter.getTabView(i));
            }
        }
        //关联切换
        viewPagerStat.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutStat));
        tabLayoutStat.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerStat.setCurrentItem(tab.getPosition());
                currentFragment = (BaseFragment) fragmentList.get(tab.getPosition());
                if(tab.getPosition()==0) {
//                    new Handler().postDelayed(() -> currentFragment.lazyLoad(), 1000);
                } else{
                    currentFragment.lazyLoad();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((BaseFragment)fragmentList.get(tab.getPosition())).hide();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

}

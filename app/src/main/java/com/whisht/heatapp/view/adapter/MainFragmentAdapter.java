package com.whisht.heatapp.view.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.whisht.heatapp.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentAdapter extends FragmentPagerAdapter {

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    private List<BaseFragment> fragmentList = new ArrayList<>();
    private Fragment instantFragment;

    public void addFragments(List<BaseFragment> fragments){
        this.fragmentList = fragments;
    }

    public void addFragment(BaseFragment baseFragment){
        fragmentList.add(baseFragment);
    }

    public BaseFragment getCurrentFragment(int position){
        return fragmentList.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList==null?0:fragmentList.size();
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        instantFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getInstantFragment() {
        return instantFragment;
    }
}

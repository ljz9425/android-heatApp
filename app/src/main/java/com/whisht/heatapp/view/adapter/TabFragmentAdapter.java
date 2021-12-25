package com.whisht.heatapp.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.whisht.heatapp.R;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

public class TabFragmentAdapter extends FragmentPagerAdapter {

    public static final String TAG = TabFragmentAdapter.class.getSimpleName();

    private String[] titleArray;
    private List<Fragment> listFragments;
    private Context context;

    public TabFragmentAdapter(Context context, FragmentManager fm, String[] titleArray) {
        super(fm);
        this.context = context;
        this.titleArray = titleArray;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    public void addFragment(Fragment fragment){
        this.listFragments.add(fragment);
    }

    public void setFragments(List<Fragment> fragments){
        this.listFragments = fragments;
    }


    public View getTabView(int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_tab_item, null);
        TextView item_text = (TextView)itemView.findViewById(R.id.item_text);
        item_text.setText(titleArray[position]);
        ScreenAdapterTools.getInstance().loadView(itemView);
        return itemView;
    }
}
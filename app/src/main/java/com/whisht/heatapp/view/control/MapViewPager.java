package com.whisht.heatapp.view.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MapViewPager extends ViewPager {
    public MapViewPager(@NonNull Context context) {
        super(context);
    }

    public MapViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (getCurrentItem() != 1) {
            return super.canScroll(v, checkV, dx, x, y);
        }
        if (Math.abs(dx) > 30) {
            return super.canScroll(v, checkV, dx, x, y);
        } else {
            return true;
        }
    }
}

package com.whisht.heatapp.utils;

import android.content.Context;
import android.widget.TextView;

public class TimerPicker {

    /**
     * 初始化时间选择器
     */
    public static DatePickerUtils initTimerPicker(Context context, DatePickerUtils sTimerPicker, TextView tv_selected_time,boolean isPreciseTime) {
        String beginTime = "2000-01-01";
        String endTime = "2100-01-01";//
        if(isPreciseTime){
            beginTime = "2000-01-01 00:00";
            endTime = "2100-01-01 00:00";
        }

        String currtime = DateFormatUtils.long2Str(System.currentTimeMillis(), isPreciseTime);

        tv_selected_time.setText(currtime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        sTimerPicker = new DatePickerUtils(context, new DatePickerUtils.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_selected_time.setText(DateFormatUtils.long2Str(timestamp, isPreciseTime));
            }
        }, beginTime, endTime,isPreciseTime);
        // 允许点击屏幕或物理返回键关闭
        sTimerPicker.setCancelable(true);
        // 显示时和分
        sTimerPicker.setCanShowPreciseTime(isPreciseTime);
        // 允许循环滚动
        sTimerPicker.setScrollLoop(false);
        // 允许滚动动画
        sTimerPicker.setCanShowAnim(true);
        return sTimerPicker;
    }
}

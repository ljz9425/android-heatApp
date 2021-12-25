package com.whisht.heatapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whisht.heatapp.R;
import com.whisht.heatapp.entity.AlarmInfo;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmItemAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<AlarmInfo> alarmInfoList;
    private LayoutInflater layoutInflater;

    public void setAlarmInfoList(List<AlarmInfo> alarmInfoList) {
        this.alarmInfoList = alarmInfoList;
    }

    public AlarmItemAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_alarm_item, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;//new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        AlarmInfo alarmInfo = alarmInfoList.get(position);
        itemViewHolder.bindView(alarmInfo, position);
    }

    @Override
    public int getItemCount() {
        return alarmInfoList == null ? 0 : alarmInfoList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.am_time)
        TextView tv_time;
        @BindView(R.id.am_result)
        TextView tv_result;
        @BindView(R.id.am_content)
        TextView tv_content;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(AlarmInfo alarmInfo, int position) {
            tv_time.setText(alarmInfo.getAlarmTime());
            tv_result.setText(alarmInfo.getAlarmResult());
            tv_content.setText(alarmInfo.getAlarmContent());
        }
    }

}

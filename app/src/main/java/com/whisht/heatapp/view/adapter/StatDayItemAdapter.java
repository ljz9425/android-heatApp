package com.whisht.heatapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whisht.heatapp.R;
import com.whisht.heatapp.entity.StatDayInfo;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatDayItemAdapter extends RecyclerView.Adapter {
    private boolean bArrow = false;
    private boolean bOpenDesc = false;
    private Context mContext;
    private List<StatDayInfo> dayInfoList;
    private LayoutInflater layoutInflater;
    private StatDayItemClick itemClick;

    public void setDayInfoList(List<StatDayInfo> dayInfoList) {
        this.dayInfoList = dayInfoList;
    }

    public StatDayItemAdapter(Context mContext, boolean bArrow, boolean bOpenDesc, StatDayItemClick itemClick) {
        this.mContext = mContext;
        this.itemClick = itemClick;
        this.bArrow = bArrow;
        this.bOpenDesc = bOpenDesc;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_stat_day_item, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        return itemViewHolder;//new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        StatDayInfo dayInfo = dayInfoList.get(position);
        itemViewHolder.bindView(dayInfo, bArrow, bOpenDesc, position);
    }

    @Override
    public int getItemCount() {
        return dayInfoList == null ? 0 : dayInfoList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sdi_ll_layout)
        LinearLayout ll_layout;
        @BindView(R.id.sdi_ll_times)
        LinearLayout ll_times;
        @BindView(R.id.sdi_ll_arrow)
        LinearLayout ll_arrow;
        @BindView(R.id.sdi_unit)
        TextView tv_unit;
        @BindView(R.id.sdi_times)
        TextView tv_times;
        @BindView(R.id.sdi_power)
        TextView tv_power;
        @BindView(R.id.sdi_consume)
        TextView tv_consume;
        @BindView(R.id.sdi_open_desc)
        TextView tv_open_desc;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(StatDayInfo dayInfo, boolean bArrow, boolean bOpenDesc, int position) {
            tv_times.setText(dayInfo.getOpenTime());
            tv_power.setText(String.format("%s kW-h (%s-%s)", dayInfo.getTotalPower(), dayInfo.getStartPower(), dayInfo.getStopPower()));
            tv_consume.setText(String.format("%s kWh/㎡ (面积: %s㎡)", dayInfo.getConsume(), dayInfo.getArea()));
            ll_arrow.setVisibility(bArrow ? View.VISIBLE : View.GONE);
            ll_times.setVisibility(bOpenDesc ? View.VISIBLE : View.GONE);
            if (bArrow) {
                tv_unit.setText("【" + dayInfo.getUnitName() + "】");
                ll_layout.setOnClickListener(v -> itemClick.onClick(dayInfo));
            } else {
                tv_unit.setText(dayInfo.getDate());
                tv_open_desc.setText(dayInfo.getOpenDesc());
            }
        }
    }

    public interface StatDayItemClick {
        void onClick(StatDayInfo dayInfo);
    }

}

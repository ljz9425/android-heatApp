package com.whisht.heatapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whisht.heatapp.R;
import com.whisht.heatapp.entity.DeviceInfo;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceItemAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<DeviceInfo> deviceInfoList;
    private LayoutInflater layoutInflater;
    private ViewDeviceClick viewDeviceClick;

    public void setDeviceInfoList(List<DeviceInfo> deviceInfoList) {
        this.deviceInfoList = deviceInfoList;
    }

    public DeviceItemAdapter(Context mContext, DeviceItemAdapter.ViewDeviceClick viewDeviceClick) {
        this.mContext = mContext;
        this.viewDeviceClick = viewDeviceClick;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_device_item, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;//new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        DeviceInfo deviceInfo = deviceInfoList.get(position);
        itemViewHolder.bindView(deviceInfo, position);
    }

    @Override
    public int getItemCount() {
        return deviceInfoList == null ? 0 : deviceInfoList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_item)
        LinearLayout layout_item;
        @BindView(R.id.unit_weather_icon)
        ImageView iv_weatherICon;
        @BindView(R.id.unit_weather_temper)
        TextView tv_weatherTemper;
        @BindView(R.id.unit_weather_day)
        TextView tv_weatherDay;
        @BindView(R.id.unit_name)
        TextView tv_unitName;
        @BindView(R.id.unit_statusDesc)
        TextView tv_statusDesc;
        @BindView(R.id.unit_macLastTime)
        TextView tv_macLastTime;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(DeviceInfo deviceInfo, int position) {
            try {
                Field field = R.drawable.class.getDeclaredField("vector_drawable_" + deviceInfo.getWeatherIcon() + "_fill");
                iv_weatherICon.setImageResource(field.getInt(field));
            } catch (Exception e) {}
            tv_weatherTemper.setText(deviceInfo.getWeatherTemp() + "℃");
            tv_weatherDay.setText(deviceInfo.getWeather());
            tv_unitName.setText(deviceInfo.getUnitName());
            tv_statusDesc.setText(deviceInfo.getStatusDesc());
            tv_macLastTime.setText(deviceInfo.getMacLastTime());
            layout_item.setOnClickListener(v -> viewDeviceClick.onClick(deviceInfo));
        }
    }

    public interface ViewDeviceClick {
        void onClick(DeviceInfo deviceInfo);
    }
}

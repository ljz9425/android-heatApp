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
import com.whisht.heatapp.entity.RoomInfo;
import com.whisht.heatapp.entity.StatDayInfo;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomItemAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<RoomInfo>  roomInfoList;
    private LayoutInflater layoutInflater;
    private RoomItemClick itemClick;

    public void setRoomInfoList(List<RoomInfo> roomInfoList) {
        this.roomInfoList = roomInfoList;
    }

    public RoomItemAdapter(Context mContext, RoomItemClick itemClick) {
        this.mContext = mContext;
        this.itemClick = itemClick;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_room_item, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        return itemViewHolder;//new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        RoomInfo roomInfo = roomInfoList.get(position);
        itemViewHolder.bindView(roomInfo, position);
    }

    @Override
    public int getItemCount() {
        return roomInfoList == null ? 0 : roomInfoList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ri_unit)
        TextView tv_unit;
        @BindView(R.id.ri_updateTime)
        TextView tv_updateTime;
        @BindView(R.id.ri_status)
        TextView tv_status;
        @BindView(R.id.ri_windSpeed)
        TextView tv_windSpeed;
        @BindView(R.id.ri_roomTemp)
        TextView tv_roomTemp;
        @BindView(R.id.ri_setTemp)
        TextView tv_setTemp;
        @BindView(R.id.ri_fault)
        TextView tv_fault;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(RoomInfo roomInfo, int position) {
            tv_unit.setText("【" + roomInfo.getRoomName() + "】");
            tv_updateTime.setText(roomInfo.getUpdateTime());
            tv_status.setText(String.format("%s %s", roomInfo.getStatus(), roomInfo.getRunModel()));
            tv_windSpeed.setText(roomInfo.getWindSpeed());
            tv_roomTemp.setText(roomInfo.getRoomTemp() + "℃");
            tv_setTemp.setText(roomInfo.getSetTemp() + "℃");
            tv_fault.setText(roomInfo.getFault());
            tv_status.setOnClickListener(v -> itemClick.onClick(roomInfo, ERoomOperateType.ROT_STATUS));
            tv_setTemp.setOnClickListener(v -> itemClick.onClick(roomInfo, ERoomOperateType.ROT_SET_TEMP));
        }
    }

    public interface RoomItemClick {
        void onClick(RoomInfo roomInfo, ERoomOperateType operateType);
    }

    public enum ERoomOperateType
    {
        ROT_STATUS,
        ROT_SET_TEMP;
    }

}

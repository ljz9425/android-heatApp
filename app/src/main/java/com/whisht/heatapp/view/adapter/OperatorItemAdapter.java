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
import com.whisht.heatapp.entity.OperatorInfo;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OperatorItemAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<OperatorInfo> operatorInfoList;
    private LayoutInflater layoutInflater;

    public void setOperatorInfoList(List<OperatorInfo> operatorInfoList) {
        this.operatorInfoList = operatorInfoList;
    }

    public OperatorItemAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_operator_item, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;//new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        OperatorInfo operatorInfo = operatorInfoList.get(position);
        itemViewHolder.bindView(operatorInfo, position);
    }

    @Override
    public int getItemCount() {
        return operatorInfoList == null ? 0 : operatorInfoList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.op_time)
        TextView tv_op_time;
        @BindView(R.id.op_unit)
        TextView tv_op_unit;
        @BindView(R.id.op_user)
        TextView tv_op_user;
        @BindView(R.id.op_content)
        TextView tv_op_content;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(OperatorInfo operatorInfo, int position) {
            tv_op_time.setText(operatorInfo.getOpTime());
            tv_op_unit.setText(operatorInfo.getOpUnit());
            tv_op_user.setText(operatorInfo.getOpUser());
            tv_op_content.setText(operatorInfo.getOpContent());
        }
    }

}

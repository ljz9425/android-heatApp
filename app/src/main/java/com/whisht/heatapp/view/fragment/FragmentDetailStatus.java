package com.whisht.heatapp.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.whisht.heatapp.R;
import com.whisht.heatapp.autoupdate.IUpdate;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.DeviceInfo;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.resp.DeviceStatusResp;
import com.whisht.heatapp.presenter.DevicePresenter;
import com.whisht.heatapp.utils.StringUtils;
import com.whisht.heatapp.view.base.BaseFragment;
import com.whisht.heatapp.view.control.DetailItem;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDetailStatus extends BaseFragment {

    @BindView(R.id.ll_detail_msg)
    LinearLayout ll_fault_msg;
    @BindView(R.id.detail_fault)
    TextView tv_fault;
    @BindView(R.id.iv_detail_weather_icon)
    ImageView iv_weather_icon;
    @BindView(R.id.tv_detail_weather_txt)
    TextView tv_weather_txt;
    @BindView(R.id.tv_detail_weather_day)
    TextView tv_weather_day;
    @BindView(R.id.detail_lastTime)
    TextView tv_last_time;
    @BindView(R.id.detail_run_status)
    DetailItem di_run_status;
    @BindView(R.id.detail_run_model)
    DetailItem di_run_model;
    @BindView(R.id.detail_set_temp)
    DetailItem di_set_temp;
    @BindView(R.id.detail_env_temp)
    DetailItem di_env_temp;
    @BindView(R.id.detail_send_temp)
    DetailItem di_send_temp;
    @BindView(R.id.detail_back_temp)
    DetailItem di_back_temp;
    @BindView(R.id.detail_volt_a)
    DetailItem di_volt_a;
    @BindView(R.id.detail_volt_b)
    DetailItem di_volt_b;
    @BindView(R.id.detail_volt_c)
    DetailItem di_volt_c;
    @BindView(R.id.detail_elec_a)
    DetailItem di_elec_a;
    @BindView(R.id.detail_elec_b)
    DetailItem di_elec_b;
    @BindView(R.id.detail_elec_c)
    DetailItem di_elec_c;
    @BindView(R.id.detail_power_active)
    DetailItem di_power_active;
    @BindView(R.id.detail_power_total)
    DetailItem di_power_total;
    @BindView(R.id.detail_heat_flow)
    DetailItem di_heat_flow;
    @BindView(R.id.detail_heat_total_flow)
    DetailItem di_heat_total_flow;
    @BindView(R.id.detail_heat_power)
    DetailItem di_heat_power;
    @BindView(R.id.detail_heat_total)
    DetailItem di_heat_total;
    @BindView(R.id.detail_heat_in_temp)
    DetailItem di_heat_in_temp;
    @BindView(R.id.detail_heat_out_temp)
    DetailItem di_heat_out_temp;
    @BindView(R.id.detail_pressure_send)
    DetailItem di_pressure_send;
    @BindView(R.id.detail_pressure_back)
    DetailItem di_pressure_back;

    @BindView(R.id.swipe_refresh_detail)
    SwipeRefreshLayout swipeRefreshDevice;

    private String unitCode = "";
    private DevicePresenter devicePresenter;
    private AlertDialog dialog;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_detail_stat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ScreenAdapterTools.getInstance().loadView(view);
        unbinder = ButterKnife.bind(this, view);
        devicePresenter = new DevicePresenter(this);
        Bundle bundle = getActivity().getIntent().getExtras().getBundle("info");;
        unitCode = bundle.getString("unitCode");
        //设置下拉刷新
        swipeRefreshDevice.setColorSchemeColors(getResources().getColor(R.color.colorLightBlue),
                getResources().getColor(R.color.colorBule));
        swipeRefreshDevice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
            }
        });
        di_run_status.setOnSetupClickListener(new DetailItem.ViewDetailClick() {
            @Override
            public void onSetupClick(View v) {
                if (di_run_status.getValue().equals("离线")) {
                    showToastMsg("设备离线状态，不能控制");
                    return;
                }
                String status = di_run_status.getValue().equals("启动") ? "停机" : "启动";
                boolean bOpen = di_run_status.getValue().equals("启动") ? false : true;
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                            .setTitle("提示")
                            .setMessage("确定要“" + status + "”吗？")
                            .setPositiveButton("确定", (d, which) -> {
                                dialog.dismiss();
                                if (bOpen) {
                                    devicePresenter.openHost(unitCode);
                                } else {
                                    devicePresenter.closeHost(unitCode);
                                }
                            })
                            .setCancelable(false);
                    dialog = builder.setNegativeButton("取消", (d, which) -> {
                        dialog.dismiss();
                    }).create();
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        di_set_temp.setOnSetupClickListener(new DetailItem.ViewDetailClick() {
            @Override
            public void onSetupClick(View v) {
                if (di_run_status.getValue().equals("离线")) {
                    showToastMsg("设备离线状态，不能控制");
                    return;
                }
                final EditText inputServer = new EditText(getActivity());
                inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputServer.setFocusable(true);
                inputServer.setText(di_set_temp.getValue().replace("℃", ""));

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("请输入设定温度").setView(inputServer).setNegativeButton(
                        "取消", null);
                builder.setPositiveButton("确定",
                        (dialog, which) -> {
                            try {
                                String inputName = inputServer.getText().toString();
                                int temp = Integer.parseInt(inputName);
                                if (temp < 10 || temp > 100) {
                                    showToastMsg("设定温度区间为10到100之间");
                                } else {
                                    if (!inputName.equals(di_set_temp.getValue().replace("℃", ""))) {
                                        di_set_temp.setValue(inputName + "℃");
                                        devicePresenter.setTemp(unitCode, inputName);
                                    }
                                }
                            } catch (Exception e) {
                                showToastMsg("请输入数字");
                            }
                        });
                builder.show();
            }
        });
        init();
        return view;
    }

    @Override
    public void init() {
        devicePresenter.queryDeviceStatus(unitCode);
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
            case NetConstant.APP_MSG_OPEN_HOST:
            case NetConstant.APP_MSG_CLOSE_HOST:
            case NetConstant.APP_MSG_SET_TEMP:
                showToastMsg("操作成功，请稍候下拉刷新状态");
                break;
            case NetConstant.APP_MSG_QUERY_DEVICE_STATUS:
                DeviceStatusResp statusResp = (DeviceStatusResp) result;
                if (StringUtils.isEmpty(statusResp.getFault()) && StringUtils.isEmpty(statusResp.getHeatStatus())) {
                    ll_fault_msg.setVisibility(View.GONE);
                } else {
                    if (!StringUtils.isEmpty(statusResp.getFault())) {
                        tv_fault.setText(statusResp.getFault());
                    } else {
                        tv_fault.setText(statusResp.getHeatStatus());
                    }
                }
                try {
                    Field field = R.drawable.class.getDeclaredField("vector_drawable_" + statusResp.getWeatherIcon() + "_fill");
                    iv_weather_icon.setImageResource(field.getInt(field));
                } catch (Exception e) {
                }
                tv_weather_txt.setText(statusResp.getWeatherText());
                tv_weather_day.setText(statusResp.getWeatherDay());
                tv_last_time.setText(statusResp.getMacLastTime());
                di_run_status.setValue(statusResp.getStatusDesc());
                di_run_model.setValue(statusResp.getRunModel());
                di_set_temp.setValue(statusResp.getSetTemp() + "℃");
                if (!StringUtils.isEmpty(statusResp.getEnvTemp())) {
                    di_env_temp.setValue(statusResp.getEnvTemp() + "℃");
                }
                di_send_temp.setValue(statusResp.getSendTemp() + "℃");
                di_back_temp.setValue(statusResp.getBackTemp() + "℃");
                if (!StringUtils.isEmpty(statusResp.getVoltA())) {
                    di_volt_a.setValue(statusResp.getVoltA());
                }
                if (!StringUtils.isEmpty(statusResp.getVoltB())) {
                    di_volt_b.setValue(statusResp.getVoltB());
                }
                if (!StringUtils.isEmpty(statusResp.getVoltC())) {
                    di_volt_c.setValue(statusResp.getVoltC());
                }
                if (!StringUtils.isEmpty(statusResp.getElecA())) {
                    di_elec_a.setValue(statusResp.getElecA());
                }
                if (!StringUtils.isEmpty(statusResp.getElecB())) {
                    di_elec_b.setValue(statusResp.getElecB());
                }
                if (!StringUtils.isEmpty(statusResp.getElecC())) {
                    di_elec_c.setValue(statusResp.getElecC());
                }
                if (!StringUtils.isEmpty(statusResp.getActivePower())) {
                    di_power_active.setValue(statusResp.getActivePower());
                }
                if (!StringUtils.isEmpty(statusResp.getTotalPower())) {
                    di_power_total.setValue(statusResp.getTotalPower());
                }
                if (!StringUtils.isEmpty(statusResp.getHeatCurFlow())) {
                    di_heat_flow.setValue(statusResp.getHeatCurFlow());
                }
                if (!StringUtils.isEmpty(statusResp.getHeatCurFlow())) {
                    di_heat_total_flow.setValue(statusResp.getHeatCurFlow());
                }
                if (!StringUtils.isEmpty(statusResp.getHeatPower())) {
                    di_heat_power.setValue(statusResp.getHeatPower());
                }
                if (!StringUtils.isEmpty(statusResp.getHeatTotalHeat())) {
                    di_heat_total.setValue(statusResp.getHeatTotalHeat());
                }
                if (!StringUtils.isEmpty(statusResp.getHeatInTemp())) {
                    di_heat_in_temp.setValue(statusResp.getHeatInTemp() + "℃");
                }
                if (!StringUtils.isEmpty(statusResp.getHeatOutTemp())) {
                    di_heat_out_temp.setValue(statusResp.getHeatOutTemp() + "℃");
                }
                if (!StringUtils.isEmpty(statusResp.getBackPressure())) {
                    di_pressure_send.setValue(statusResp.getBackPressure());
                }
                if (!StringUtils.isEmpty(statusResp.getSendPressure())) {
                    di_pressure_back.setValue(statusResp.getSendPressure());
                }
                //关闭下拉加载框
                swipeRefreshDevice.setRefreshing(false);
                break;
            default:
        }

    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {
        //关闭下拉加载框
        swipeRefreshDevice.setRefreshing(false);
        showToastMsg(errMsg);
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

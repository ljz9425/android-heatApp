package com.whisht.heatapp.view.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.whisht.heatapp.R;
import com.whisht.heatapp.autoupdate.IUpdate;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.DeviceInfo;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.req.DeviceConfigReq;
import com.whisht.heatapp.entity.http.resp.DeviceConfigResp;
import com.whisht.heatapp.entity.http.resp.DeviceStatusResp;
import com.whisht.heatapp.presenter.DevicePresenter;
import com.whisht.heatapp.utils.StringUtils;
import com.whisht.heatapp.view.base.BaseFragment;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentDeviceConfig extends BaseFragment {

    @BindView(R.id.dc_ctrl_type)
    Switch sw_ctrl_type;
    @BindView(R.id.dc_ctrl_saturday)
    Switch sw_ctrl_saturday;
    @BindView(R.id.dc_ctrl_sunday)
    Switch sw_ctrl_sunday;
    @BindView(R.id.dc_room_temp_open)
    TextView tv_room_temp_open;
    @BindView(R.id.dc_room_temp_close)
    TextView tv_room_temp_close;
    @BindView(R.id.dc_start_date)
    TextView tv_start_date;
    @BindView(R.id.dc_stop_date)
    TextView tv_stop_date;
    @BindView(R.id.dc_ctrl_1)
    Switch sw_ctrl_1;
    @BindView(R.id.dc_back_temp)
    TextView tv_back_temp;
    @BindView(R.id.dc_start_time)
    TextView tv_start_time;
    @BindView(R.id.dc_stop_time)
    TextView tv_stop_time;
    @BindView(R.id.dc_ctrl_2)
    Switch sw_ctrl_2;
    @BindView(R.id.dc_back_temp2)
    TextView tv_back_temp2;
    @BindView(R.id.dc_start_time2)
    TextView tv_start_time2;
    @BindView(R.id.dc_stop_time2)
    TextView tv_stop_time2;
    @BindView(R.id.dc_ctrl_3)
    Switch sw_ctrl_3;
    @BindView(R.id.dc_back_temp3)
    TextView tv_back_temp3;
    @BindView(R.id.dc_start_time3)
    TextView tv_start_time3;
    @BindView(R.id.dc_stop_time3)
    TextView tv_stop_time3;
    @BindView(R.id.dc_save)
    Button btn_save;

    private String id = "";
    private String unitCode = "";
    DevicePresenter devicePresenter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_device_config;
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
        btn_save.setEnabled(false);
        isPrepared = true;
        devicePresenter.queryDeviceConfig(unitCode);
        return view;
    }


    @OnClick({
            R.id.dc_room_temp_open, R.id.dc_room_temp_close, R.id.dc_start_date, R.id.dc_stop_date, R.id.dc_save,
            R.id.dc_ctrl_1, R.id.dc_back_temp, R.id.dc_start_time, R.id.dc_stop_time,
            R.id.dc_ctrl_2, R.id.dc_back_temp2, R.id.dc_start_time2, R.id.dc_stop_time2,
            R.id.dc_ctrl_3, R.id.dc_back_temp3, R.id.dc_start_time3, R.id.dc_stop_time3,
    })
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.dc_room_temp_open:
                if (sw_ctrl_type.isChecked()) {
                    editTemp(tv_room_temp_open, "????????????????????????");
                }
                break;
            case R.id.dc_room_temp_close:
                if (sw_ctrl_type.isChecked()) {
                    editTemp(tv_room_temp_close, "????????????????????????");
                }
                break;
            case  R.id.dc_back_temp:
                if (sw_ctrl_type.isChecked() && sw_ctrl_1.isChecked()) {
                    editTemp(tv_back_temp, "????????????1????????????");
                }
                break;
            case  R.id.dc_back_temp2:
                if (sw_ctrl_type.isChecked() && sw_ctrl_2.isChecked()) {
                    editTemp(tv_back_temp2, "????????????2????????????");
                }
                break;
            case  R.id.dc_back_temp3:
                if (sw_ctrl_type.isChecked() && sw_ctrl_3.isChecked()) {
                    editTemp(tv_back_temp3, "????????????3????????????");
                }
                break;
            case R.id.dc_start_date:
                if (sw_ctrl_type.isChecked()) {
                    editDate(tv_start_date, "????????????");
                }
                break;
            case R.id.dc_stop_date:
                if (sw_ctrl_type.isChecked()) {
                    editDate(tv_stop_date, "????????????");
                }
                break;
            case R.id.dc_start_time:
                if (sw_ctrl_type.isChecked() && sw_ctrl_1.isChecked()) {
                    editTime(tv_start_time, "????????????1????????????");
                }
                break;
            case R.id.dc_stop_time:
                if (sw_ctrl_type.isChecked() && sw_ctrl_1.isChecked()) {
                    editTime(tv_stop_time, "????????????1????????????");
                }
                break;
            case R.id.dc_start_time2:
                if (sw_ctrl_type.isChecked() && sw_ctrl_2.isChecked()) {
                    editTime(tv_start_time2, "????????????2????????????");
                }
                break;
            case R.id.dc_stop_time2:
                if (sw_ctrl_type.isChecked() && sw_ctrl_2.isChecked()) {
                    editTime(tv_stop_time2, "????????????2????????????");
                }
                break;
            case R.id.dc_start_time3:
                if (sw_ctrl_type.isChecked() && sw_ctrl_3.isChecked()) {
                    editTime(tv_start_time3, "????????????3????????????");
                }
                break;
            case R.id.dc_stop_time3:
                if (sw_ctrl_type.isChecked() && sw_ctrl_3.isChecked()) {
                    editTime(tv_stop_time3, "????????????3????????????");
                }
                break;
            case R.id.dc_ctrl_1:
                if(!sw_ctrl_1.isChecked() && sw_ctrl_2.isChecked()) {
                    showToastMsg("????????????????????????2");
                    sw_ctrl_1.setChecked(true);
                }
                break;
            case R.id.dc_ctrl_2:
                if(!sw_ctrl_2.isChecked() && sw_ctrl_3.isChecked()) {
                    showToastMsg("????????????????????????3");
                    sw_ctrl_2.setChecked(true);
                    return;
                }
                if (!sw_ctrl_1.isChecked() && sw_ctrl_2.isChecked()) {
                    showToastMsg("????????????????????????1");
                    sw_ctrl_2.setChecked(false);
                }
                break;
            case R.id.dc_ctrl_3:
                if (!sw_ctrl_2.isChecked() && sw_ctrl_3.isChecked()) {
                    showToastMsg("????????????????????????2");
                    sw_ctrl_3.setChecked(false);
                }
                break;
            case R.id.dc_save:
                saveConfig();
                break;
            default:
//                showToastMsg(view.getId()+"");
                break;
        }
    }

    private boolean validTimeEnd(String times, String msg) {
        if ("23:59".equals(times)) return true;
        String t = times.substring(3);
        if (!(t.equals("00") || t.equals("30"))) {
            showToastMsg(msg + "?????????" + times + "???????????????????????????23:59???xx:00???xx:30)");
            return false;
        }
        return true;
    }

    public boolean validTime(String curStart, String curStop, String upStart, String upStop, String msg) {
        try {
            if (curStart.substring(3).equals("30"))
            if (curStart.equals("00:00") && curStop.equals("00:00")) {
                showToastMsg("?????????" + msg + "????????????");
                return false;
            }
            int upOpenTime = Integer.parseInt(upStart.replace(":", ""));
            int upCloseTime = Integer.parseInt(upStop.replace(":", ""));
            int openTime = Integer.parseInt(curStart.replace(":", ""));
            int closeTime = Integer.parseInt(curStop.replace(":", ""));
            if (upCloseTime > upOpenTime) {
                if (openTime < upCloseTime && openTime > upOpenTime) {
                    System.out.println(msg + "???????????????" + curStart + "????????????" + upStart + "-" + upStop + "??????");
                    return false;
                }
                if (closeTime < upCloseTime && closeTime > upOpenTime) {
                    System.out.println(msg + "???????????????" + curStop + "????????????" + upStart + "-" + upStop + "??????");
                    return false;
                }
                if (openTime >= upCloseTime && closeTime < openTime && closeTime > upCloseTime) {
                    System.out.println(msg + "???????????????" + curStop + "?????????" + upStop + "-" + curStart + "??????");
                    return false;
                }
                if (openTime <= upOpenTime && closeTime > openTime && closeTime > upOpenTime) {
                    System.out.println(msg + "???????????????" + curStop + "?????????" + curStart + "-" + upStart + "??????");
                    return false;
                }
            } else {
                if (openTime < upCloseTime || openTime > upOpenTime) {
                    System.out.println(msg + "???????????????" + curStart + "????????????" + upStart + "-" + upStop + "??????");
                    return false;
                }
                if (closeTime < upCloseTime || closeTime > upOpenTime) {
                    System.out.println(msg + "???????????????" + curStop + "????????????" + upStart + "-" + upStop + "??????");
                    return false;
                }
                if (openTime > closeTime) {
                    System.out.println(msg + "???????????????" + curStop + "?????????" + curStart + "-" + upStart + "??????");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToastMsg(e.getMessage());
            return false;
        }
        return true;
    }

    private void saveConfig() {
        //???????????? ??????
        boolean bValid = false;
        if (Integer.parseInt(tv_room_temp_open.getText().toString())
                >= Integer.parseInt(tv_room_temp_close.getText().toString())) {
            showToastMsg("????????????????????????????????????????????????");
            return;

        }
        if (sw_ctrl_1.isChecked()) {
            if (tv_start_time.getText().toString().equals("00:00")
                && tv_stop_time.getText().toString().equals("00:00")) {
                showToastMsg("?????????????????????1????????????");
                return;
            }
            if (!validTimeEnd(tv_start_time.getText().toString(), "????????????1????????????")) return;
            if (!validTimeEnd(tv_stop_time.getText().toString(), "????????????1????????????")) return;
            if (tv_stop_time.getText().toString().equals("00:00")) {
                showToastMsg("????????????1?????????????????????00:00?????????23:59");
                return;
            }
            if (tv_back_temp.getText().toString().equals("0")) {
                showToastMsg("?????????????????????1????????????");
                return;
            }
        }
        if (sw_ctrl_2.isChecked()) {
            if (tv_back_temp2.getText().toString().equals("0")) {
                showToastMsg("?????????????????????2????????????");
                return;
            }
            if (!validTimeEnd(tv_start_time2.getText().toString(), "????????????2????????????")) return;
            if (!validTimeEnd(tv_stop_time2.getText().toString(), "????????????2????????????")) return;
            if (tv_stop_time2.getText().toString().equals("00:00")) {
                showToastMsg("????????????2?????????????????????00:00?????????23:59");
                return;
            }
            bValid = validTime(tv_start_time.getText().toString(),
                    tv_stop_time.getText().toString(),
                    tv_start_time2.getText().toString(),
                    tv_stop_time2.getText().toString(), "????????????2");
            if (!bValid) {
                return;
            }
        }
        if (sw_ctrl_3.isChecked()) {
            if (tv_back_temp3.getText().toString().equals("0")) {
                showToastMsg("?????????????????????3????????????");
                return;
            }
            if (!validTimeEnd(tv_start_time3.getText().toString(), "????????????3????????????")) return;
            if (!validTimeEnd(tv_stop_time3.getText().toString(), "????????????3????????????")) return;
            if (tv_stop_time3.getText().toString().equals("00:00")) {
                showToastMsg("????????????3?????????????????????00:00?????????23:59");
                return;
            }
            bValid = validTime(
                    tv_start_time2.getText().toString(),
                    tv_stop_time2.getText().toString(),
                    tv_start_time3.getText().toString(),
                    tv_stop_time3.getText().toString(), "????????????3");
            if (!bValid) {
                return;
            }

        }
        //???????????? ??????
        DeviceConfigReq req = new DeviceConfigReq();
        req.setId(id);
        req.setUnitCode(unitCode);
        req.setCtrlType((byte) (sw_ctrl_type.isChecked() ? 1 : 0));
        req.setSaturdayCtrl((byte) (sw_ctrl_saturday.isChecked() ? 1 : 0));
        req.setSundayCtrl((byte) (sw_ctrl_sunday.isChecked() ? 1 : 0));
        req.setStartDate(tv_start_date.getText().toString());
        req.setStopDate(tv_stop_date.getText().toString());
        req.setRoomTempOpen(Byte.parseByte(tv_room_temp_open.getText().toString()));
        req.setRoomTempClose(Byte.parseByte(tv_room_temp_close.getText().toString()));
        if (sw_ctrl_1.isChecked()) {
            req.setBackTemp(Byte.parseByte(tv_back_temp.getText().toString()));
            req.setOpenCloseTimingStart(tv_start_time.getText().toString());
            req.setOpenCloseTimingStop(tv_stop_time.getText().toString());
        } else {
            req.setBackTemp((byte) 0);
            req.setOpenCloseTimingStart("00:00");
            req.setOpenCloseTimingStop("00:00");
        }
        if (sw_ctrl_2.isChecked()) {
            req.setBackTemp2(Byte.parseByte(tv_back_temp2.getText().toString()));
            req.setOpenCloseTimingStart2(tv_start_time2.getText().toString());
            req.setOpenCloseTimingStop2(tv_stop_time2.getText().toString());
        } else {
            req.setBackTemp2((byte) 0);
            req.setOpenCloseTimingStart2("00:00");
            req.setOpenCloseTimingStop2("00:00");
        }
        if (sw_ctrl_3.isChecked()) {
            req.setBackTemp3(Byte.parseByte(tv_back_temp3.getText().toString()));
            req.setOpenCloseTimingStart3(tv_start_time3.getText().toString());
            req.setOpenCloseTimingStop3(tv_stop_time3.getText().toString());
        } else {
            req.setBackTemp3((byte) 0);
            req.setOpenCloseTimingStart3("00:00");
            req.setOpenCloseTimingStop3("00:00");
        }
        devicePresenter.saveDeviceConfig(req);
    }

    private void editTemp(TextView textView, String msg) {
        final EditText inputServer = new EditText(getActivity());
        inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputServer.setFocusable(true);
        inputServer.setText(textView.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(msg).setView(inputServer).setNegativeButton(
                "??????", null);
        builder.setPositiveButton("??????",
                (dialog, which) -> {
                    try {
                        String inputName = inputServer.getText().toString();
                        int temp = Integer.parseInt(inputName);
                        if (temp < 0 || temp > 100) {
                            showToastMsg("???" + msg + "????????????0???100??????");
                        } else {
                            if (!inputName.equals(textView.getText())) {
                                textView.setText(temp+"");
                            }
                        }
                    } catch (Exception e) {
                        showToastMsg("???????????????");
                    }
                });
        builder.show();
    }

    private void editDate(TextView textView, String msg) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int y = calendar.get(Calendar.YEAR);
            String value = textView.getText().toString();
            int m = Integer.parseInt(value.substring(0, value.indexOf("???"))) - 1;
            int d = Integer.parseInt(value.substring(value.indexOf("???") + 1, value.indexOf("???")));
            DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                String mon = ""+(month+1);
                if (mon.length() == 1) {
                    mon = "0" + mon;
                }
                String day = "" + dayOfMonth;
                if (day.length() == 1) {
                    day = "0" + day;
                }
                textView.setText(String.format("%s???%s???", mon, day));
//                showToastMsg(String.format("%s, %s, %s", year, month, dayOfMonth));
            }, y, m, d);
            pickerDialog.getDatePicker().setCalendarViewShown(true);
            pickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editTime(TextView textView, String msg) {
        try {
            String val = textView.getText().toString();
            if (StringUtils.isEmpty(val)) {
                val = "00:00";
            }
            int h = Integer.parseInt(val.substring(0, val.indexOf(":")));
            int m = Integer.parseInt(val.substring(val.indexOf(":") + 1));
            new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
                String hour = "" + hourOfDay;
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }
                String min = "" + minute;
                if (min.length() == 1) {
                    min = "0" + min;
                }
                textView.setText(String.format("%s:%s", hour, min));
                showToastMsg(String.format("%s, %s", hourOfDay, minute));
            }, h, m, true).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void hide() {
    }

    @Override
    public void lazyLoad() {
//        if (!isVisible || !isPrepared) {
//            return;
//        }
//        devicePresenter.queryDeviceConfig(unitCode);
    }

    @Override
    public void success(int processId, BaseResp result) {
        switch (processId) {
            case NetConstant.APP_MSG_QUERY_DEVICE_CONFIG:
                DeviceConfigResp resp = (DeviceConfigResp)result;
                id = resp.getId();
                if (resp.getCtrlType() == 1) {
                    sw_ctrl_type.setChecked(true);
                }
                if (resp.getSaturdayCtrl() == 1) {
                    sw_ctrl_saturday.setChecked(true);
                }
                if (resp.getSundayCtrl() == 1) {
                    sw_ctrl_sunday.setChecked(true);
                }
                tv_room_temp_open.setText(resp.getRoomTempOpen()+"");
                tv_room_temp_close.setText(resp.getRoomTempClose()+"");
                tv_start_date.setText(resp.getStartDate());
                tv_stop_date.setText(resp.getStopDate());
                if (resp.getBackTemp() > 0) {
                    sw_ctrl_1.setChecked(true);
                    tv_back_temp.setText(resp.getBackTemp()+"");
                    tv_start_time.setText(resp.getOpenCloseTimingStart());
                    tv_stop_time.setText(resp.getOpenCloseTimingStop());
                }
                if (resp.getBackTemp2() > 0) {
                    sw_ctrl_2.setChecked(true);
                    tv_back_temp2.setText(resp.getBackTemp2()+"");
                    tv_start_time2.setText(resp.getOpenCloseTimingStart2());
                    tv_stop_time2.setText(resp.getOpenCloseTimingStop2());
                }
                if (resp.getBackTemp3() > 0) {
                    sw_ctrl_3.setChecked(true);
                    tv_back_temp3.setText(resp.getBackTemp3()+"");
                    tv_start_time3.setText(resp.getOpenCloseTimingStart3());
                    tv_stop_time3.setText(resp.getOpenCloseTimingStop3());
                }
                btn_save.setEnabled(true);
                break;
            case NetConstant.APP_MSG_SAVE_DEVICE_STATUS:
                showToastMsg("????????????");
                break;
            default:
                break;
        }

    }


    @Override
    public void fail(int processId, int errCode, String errMsg) {
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

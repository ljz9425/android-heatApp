package com.whisht.heatapp.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.whisht.heatapp.R;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.RoomInfo;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.resp.RoomResp;
import com.whisht.heatapp.presenter.DevicePresenter;
import com.whisht.heatapp.view.adapter.RoomItemAdapter;
import com.whisht.heatapp.view.base.BaseFragment;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentRoomStatus extends BaseFragment {
    @BindView(R.id.swipe_room)
    SwipeRefreshLayout swipeRefreshRoom;
    @BindView(R.id.rv_room)
    RecyclerView rvRoom;

    private String unitCode = "";
    List<RoomInfo> roomInfoList;
    private DevicePresenter devicePresenter;
    private RoomItemAdapter roomItemAdapter;
    private int lastVisibleItem;
    boolean canLoad = false;
    int curPage = 1;
    int pageSize = 20;
    private AlertDialog dialog;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_room_status;
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
        //??????????????????
        swipeRefreshRoom.setColorSchemeColors(getResources().getColor(R.color.colorLightBlue),
                getResources().getColor(R.color.colorBule));
        swipeRefreshRoom.setOnRefreshListener(() -> initData());
        //????????????????????????
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mFragmentView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRoom.setLayoutManager(mLayoutManager);

        roomItemAdapter = new RoomItemAdapter(mFragmentView.getContext(), (roomInfo, operateType) -> {
            switch (operateType) {
                case ROT_STATUS:
                    boolean bOpen = !roomInfo.getStatus().contains("??????");
                    String status = bOpen ? "??????" : "??????";
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                                .setTitle("??????")
                                .setMessage("????????????" + status + "?????????")
                                .setPositiveButton("??????", (d, which) -> {
                                    dialog.dismiss();
                                    if (bOpen) {
                                        devicePresenter.openRoom(roomInfo.getUnitCode(), roomInfo.getId(), roomInfo.getRoomName());
                                    } else {
                                        devicePresenter.closeRoom(roomInfo.getUnitCode(), roomInfo.getId(), roomInfo.getRoomName());
                                    }
                                })
                                .setCancelable(false);
                        dialog = builder.setNegativeButton("??????", (d, which) -> {
                            dialog.dismiss();
                        }).create();
                        dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case ROT_SET_TEMP:
                    final EditText inputServer = new EditText(getActivity());
                    inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
                    inputServer.setFocusable(true);
                    inputServer.setText(roomInfo.getSetTemp().replace("???", ""));

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("?????????????????????").setView(inputServer).setNegativeButton(
                            "??????", null);
                    builder.setPositiveButton("??????",
                            (dialog, which) -> {
                                try {
                                    String inputName = inputServer.getText().toString();
                                    int temp = Integer.parseInt(inputName);
                                    if (temp < 10 || temp > 100) {
                                        showToastMsg("?????????????????????10???100??????");
                                    } else {
                                        if (!inputName.equals(roomInfo.getSetTemp().replace("???", ""))) {
                                            roomInfo.setRoomName(inputName + "???");
                                            devicePresenter.setRoomTemp(roomInfo.getUnitCode(), roomInfo.getId(), roomInfo.getRoomName(), roomInfo.getSetTemp());
                                        }
                                    }
                                } catch (Exception e) {
                                    showToastMsg("???????????????");
                                }
                            });
                    builder.show();

                    break;
            }
        });

        rvRoom.setAdapter(roomItemAdapter);
        rvRoom.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // ???????????????????????????????????????????????????
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == roomItemAdapter.getItemCount()) {
                    //?????????????????????????????????
                    queryNextPageData();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        isPrepared = true;
        initData();
        return view;
    }

    private void initData() {
        curPage = 1;
        canLoad = true;
        if (roomInfoList != null && roomInfoList.size() > 0 && null != roomItemAdapter) {
            roomItemAdapter.notifyItemRangeRemoved(0, roomItemAdapter.getItemCount());
            roomInfoList.clear();
        }
        queryNextPageData();
    }

    private void queryNextPageData() {
        if (canLoad && null != devicePresenter) {
            devicePresenter.queryRoomList(curPage, pageSize, unitCode);
        }
    }

    @Override
    public void lazyLoad() {
//        if (!isVisible || !isPrepared) {
//            return;
//        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void success(int processId, BaseResp result) {
        switch (processId) {
            case NetConstant.APP_MSG_OPEN_ROOM:
            case NetConstant.APP_MSG_CLOSE_ROOM:
            case NetConstant.APP_MSG_SET_ROOM_TEMP:
                showToastMsg("??????????????????????????????????????????");
                break;
            case NetConstant.APP_MSG_QUERY_ROOM_LIST:
                RoomResp resp = (RoomResp)result;
                List<RoomInfo> infoList = resp.getDataList();
                canLoad = curPage++ < resp.getMaxPageSize();
                if (null == infoList || infoList.size() <= 0) {
                    swipeRefreshRoom.setRefreshing(false);
                    if (isVisible) {
                        showToastMsg("????????????");
                    }
                    return;
                }
                int itemPosition;
                if (null == roomInfoList || roomInfoList.size() <= 0) {
                    itemPosition = 0;
                    roomInfoList = infoList;
                    roomItemAdapter.setRoomInfoList(roomInfoList);
                } else {
                    itemPosition = infoList.size();
                    roomInfoList.addAll(infoList);
                }
                roomItemAdapter.notifyItemRangeChanged(itemPosition, roomInfoList.size());
                //?????????????????????
                swipeRefreshRoom.setRefreshing(false);
                break;
            default:
        }
    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {
        //?????????????????????
        swipeRefreshRoom.setRefreshing(false);
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

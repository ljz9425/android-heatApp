package com.whisht.heatapp.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.whisht.heatapp.R;
import com.whisht.heatapp.autoupdate.IUpdate;
import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.entity.DeviceInfo;
import com.whisht.heatapp.entity.LoginInfo;
import com.whisht.heatapp.entity.base.BaseResp;
import com.whisht.heatapp.entity.http.resp.DeviceResp;
import com.whisht.heatapp.presenter.DevicePresenter;
import com.whisht.heatapp.presenter.base.BasePresenter;
import com.whisht.heatapp.utils.StringUtils;
import com.whisht.heatapp.view.activity.DetailActive;
import com.whisht.heatapp.view.base.BaseFragment;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityFragmentMap extends BaseFragment {

    @BindView(R.id.titleName)
    TextView tv_title;
    private MapView mMapView = null;

    private DevicePresenter devicePresenter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main_map;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        tv_title.setText(R.string.main_menu_index_title);
        mMapView = view.findViewById(R.id.bMapView);
        ScreenAdapterTools.getInstance().loadView(view);
        unbinder = ButterKnife.bind(this, view);
        devicePresenter = new DevicePresenter(this);
        LoginInfo loginInfo = devicePresenter.getLoginInfo();
        LatLng point = new LatLng(38.064921, 114.613982);
        int mapLevel = 12;
        try {
            point = new LatLng(Double.parseDouble(loginInfo.getLat()), Double.parseDouble(loginInfo.getLon()));
            mapLevel = Integer.parseInt(loginInfo.getMapLevel());
        } catch (Exception e) {}
        //?????????UiSettings?????????
        UiSettings mUiSettings = mMapView.getMap().getUiSettings();
        //????????????enable???true???false ????????????????????????????????????
        mUiSettings.setRotateGesturesEnabled(false);
        //??????????????????
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(mapLevel)
                .build();
        //??????MapStatusUpdate??????????????????????????????????????????????????????
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //??????????????????
        mMapView.getMap().setMapStatus(mMapStatusUpdate);

        mMapView.getMap().setOnMarkerClickListener(marker -> {
            Bundle bundle = marker.getExtraInfo();
            //????????????
            Intent intent = new Intent(MainActivityFragmentMap.this.getActivity(), DetailActive.class);
            intent.putExtra("info", bundle);
            startActivity(intent);
            return true;
        });
        isPrepared = true;
        lazyLoad();
        return view;
    }


    @Override
    public void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }
        if (null != devicePresenter) {
            devicePresenter.queryDeviceListForMap("");
        }
    }

    @Override
    public void hide() {
    }

    @Override
    public void onResume() {
        super.onResume();
        //???activity??????onResume?????????mMapView. onResume ()?????????????????????????????????
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //???activity??????onPause?????????mMapView. onPause ()?????????????????????????????????
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //???activity??????onDestroy?????????mMapView.onDestroy()?????????????????????????????????
        mMapView.onDestroy();
    }


    @Override
    public void success(int processId, BaseResp result) {
        switch (processId) {
            case NetConstant.APP_MSG_QUERY_DEVICE_LIST_FOR_MAP:
                mMapView.getMap().clear();
                DeviceResp deviceResp = (DeviceResp)result;
                List<DeviceInfo> infoList = deviceResp.getDataList();
                List<OverlayOptions> markerList  = new ArrayList<>();
                List<InfoWindow> infoWindowList = new ArrayList<>();
                for(DeviceInfo dev: infoList) {
                    if (StringUtils.isEmpty(dev.getLon()) || StringUtils.isEmpty(dev.getLat())) { continue; }
                    Bundle bundle = new Bundle();
                    bundle.putString("unitCode", dev.getUnitCode());
                    bundle.putString("unitName", dev.getUnitName());
                    LatLng point = new LatLng(Double.parseDouble(dev.getLat()), Double.parseDouble(dev.getLon()));
                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.pin_lock);
                    if (StringUtils.isEmpty(dev.getFault())) {
                        if (dev.getStatusDesc().equals("??????")) {
                            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.pin_open);
                        } else if (dev.getStatusDesc().equals("??????")) {
                            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.pin_close);
                        } else if (dev.getStatusDesc().equals("??????")) {
                            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.pin_offline);
                        }
                    }
                    markerList.add(new MarkerOptions().position(point).icon(bitmap).extraInfo(bundle));
                    //?????????InfoWindow
                    TextView button = new TextView(getContext());
                    button.setBackgroundResource(R.drawable.map_info_bg);
                    button.setText(dev.getUnitName());
                    button.setTextColor(Color.DKGRAY);
                    button.setPadding(0,0,0,5);
                    button.setTextSize(15);
                    // ??????InfoWindow
                    InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), point, -75, null);
                    infoWindowList.add(mInfoWindow);
                }
                mMapView.getMap().addOverlays(markerList );
                mMapView.getMap().showInfoWindows(infoWindowList);
                break;
            default:
        }

    }

    @Override
    public void fail(int processId, int errCode, String errMsg) {

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

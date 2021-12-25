package com.whisht.heatapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.content.PermissionChecker;

import com.whisht.heatapp.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.yanzhenjie.permission.runtime.PermissionDef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionHelper {
    public interface PermissionCallBack {
        void OnGranted();

        void OnGranted(int sign);

        void OnExit(boolean isMustNeed);

        void OnExit(boolean isMustNeed, int sign);
    }

    public static final int REQ_CODE_PERMISSION = 1;
    int targetSdkVersion;
    private List<String> permissions = new ArrayList<>();
    private List<String> permissionDenieds = new ArrayList<>();
    private List<String> permissionGranteds = new ArrayList<>();
    private List<String> permissionNoticeDenieds = new ArrayList<>();
    private Context mContext;
    private PermissionCallBack back;
    private boolean isGotoAppSetting = false;

    public PermissionHelper(Context context, PermissionCallBack back) {
        this.mContext = context;
        this.back = back;
        try {
            final PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AlertDialog dialog;

    public void requestPermission(boolean isMustNeed, List<String> permissions) {
        String[] per = new String[permissions.size()];
        per = permissions.toArray(per);
        requestPermission(isMustNeed, Integer.MAX_VALUE, per);
    }

    public void requestPermission(List<String> permissions) {
        requestPermission(true, permissions);
    }

    public void requestPermission(String[]... permissionGroup) {
        requestPermission(true, Integer.MAX_VALUE, permissionGroup);
    }

    public void requestPermission(boolean isMustNeed, int sign, String[]... permissionGroup) {
        List<String> tmp = new ArrayList<>();
        for (String[] i : permissionGroup) {
            tmp.addAll(Arrays.asList(i));
        }
        String[] per = new String[tmp.size()];
        per = tmp.toArray(per);
        requestPermission(isMustNeed, sign, per);
    }

    public void requestPermission(String... permissionArray) {
        requestPermission(true, Integer.MAX_VALUE, permissionArray);
    }

    private boolean checkPermission(String... permissionList) {
        boolean granted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = PackageManager.PERMISSION_DENIED;
            for (String pm : permissionList) {
                if (targetSdkVersion >= Build.VERSION_CODES.M) {
                    result = mContext.checkSelfPermission(pm);
                } else {
                    // targetSdkVersion < Android M, we have to use PermissionChecker
                    result = PermissionChecker.checkSelfPermission(mContext, pm);
                }
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
        }
        return granted;
    }

    public void requestPermission(boolean isMustNeed, int sign, String... permissionArray) {
        if (checkPermission(permissionArray)) {
            if (sign == Integer.MAX_VALUE)
                back.OnGranted();
            else
                back.OnGranted(sign);
        } else {
            permissions.addAll(Arrays.asList(permissionArray));
            filterPermission(false);
            if (dialog != null && dialog.isShowing())
                return;
            //获取权限提示语
            List<String> perNameList = Permission.transformText(mContext, permissionDenieds);
            String perNames = TextUtils.join(",", perNameList);
            if (perNames.equals("")) {
                //暂时这样做吧，可能不太好
                if (sign == Integer.MAX_VALUE)
                    back.OnGranted();
                else
                    back.OnGranted(sign);
                return;
            }
            String tip = "APP需要获取" + perNames + "，请点击允许\n否则，您将无法正常使用APP";
            dialog = new AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Light_Dialog_Alert)
                    .setTitle(perNames + "不可用")
                    .setMessage(tip)
                    .setPositiveButton("立即开启", (d, which) -> {
                        dialog.dismiss();
                        startRequestPermission(isMustNeed, sign);
                    })
                    .setNegativeButton("取消", (d, which) -> {
                        dialog.dismiss();
                        if (sign == Integer.MAX_VALUE)
                            back.OnExit(isMustNeed);
                        else
                            back.OnExit(isMustNeed, sign);
                    }).setCancelable(false).create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#52b9fe"));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
        }
    }

    private void filterPermission(boolean isFilterNoticePer) {
        permissionDenieds.clear();
        permissionGranteds.clear();
        permissionNoticeDenieds.clear();
        for (String per : permissions) {
            if (AndPermission.hasPermissions(mContext, per)) {
                permissionGranteds.add(per);
            } else {
                permissionDenieds.add(per);
                if (isFilterNoticePer && AndPermission.hasAlwaysDeniedPermission(mContext, per)) {
                    permissionNoticeDenieds.add(per);
                }
            }
        }
    }

    private void startRequestPermission(boolean isMustNeed, int sign) {
        if (permissionDenieds.size() > 0 && permissionDenieds.size() == permissionNoticeDenieds.size()) {
            gotoSetting(isMustNeed, sign);
        } else {
            @PermissionDef String[] toRequest = new String[permissionDenieds.size()];
            toRequest = permissionDenieds.toArray(toRequest);
            AndPermission.with(mContext)
                    .runtime()
                    .permission(toRequest)
                    .onDenied((denied) -> {
                        filterPermission(true);
                        if (permissionDenieds.size() > 0) {
                            if (permissionDenieds.size() == permissionNoticeDenieds.size()) {
                                gotoSetting(isMustNeed, sign);
                            } else {
                                if (isMustNeed)
                                    tipExit(sign);
                                else {
                                    if (sign == Integer.MAX_VALUE)
                                        back.OnExit(isMustNeed);
                                    else
                                        back.OnExit(isMustNeed, sign);
                                }
                            }
                        } else {
                            if (sign == Integer.MAX_VALUE)
                                back.OnGranted();
                            else
                                back.OnGranted(sign);
                        }
                    })
                    .onGranted((granted) -> {
                        if (sign == Integer.MAX_VALUE)
                            back.OnGranted();
                        else
                            back.OnGranted(sign);
                    })
                    .start();
        }
    }

    private void tipExit(int sign) {
        if (dialog != null && dialog.isShowing())
            return;
        //获取权限提示语
        List<String> perNameList = Permission.transformText(mContext, permissionDenieds);
        String perNames = TextUtils.join(",", perNameList);
        String tip = "APP需要获取" + perNames + "，您已经禁止\n将无法正常使用APP";
        dialog = new AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle(perNames + "不可用")
                .setMessage(tip)
                .setPositiveButton("确认", (d, which) -> {
                    dialog.dismiss();
                    if (sign == Integer.MAX_VALUE)
                        back.OnExit(true);
                    else
                        back.OnExit(true, sign);
                })
                .setCancelable(false).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#52b9fe"));
    }

    private void gotoSetting(boolean isMustNeed, int sign) {
        if (isGotoAppSetting) {
            if (isMustNeed)
                tipExit(sign);
            else {
                if (sign == Integer.MAX_VALUE)
                    back.OnExit(isMustNeed);
                else
                    back.OnExit(isMustNeed, sign);
            }
            return;
        }
        isGotoAppSetting = true;
        AndPermission.with(mContext)
                .runtime()
                .setting()
                .start(REQ_CODE_PERMISSION);
    }
}

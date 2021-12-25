package com.whisht.heatapp.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.whisht.heatapp.BuildConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RomUtil {
    /**
     * Build.MANUFACTURER判断各大手机厂商品牌
     */
    private static final String MANUFACTURER_HUAWEI = "huawei";//华为
    private static final String MANUFACTURER_MEIZU = "meizu";//魅族
    private static final String MANUFACTURER_XIAOMI = "xiaomi";//小米
    private static final String MANUFACTURER_SONY = "sony";//索尼
    private static final String MANUFACTURER_OPPO = "oppo";
    private static final String MANUFACTURER_LG = "lg";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    private static final String MANUFACTURER_LETV = "letv";//乐视
    private static final String MANUFACTURER_ZTE = "zte";//中兴
    private static final String MANUFACTURER_YULONG = "yulong";//酷派
    private static final String MANUFACTURER_LENOVO = "lenovo";//联想

    private static final int PERMISSION_SETTING_FOR_RESULT = Short.MAX_VALUE;
    private static boolean isAppSettingOpen = false;

    public static boolean isMIMU() {
        return MANUFACTURER_XIAOMI.equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isOPPO() {
        return MANUFACTURER_OPPO.equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isHUAWEI() {
        return MANUFACTURER_HUAWEI.equalsIgnoreCase(Build.MANUFACTURER);
    }
    /**
     * 跳转到相应品牌手机系统权限设置页，如果跳转不成功，则跳转到应用详情页
     * 这里需要改造成返回true或者false，应用详情页:true，应用权限页:false
     *
     * @param activity
     */
    public static void GotoSetting(Activity activity) {
        switch (Build.MANUFACTURER.toLowerCase()) {
            case MANUFACTURER_HUAWEI://华为
                Huawei(activity);
                break;
            case MANUFACTURER_MEIZU://魅族
                Meizu(activity);
                break;
            case MANUFACTURER_XIAOMI://小米
                Xiaomi(activity);
                break;
            case MANUFACTURER_SONY://索尼
                Sony(activity);
                break;
            case MANUFACTURER_OPPO://oppo
                OPPO(activity);
                break;
            case MANUFACTURER_LG://lg
                LG(activity);
                break;
            case MANUFACTURER_LETV://乐视
                Letv(activity);
                break;
            default://其他
                try {//防止应用详情页也找不到，捕获异常后跳转到设置，这里跳转最好是两级，太多用户也会觉得麻烦，还不如不跳
                    openAppDetailSetting(activity);
//                    activity.startActivityForResult(getAppDetailSettingIntent(activity), PERMISSION_SETTING_FOR_RESULT);
                } catch (Exception e) {
                    SystemConfig(activity);
                }
                break;
        }
    }

    private static void Huawei(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            activity.startActivityForResult(intent, PERMISSION_SETTING_FOR_RESULT);
            isAppSettingOpen = false;
        } catch (Exception e) {
            openAppDetailSetting(activity);
        }
    }

    private static void Meizu(Activity activity) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            activity.startActivity(intent);
            isAppSettingOpen=false;
        } catch (Exception e) {
            openAppDetailSetting(activity);
        }
    }

    private static void Xiaomi(Activity activity) {
        try {// MIUI 8 9
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", activity.getPackageName());
            activity.startActivityForResult(localIntent, PERMISSION_SETTING_FOR_RESULT);
            isAppSettingOpen=false;
        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", activity.getPackageName());
                activity.startActivityForResult(localIntent, PERMISSION_SETTING_FOR_RESULT);
                isAppSettingOpen=false;
            } catch (Exception e1) { // 否则跳转到应用详情
                openAppDetailSetting(activity);
//                activity.startActivityForResult(getAppDetailSettingIntent(activity), PERMISSION_SETTING_FOR_RESULT);
                //这里有个问题，进入活动后需要再跳一级活动，就检测不到返回结果
//                activity.startActivity(getAppDetailSettingIntent());
            }
        }
    }

    /**
     * 索尼，6.0以上的手机非常少，基本没看见
     * @param activity
     */
    private static void Sony(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
            isAppSettingOpen=false;
        } catch (Exception e) {
            openAppDetailSetting(activity);
        }
    }

    private static void OPPO(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
            isAppSettingOpen=false;
        } catch (Exception e) {
            openAppDetailSetting(activity);
        }
    }

    /**
     * LG经过测试，正常使用
     * @param activity
     */
    private static void LG(Activity activity) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
            isAppSettingOpen=false;
        } catch (Exception e) {
            openAppDetailSetting(activity);
        }
    }

    /**
     * 乐视6.0以上很少，基本都可以忽略了，现在乐视手机不多
     * @param activity
     */
    private static void Letv(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
            intent.setComponent(comp);
            activity.startActivity(intent);
            isAppSettingOpen=false;
        } catch (Exception e) {
            openAppDetailSetting(activity);
        }
    }

    /**
     * 只能打开到自带安全软件
     * @param activity
     */
    private static void _360(Activity activity) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        }catch (Exception e){
            openAppDetailSetting(activity);
        }
    }

    /**
     * 获取应用详情页面
     * @return
     */
    private static Intent getAppDetailSettingIntent(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        return localIntent;
    }

    private static void openAppDetailSetting(Activity activity) {
        activity.startActivityForResult(getAppDetailSettingIntent(activity), PERMISSION_SETTING_FOR_RESULT);
        isAppSettingOpen=true;
    }

    /**
     * 系统设置界面
     * @param activity
     */
    private static void SystemConfig(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }


    public static void GoToAppSetting(Activity activity) {
        switch (Build.MANUFACTURER.toLowerCase()){
            case MANUFACTURER_HUAWEI://华为
                HuaweiApp(activity);
                break;
            case MANUFACTURER_XIAOMI://小米
                XiaomiApp(activity);
                break;
            default://其他
                try {//防止应用详情页也找不到，捕获异常后跳转到设置，这里跳转最好是两级，太多用户也会觉得麻烦，还不如不跳
                    openAppDetailSetting(activity);
                }catch (Exception e){
                    SystemConfig(activity);
                }
                break;
        }
    }
    private static void HuaweiApp(Activity activity){
        try{
            Intent intent = new Intent();
            intent.setClassName("com.android.settings","com.android.settings.applications.InstalledAppDetailsTop");
            intent.setData(Uri.parse("package:"+activity.getPackageName()));
            activity.startActivityForResult(intent, PERMISSION_SETTING_FOR_RESULT);
        }catch (Exception e){
            e.printStackTrace();
            GotoSetting(activity);
        }
    }
    private static void XiaomiApp(Activity activity){
        try{
            Intent intent = new Intent();
            intent.setClassName("com.android.settings","com.android.settings.applications.InstalledAppDetailsTop");
            intent.setData(Uri.parse("package:"+activity.getPackageName()));
            activity.startActivityForResult(intent, PERMISSION_SETTING_FOR_RESULT);
        }catch (Exception e){
            GotoSetting(activity);
        }
    }

    public static class AutoBootUtil{
        private static HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>() {
            {
                put("Xiaomi", Arrays.asList(
                        "com.miui.securitycenter/com.miui.permcenter.autostart.AutoStartManagementActivity",//MIUI10_9.8.1(9.0)
                        "com.miui.securitycenter"
                ));

                put("samsung", Arrays.asList(
                        "com.samsung.android.sm_cn/com.samsung.android.sm.ui.ram.AutoRunActivity",
                        "com.samsung.android.sm_cn/com.samsung.android.sm.ui.appmanagement.AppManagementActivity",
                        "com.samsung.android.sm_cn/com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity",
                        "com.samsung.android.sm_cn/.ui.ram.RamActivity",
                        "com.samsung.android.sm_cn/.app.dashboard.SmartManagerDashBoardActivity",

                        "com.samsung.android.sm/com.samsung.android.sm.ui.ram.AutoRunActivity",
                        "com.samsung.android.sm/com.samsung.android.sm.ui.appmanagement.AppManagementActivity",
                        "com.samsung.android.sm/com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity",
                        "com.samsung.android.sm/.ui.ram.RamActivity",
                        "com.samsung.android.sm/.app.dashboard.SmartManagerDashBoardActivity",

                        "com.samsung.android.lool/com.samsung.android.sm.ui.battery.BatteryActivity",
                        "com.samsung.android.sm_cn",
                        "com.samsung.android.sm"
                ));


                put("HUAWEI", Arrays.asList(
                        "com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity",//EMUI9.1.0(方舟,9.0)
                        "com.huawei.systemmanager/.appcontrol.activity.StartupAppControlActivity",
                        "com.huawei.systemmanager/.optimize.process.ProtectActivity",
                        "com.huawei.systemmanager/.optimize.bootstart.BootStartActivity",
                        "com.huawei.systemmanager"//最后一行可以写包名, 这样如果签名的类路径在某些新版本的ROM中没找到 就直接跳转到对应的安全中心/手机管家 首页.
                ));

                put("vivo", Arrays.asList(
                        "com.iqoo.secure/.ui.phoneoptimize.BgStartUpManager",
                        "com.iqoo.secure/.safeguard.PurviewTabActivity",
                        "com.vivo.permissionmanager/.activity.BgStartUpManagerActivity",
//                    "com.iqoo.secure/.ui.phoneoptimize.AddWhiteListActivity", //这是白名单, 不是自启动
                        "com.iqoo.secure",
                        "com.vivo.permissionmanager"
                ));

                put("Meizu", Arrays.asList(
                        "com.meizu.safe/.permission.SmartBGActivity",//Flyme7.3.0(7.1.2)
                        "com.meizu.safe/.permission.PermissionMainActivity",//网上的
                        "com.meizu.safe"
                ));

                put("OPPO", Arrays.asList(
                        "com.coloros.safecenter/.startupapp.StartupAppListActivity",
                        "com.coloros.safecenter/.permission.startup.StartupAppListActivity",
                        "com.oppo.safe/.permission.startup.StartupAppListActivity",
                        "com.coloros.oppoguardelf/com.coloros.powermanager.fuelgaue.PowerUsageModelActivity",
                        "com.coloros.safecenter/com.coloros.privacypermissionsentry.PermissionTopActivity",
                        "com.coloros.safecenter",
                        "com.oppo.safe",
                        "com.coloros.oppoguardelf"
                ));

                put("oneplus", Arrays.asList(
                        "com.oneplus.security/.chainlaunch.view.ChainLaunchAppListActivity",
                        "com.oneplus.security"
                ));
                put("letv", Arrays.asList(
                        "com.letv.android.letvsafe/.AutobootManageActivity",
                        "com.letv.android.letvsafe/.BackgroundAppManageActivity",//应用保护
                        "com.letv.android.letvsafe"
                ));
                put("zte", Arrays.asList(
                        "com.zte.heartyservice/.autorun.AppAutoRunManager",
                        "com.zte.heartyservice"
                ));
                //金立
                put("F", Arrays.asList(
                        "com.gionee.softmanager/.MainActivity",
                        "com.gionee.softmanager"
                ));

                //以下为未确定(厂商名也不确定)
                put("smartisanos", Arrays.asList(
                        "com.smartisanos.security/.invokeHistory.InvokeHistoryActivity",
                        "com.smartisanos.security"
                ));
                //360
                put("360", Arrays.asList(
                        "com.yulong.android.coolsafe/.ui.activity.autorun.AutoRunListActivity",
                        "com.yulong.android.coolsafe"
                ));
                //360
                put("ulong", Arrays.asList(
                        "com.yulong.android.coolsafe/.ui.activity.autorun.AutoRunListActivity",
                        "com.yulong.android.coolsafe"
                ));
                //酷派
                put("coolpad"/*厂商名称不确定是否正确*/, Arrays.asList(
                        "com.yulong.android.security/com.yulong.android.seccenter.tabbarmain",
                        "com.yulong.android.security"
                ));
                //联想
                put("lenovo"/*厂商名称不确定是否正确*/, Arrays.asList(
                        "com.lenovo.security/.purebackground.PureBackgroundActivity",
                        "com.lenovo.security"
                ));
                put("htc"/*厂商名称不确定是否正确*/, Arrays.asList(
                        "com.htc.pitroad/.landingpage.activity.LandingPageActivity",
                        "com.htc.pitroad"
                ));
                //华硕
                put("asus"/*厂商名称不确定是否正确*/, Arrays.asList(
                        "com.asus.mobilemanager/.MainActivity",
                        "com.asus.mobilemanager"
                ));

            }
        };

        public static void startToAutoStartSetting(Context context) {
            Log.e("Util", "******************当前手机型号为：" + Build.MANUFACTURER);

            Set<Map.Entry<String, List<String>>> entries = hashMap.entrySet();
            boolean has = false;
            for (Map.Entry<String, List<String>> entry : entries) {
                String manufacturer = entry.getKey();
                List<String> actCompatList = entry.getValue();
                if (Build.MANUFACTURER.equalsIgnoreCase(manufacturer)) {
                    for (String act : actCompatList) {
                        try {
                            Intent intent;
                            if (act.contains("/")) {
                                intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ComponentName componentName = ComponentName.unflattenFromString(act);
                                intent.setComponent(componentName);
                            } else {
                                //找不到? 网上的做法都是跳转到设置... 这基本上是没意义的 基本上自启动这个功能是第三方厂商自己写的安全管家类app
                                //所以我是直接跳转到对应的安全管家/安全中心
                                intent = context.getPackageManager().getLaunchIntentForPackage(act);
                            }
                            context.startActivity(intent);
                            has = true;
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (!has) {
                try {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        }
    }
}

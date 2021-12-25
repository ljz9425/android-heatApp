package com.whisht.heatapp.utils;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SharedPreferencesHelper {
    public static final String MSP_SESSION = "session";
    public static final String MSP_USERID = "userid";
    public static final String MSP_MAP_LEVEL = "maplevel";
    public static final String MSP_MAP_CENTER = "mapcenter";
    public static final String MSP_USERINFO = "userinfo";
    public static final String MSP_AUTO_BOOT = "autoboot";
    public static final String MSP_LOCKSHOW = "lockshow";

    private SecuritySharedPreference sharedPreferences;
    private SecuritySharedPreference.SecurityEditor editor;

    public SharedPreferencesHelper(Context context, String FILE_NAME) {
        sharedPreferences = new SecuritySharedPreference(context, FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 存储
     */
    public void putValue(String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if(object instanceof Serializable) {
            editor.putString(key, HBJsonUtil.convertEntityToJSONStr(object));
        } else if(object instanceof List){
            editor.putString(key,HBJsonUtil.convertEntityToJSONStr(object));
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /**
     * 获取保存的数据
     */
    public Object getValue(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        } else if(defaultObject instanceof ArrayList){
            String tmp = sharedPreferences.getString(key, "[]");
            return HBJsonUtil.convertJSONStrToEntity(defaultObject.getClass(), tmp);
        } else if(defaultObject instanceof List){
            String tmp = sharedPreferences.getString(key, "[]");
            return HBJsonUtil.convertJSONStrToEntity(defaultObject.getClass(), tmp);
        } else if(defaultObject instanceof Serializable) {
            String tmp = sharedPreferences.getString(key, "{}");
            return HBJsonUtil.convertJSONStrToEntity(defaultObject.getClass(), tmp);
        } else {
            return sharedPreferences.getString(key, null);
        }
    }

    /**
     * 移除某个key值已经对应的值
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否存在
     */
    public Boolean contain(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }
}

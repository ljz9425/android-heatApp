package com.whisht.heatapp.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;


public class HBJsonUtil {
    public static <T> T convertJSONStrToEntity(TypeReference<T> c, String jsonStr) {
        T cr = JSONObject.parseObject(jsonStr, c);
        return cr;
    }
    public static <T> T convertJSONStrToEntity(ParameterizedTypeImpl c, String jsonStr) {
        T cr = JSONObject.parseObject(jsonStr, c);
        return cr;
    }
    /**
     * 将json字符串转换为java对象
     * @param <T> 对象类
     * @param c 需要转换后的类，当jsonStr包含@Type时，为父类或者子类类别
     * @param jsonStr 需要转换的json字符串
     * @return
     */
    public static <T> T convertJSONStrToEntity(Class<T> c,String jsonStr) {
        T cr = JSONObject.parseObject(jsonStr, c);
        return cr;
    }
    public static JSONObject convertJSONStrToJsonObject(String jsonStr) {
        try {
            return JSONObject.parseObject(jsonStr);
        }catch(Exception e) {}
        return null;
    }
    public static JSONArray convertJSONStrToJsonArray(String jsonStr) {
        return JSONArray.parseArray(jsonStr);
    }
    /**
     * 将类实例转换为json字符串
     * @param <T>
     * @param c
     * @return
     */
    public static <T> String convertEntityToJSONStr(T c) {
        return JSONObject.toJSONString(c);
    }
    /**
     * 将类实例转换为json字符串
     * @param <T> 需要转换的类
     * @param c 需要转换的类实例
     * @param isType 是否增加@Type 标记
     * @return
     */
    public static <T> String convertEntityToJSONStr(T c,boolean isType) {
        return JSONObject.toJSONString(c, SerializerFeature.WriteClassName);
    }
}

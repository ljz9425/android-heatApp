package com.whisht.heatapp.utils.netutils.base;

import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NetWorkService {
    /**
     * 下载apk（注明为流文件，防止retrofit将大文件读入内存）
     * @param url   链接地址 通过@Url覆盖baseurl
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadGetByUrl(@Url String url);

    @POST("configversion/query")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> queryVersion(@Body RequestBody requestBody);

    @POST("user/updatePassWord")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> updatePassWord(@Body RequestBody requestBody);

    @POST("login")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> login(@Body RequestBody requestBody);

    @POST("logout")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> logout(@Body RequestBody requestBody);

    @POST("queryDeviceList")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> queryDeviceList(@Body RequestBody requestBody);

    @POST("queryStatusForApp")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> queryStatusForApp(@Body RequestBody requestBody);

    @POST("queryDeviceStatus")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> queryDeviceStatus(@Body RequestBody requestBody);

    @POST("openHost")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> openHost(@Body RequestBody requestBody);


    @POST("closeHost")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> closeHost(@Body RequestBody requestBody);

    @POST("setTemp")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> setTemp(@Body RequestBody requestBody);

    @POST("openRoom")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> openRoom(@Body RequestBody requestBody);

    @POST("closeRoom")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> closeRoom(@Body RequestBody requestBody);

    @POST("setRoomTemp")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> setRoomTemp(@Body RequestBody requestBody);

    @POST("queryDeviceConfig")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> queryDeviceConfig(@Body RequestBody requestBody);

    @POST("saveDeviceConfig")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> saveDeviceConfig(@Body RequestBody requestBody);

    @POST("queryOperator")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> queryOperator(@Body RequestBody requestBody);

    @POST("queryAlarm")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> queryAlarm(@Body RequestBody requestBody);

    @POST("queryRoomList")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> queryRoomList(@Body RequestBody requestBody);

    @POST("queryDeviceListForMap")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> queryDeviceListForMap(@Body RequestBody requestBody);

    @POST("statDay")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> statDay(@Body RequestBody requestBody);

    @POST("statDayList")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> statDayList(@Body RequestBody requestBody);

    @POST("statMonth")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> statMonth(@Body RequestBody requestBody);

    @POST("statMonthList")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> statMonthList(@Body RequestBody requestBody);

    @POST("statYear")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> statYear(@Body RequestBody requestBody);

    @POST("statYearList")
    @Headers({"Content-Type: application/json"})
    Observable<ResponseBody> statYearList(@Body RequestBody requestBody);

}

package com.whisht.heatapp.utils.netutils.base;

import com.whisht.heatapp.constant.NetConstant;
import com.whisht.heatapp.utils.CommonUtils;
import com.whisht.heatapp.utils.HttpUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {
    public static int DEFAULT_TIME_OUT = 5;

    static {
        if (CommonUtils.isApkInDebug()) {
            DEFAULT_TIME_OUT = 3;
        }
    }

    public static int DEFAULT_READ_TIME_OUT = 15;
    private Retrofit mRetrofit;
    private boolean isResult = false;
    private Thread checkTh = null;

    private RetrofitServiceManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间
        Interceptor myInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //在这里获取到request后就可以做任何事情了
                Response response = chain.proceed(request);
                return response;
            }
        };
        builder.addInterceptor(myInterceptor);
        checkTh = new Thread(() -> {
            if (CommonUtils.isApkInDebug()) {
                NetConstant.SERVER_IP = NetConstant.SERVER_DIP1;
                NetConstant.SERVER_URL = "http://" + NetConstant.SERVER_DIP1 + ":" + NetConstant.SERVER_PORT + "/app/";

            } else {
                NetConstant.SERVER_IP = NetConstant.SERVER_IP1;
                NetConstant.SERVER_URL = "http://" + NetConstant.SERVER_IP1 + ":" + NetConstant.SERVER_PORT + "/app/";

            }
            String test = HttpUtil.sendGet(NetConstant.SERVER_URL + "test", null, null);
            if (!"test".equals(test)) {
                if (CommonUtils.isApkInDebug()) {
                    NetConstant.SERVER_IP = NetConstant.SERVER_DIP2;
                    NetConstant.SERVER_URL = "http://" + NetConstant.SERVER_DIP2 + ":" + NetConstant.SERVER_PORT + "/app/";
                } else {
                    NetConstant.SERVER_IP = NetConstant.SERVER_IP2;
                    NetConstant.SERVER_URL = "http://" + NetConstant.SERVER_IP2 + ":" + NetConstant.SERVER_PORT + "/app/";

                }
                test = HttpUtil.sendGet(NetConstant.SERVER_URL + "test", null, null);
                if (!"test".equals(test)) {
                    if (CommonUtils.isApkInDebug()) {
                        NetConstant.SERVER_IP = NetConstant.SERVER_DIP1;
                        NetConstant.SERVER_URL = "http://" + NetConstant.SERVER_DIP1 + ":" + NetConstant.SERVER_PORT + "/app/";

                    } else {
                        NetConstant.SERVER_IP = NetConstant.SERVER_IP1;
                        NetConstant.SERVER_URL = "http://" + NetConstant.SERVER_IP1 + ":" + NetConstant.SERVER_PORT + "/app/";
                    }
                }
            }
            isResult = true;
        });
        checkTh.start();
        int all = 50;
        int cou = 0;
        while (!isResult && cou++ < all) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetConstant.SERVER_URL)
                .build();
    }

    private static class SingletonHolder {
        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
    }

    /**
     * 获取RetrofitServiceManager
     *
     * @return
     */
    public static RetrofitServiceManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

}

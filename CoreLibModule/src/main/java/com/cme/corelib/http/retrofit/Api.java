package com.cme.corelib.http.retrofit;


import android.text.TextUtils;

import com.cme.corelib.CoreLib;
import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016-11-25
 *
 * @desc
 */

public class Api {
    protected static final Object monitor = new Object();
    public static Api instance;
    private static Retrofit retrofit;
    private static CommonApiService service;
    private static String apiBaseUrl;

    private boolean isNewCreate = false;

    public Api(OkHttpClient okHttpClient, String baseUrl) {
        this(okHttpClient, baseUrl, false);
    }

    public Api(OkHttpClient okHttpClient, String baseUrl, boolean fileUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson())) // 添加Gson转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .client(okHttpClient)
                .build();
        if (fileUrl) {
            isNewCreate = false;
        } else {
            isNewCreate = true;
        }
    }

    public static Api getInstance(String baseUrl) {
        if (!TextUtils.equals(apiBaseUrl, baseUrl)) {
            instance = null;
        }
        apiBaseUrl = baseUrl;
        if (instance == null) {
            synchronized (Api.class) {
                if (instance == null)
                    instance = new Api(getHttpClient(), baseUrl);
            }
        }
        return instance;
    }

    private static OkHttpClient getHttpClient() {
        //cache url
        File httpCacheDirectory = new File(CoreLib.getContext().getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new ResponseInterceptor())
//                .addInterceptor(new CacheInterceptor())
                .cache(cache)
//                .addInterceptor(new LogInterceptor())
                .build();
    }

    /**
     * 创建新的Api类
     *
     * @param baseUrl
     * @return
     */
    public static Api newApiInstance(String baseUrl) {
        return new Api(getHttpClient(), baseUrl, true);
    }

    public CommonApiService getApiService() {
        synchronized (monitor) {
            if (isNewCreate) {
                isNewCreate = false;
                service = null;
            }
            if (service == null) {
                service = retrofit.create(CommonApiService.class);
            }
            return service;
//            return (CommonApiService) Proxy.newProxyInstance(CommonApiService.class.getClassLoader(), new Class<?>[]{CommonApiService.class},
//                    new ProxyHandler(service));
        }
    }
}

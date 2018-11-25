package com.adc2018.bpmhw3.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 工具类
 * 封装一些网络请求
 */
public class RetrofitTool {

    /**
     * 获取默认的 Retrofit 请求框架
     * @param url 调用api 的 url
     * @return
     */
    public static Retrofit getRetrofit(String url) {
       return  new Retrofit.Builder().
               baseUrl(url)
               .addConverterFactory(GsonConverterFactory.create())
               .build();
    }

}

package com.adc2018.bpmhw3.network.api.amap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AMapApi {

    @GET("regeo")
    Call<ResponseBody>  getAddress(@Query("key") String key, @Query("location") String location);
}

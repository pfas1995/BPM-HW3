package com.adc2018.bpmhw3.network.api.rmp;

import com.adc2018.bpmhw3.entity.rmp.Test;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TestApi {

    @GET("Test")
    Call<Object> getAllTest();

    @GET("Test/{id}")
    Call<Test> getTestById(@Path("id") String  id);

    @POST("Test/")
    Call<Test> addTestItem(@Body Test test);
}

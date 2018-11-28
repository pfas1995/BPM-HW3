package com.adc2018.bpmhw3.network.api.rmp;

import com.adc2018.bpmhw3.entity.rmp.User;
import com.adc2018.bpmhw3.network.entity.UserList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BPMApi {

    @POST("User")
    Call<User> registerUser(@Body User user);

    @GET("User")
    Call<UserList> getUserByName(@Query("User.user_name") String name);

    @GET("User")
    Call<UserList> login(@Query("User.user_name") String name, @Query("User.user_pwd") String pwd);


}

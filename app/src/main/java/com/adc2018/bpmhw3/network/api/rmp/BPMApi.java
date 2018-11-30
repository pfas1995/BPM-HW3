package com.adc2018.bpmhw3.network.api.rmp;

import com.adc2018.bpmhw3.entity.rmp.Card;
import com.adc2018.bpmhw3.entity.rmp.User;
import com.adc2018.bpmhw3.entity.rmp.UserCard;
import com.adc2018.bpmhw3.entity.rmp.list.UserCardList;
import com.adc2018.bpmhw3.network.entity.UserList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BPMApi {

    @POST("User")
    Call<User> registerUser(@Body User user);

    @GET("User")
    Call<UserList> getUserByName(@Query("User.user_name") String name);

    @GET("User")
    Call<UserList> login(@Query("User.user_name") String name, @Query("User.user_pwd") String pwd);

    @POST("Card")
    Call<Card> addCard(@Body Card card);

    @GET("Usercard")
    Call<UserCardList> getUserCardByUserId(@Query("Usercard.user.id") String id);

    @POST("Usercard")
    Call<UserCard> addUserCard(@Body UserCard userCard);


    @PUT("Usercard/{id}")
    Call<UserCard> updateUserCard(@Path("id") String id, @Body UserCard userCard);
}

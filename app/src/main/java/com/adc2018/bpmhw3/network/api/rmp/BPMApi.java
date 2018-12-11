package com.adc2018.bpmhw3.network.api.rmp;

import com.adc2018.bpmhw3.entity.rmp.Card;
import com.adc2018.bpmhw3.entity.rmp.CardGroup;
import com.adc2018.bpmhw3.entity.rmp.Friend;
import com.adc2018.bpmhw3.entity.rmp.Group;
import com.adc2018.bpmhw3.entity.rmp.Meet;
import com.adc2018.bpmhw3.entity.rmp.MeetDocument;
import com.adc2018.bpmhw3.entity.rmp.ShareCard;
import com.adc2018.bpmhw3.entity.rmp.User;
import com.adc2018.bpmhw3.entity.rmp.UserCard;
import com.adc2018.bpmhw3.entity.rmp.UserGroup;
import com.adc2018.bpmhw3.entity.rmp.list.CardGroupList;
import com.adc2018.bpmhw3.entity.rmp.list.FriendList;
import com.adc2018.bpmhw3.entity.rmp.list.MeetList;
import com.adc2018.bpmhw3.entity.rmp.list.ShareCardList;
import com.adc2018.bpmhw3.entity.rmp.list.UserCardList;
import com.adc2018.bpmhw3.entity.rmp.list.UserGroupList;
import com.adc2018.bpmhw3.network.entity.UserList;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @PUT("Card/{id}")
    Call<Card> updateCard(@Path("id") String id, @Body Card card);

    @GET("Usercard")
    Call<UserCardList> getUserCardByUserId(@Query("Usercard.user.id") String id);

    @GET("Usercard")
    Call<UserCardList> getUserCardByCardId(@Query("Usercard.card.id") String id);

    @POST("Usercard")
    Call<UserCard> addUserCard(@Body UserCard userCard);


    @PUT("Usercard/{id}")
    Call<UserCard> updateUserCard(@Path("id") String id, @Body UserCard userCard);

    @POST("Group")
    Call<Group> addGroup(@Body Group group);

    @PUT("Group/{id}")
    Call<Group> updateGroupById(@Path("id") String id, @Body Group group);

    @POST("Cardgroup")
    Call<CardGroup> addCardGroup(@Body CardGroup cardGroup);

    @GET("Cardgroup")
    Call<CardGroupList> getCardGroupByGroupId(@Query("Cardgroup.group.id") String gid);

    @GET("Cardgroup")
    Call<CardGroupList> getCardGroupByCard(@Query("Cardgroup.card.id") String cid);

    @PUT("Cardgroup/{id}")
    Call<CardGroup> updateCardGroup(@Path("id") String id, @Body CardGroup cardGroup);

    @POST("Usergroup")
    Call<UserGroup> addUserGroup(@Body UserGroup userGroup);

    @GET("Usergroup")
    Call<UserGroupList> getUserGroupByUserId(@Query("Usergroup.user.id") String id);

    @GET("Usergroup")
    Call<UserGroupList> getUserGroupByUserIdAndGroupName(@Query("Usergroup.user.id") String uid, @Query("Usergroup.user_group.name") String groupName);

    @PUT("Usergroup/{id}")
    Call<UserGroup> updateUserGroup(@Path("id") String id, @Body UserGroup userGroup);

    @POST("Friend")
    Call<Friend> addFriend(@Body Friend Friend);

    @GET("Friend")
    Call<FriendList> getFriendByUserId(@Query("Friend.user.id") String id);

    @GET("Friend")
    Call<FriendList> getUserABRelation(@Query("Friend.user.id") String aid, @Query("Friend.usercard.user.id") String bid);

    @GET("Friend")
    Call<FriendList> getCardFriend(@Query("Friend.user.id") String uid, @Query("Friend.usercard.card.id") String cid);

    @PUT("Friend/{id}")
    Call<Friend> updateFriend(@Path("id") String id, @Body Friend friend);

    @POST("Sharecard")
    Call<ShareCard> addShareCard(@Body ShareCard shareCard);

    @GET("Sharecard")
    Call<ShareCardList> getSharecardByUserId(@Query("Sharecard.to_user.id") String id);


    @PUT("Sharecard/{id}")
    Call<ShareCard> updateSharecard(@Path("id") String id, @Body ShareCard shareCard);

    @DELETE("Sharecard/{id}")
    Call<ResponseBody> deleteShareCard(@Path("id") String id);

    @POST("Meetdocument")
    Call<MeetDocument> addMeetDocument(@Body MeetDocument meetDocument);


    @POST("Meet")
    Call<Meet> addMeet(@Body Meet meet);

    @GET("Meet")
    Call<MeetList> findMeetByUserAndCard(@Query("Meet.persona.id") String uid, @Query("Meet.personb.id") String cid);
}

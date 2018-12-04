package com.adc2018.bpmhw3.entity.rmp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Friend implements Serializable {

    private static final String type = "Friend";

    private String id;
    private User user;
    @SerializedName("user_friends")
    private List<UserCard> friends;
    private Long create_time;


    public static Friend Factory(User user) {
        Friend friend = new Friend();
        friend.user = user;
        friend.friends = new ArrayList<>();
        friend.create_time = System.currentTimeMillis();
        return friend;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserCard> getFriends() {
        return friends;
    }

    public void setFriends(List<UserCard> friends) {
        this.friends = friends;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void addFriend(UserCard userCard) {
        friends.add(userCard);
    }

    public Boolean containsFriend(UserCard userCard) {
        return friends.contains(userCard);
    }

    @Override
    public String toString() {
        return "Friend{" +
                "user=" + user +
                ", friends=" + friends +
                ", create_time=" + create_time +
                '}';
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }
}

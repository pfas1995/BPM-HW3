package com.adc2018.bpmhw3.entity.rmp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Friend implements Serializable {

    private static final String type = "Friend";

    private String id;
    private User user;
    @SerializedName("usercard")
    private UserCard userCard;
    private Long create_time;


    public static Friend Factory(User user, UserCard userCard) {
        Friend friend = new Friend();
        friend.user = user;
        friend.userCard = userCard;
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

    public UserCard getUserCard() {
        return userCard;
    }

    public void setUserCard(UserCard userCard) {
        this.userCard = userCard;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return Objects.equals(id, friend.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", userCard=" + userCard +
                ", create_time=" + create_time +
                '}';
    }
}

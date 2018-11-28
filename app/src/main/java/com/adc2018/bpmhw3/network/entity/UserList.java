package com.adc2018.bpmhw3.network.entity;

import com.adc2018.bpmhw3.entity.rmp.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserList {

    @SerializedName("User")
    List<User> user;

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserList{" +
                "user=" + user +
                '}';
    }

    public Boolean empty() {
        if(user != null) {
            return user.size() == 0;
        }
        return true;
    }
}

package com.adc2018.bpmhw3.entity.rmp.list;

import com.adc2018.bpmhw3.entity.rmp.Friend;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FriendList {

    @SerializedName("Friend")
    private List<Friend> friends;

    public List<Friend> getFriends() {
        if(friends == null) friends =  new ArrayList<>();
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public Boolean empty() {
        return friends == null || friends.size() == 0;
    }

    public Friend getFirstFriend() {
        if(!this.empty()) {
            return friends.get(0);
        }
        return null;
    }

    @Override
    public String toString() {
        return "FriendList{" +
                "friends=" + friends +
                '}';
    }
}

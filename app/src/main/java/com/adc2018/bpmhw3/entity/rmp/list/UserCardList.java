package com.adc2018.bpmhw3.entity.rmp.list;

import com.adc2018.bpmhw3.entity.rmp.UserCard;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserCardList {

    @SerializedName("Usercard")
    private List<UserCard> usercard;

    public List<UserCard> getUsercard() {
        if(usercard == null)  usercard = new ArrayList<>();
        return usercard;
    }

    public Boolean empty() {
        return usercard == null || usercard.size() == 0;
    }
}

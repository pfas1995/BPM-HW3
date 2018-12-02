package com.adc2018.bpmhw3.entity.rmp.list;

import com.adc2018.bpmhw3.entity.rmp.UserGroup;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserGroupList {

    @SerializedName("Usergroup")
    private List<UserGroup> userGgroup;


    public List<UserGroup> getUserGgroup() {
        return userGgroup;
    }

    public void setUserGgroup(List<UserGroup> userGgroup) {
        this.userGgroup = userGgroup;
    }

    public Boolean empty() {
        return userGgroup == null || userGgroup.size() == 0;
    }

    @Override
    public String toString() {
        return "UserGroupList{" +
                "userGgroup=" + userGgroup +
                '}';
    }
}

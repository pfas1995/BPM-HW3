package com.adc2018.bpmhw3.entity.rmp.list;

import com.adc2018.bpmhw3.entity.rmp.UserGroup;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserGroupList {

    @SerializedName("Usergroup")
    private List<UserGroup> userGgroup;


    public List<UserGroup> getUserGgroup() {
        if(userGgroup == null) userGgroup = new ArrayList<>();
        return userGgroup;
    }

    public void setUserGgroup(List<UserGroup> userGgroup) {
        this.userGgroup = userGgroup;
    }

    public Boolean empty() {
        return userGgroup == null || userGgroup.size() == 0;
    }

    public UserGroup getFirstUserGroup() {
        return userGgroup.get(0);
    }

    @Override
    public String toString() {
        return "UserGroupList{" +
                "userGgroup=" + userGgroup +
                '}';
    }
}

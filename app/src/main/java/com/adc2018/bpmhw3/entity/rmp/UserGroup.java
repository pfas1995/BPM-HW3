package com.adc2018.bpmhw3.entity.rmp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class UserGroup implements Serializable {

    private static final String type = "Usergroup";

    private String id;
    private User user;

    @SerializedName("user_group")
    private Group group;

    private Long create_time;

    public static UserGroup Factory(User user, Group group) {
        UserGroup userGroup = new UserGroup();
        userGroup.user = user;
        userGroup.group = group;
        userGroup.create_time = System.currentTimeMillis();
        return userGroup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public static String getType() {
        return type;
    }

    public boolean defaultGroup() {
       if(user != null && group != null) {
           return Objects.equals(group.getName(), user.getUser_name() + "默认分组");
       }
       return false;
    }


    @Override
    public String toString() {
        return "UserGroup{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", group=" + group +
                '}';
    }

}

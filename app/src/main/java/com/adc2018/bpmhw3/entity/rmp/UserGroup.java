package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;
import java.util.List;

public class UserGroup implements Serializable {

    private static final String type = "UserGroup";

    private String id;
    private User user;
    private List<CardGroup> group;

    public static UserGroup Factory(User user, List<CardGroup> group) {
        UserGroup userGroup = new UserGroup();
        userGroup.user = user;
        userGroup.group = group;
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

    public List<CardGroup> getGroup() {
        return group;
    }

    public void addCardGroup(CardGroup cardGroup) {
        this.group.add(cardGroup);
    }

    public static String getType() {
        return type;
    }


    @Override
    public String toString() {
        return "UserGroup{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", group=" + group +
                '}';
    }

    public void setGroup(List<CardGroup> group) {
        this.group = group;
    }
}

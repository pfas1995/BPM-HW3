package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;
import java.util.List;

public class UserGroup implements Serializable {

    private String id;
    private User user;
    private List<CardGroup> group;

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

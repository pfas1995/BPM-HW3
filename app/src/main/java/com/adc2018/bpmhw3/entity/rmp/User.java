package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String user_name;
    private String user_pwd;
    private String role;

    public static User getInstance(String name, String pwd) {
        User user = new User();
        user.setUser_name(name);
        user.setUser_pwd(pwd);
        user.setRole("common");
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_pwd='" + user_pwd + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;

public class Card implements Serializable {

    private static final String type = "Card";

    private String id;
    private String name;
    private String company;
    private String position;
    private String phone_number;
    private String address;
    private String email;


    public static Card CardFactory(String name, String company, String position, String phone_number,
                                   String address, String email) {
        Card card = new Card();
        card.name = name;
        card.company = company;
        card.position = position;
        card.phone_number = phone_number;
        card.address = address;
        card.email = email;
        return  card;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", position='" + position + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

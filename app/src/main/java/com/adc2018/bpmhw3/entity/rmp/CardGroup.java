package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;
import java.util.List;

public class CardGroup implements Serializable {

    private String id;
    private String group_name;
    private List<Card> cards;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "CardGroup{" +
                "id='" + id + '\'' +
                ", group_name='" + group_name + '\'' +
                ", cards=" + cards +
                '}';
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}

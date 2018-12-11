package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;

public class CardGroup implements Serializable {

    private static final String type = "Cardgroup";

    private String id;
    private Group group;
    private Card card;

    public static CardGroup Factory(Group group, Card card) {
        CardGroup cardGroup = new CardGroup();
        cardGroup.group = group;
        cardGroup.card = card;
        return cardGroup;
    }


    public static String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "CardGroup{" +
                "id='" + id + '\'' +
                ", group=" + group +
                ", card=" + card +
                '}';
    }
}

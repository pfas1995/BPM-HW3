package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardGroup implements Serializable {

    private static final String type = "CardGroup";

    private String id;
    private String group_name;
    private List<Card> cards;

    public static CardGroup Factory(String group_name) {
        CardGroup cardGroup = new CardGroup();
        cardGroup.setGroup_name(group_name);
        cardGroup.setCards(new ArrayList<Card>());
        return cardGroup;
    }

    public static CardGroup Factory(String id, String group_name, List<Card> cards) {
        CardGroup cardGroup = new CardGroup();
        cardGroup.setId(id);
        cardGroup.setGroup_name(group_name);
        cardGroup.setCards(cards);
        return cardGroup;
    }

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

    public static String getType() {
        return type;
    }

    public void addCard(Card card) {
        if(cards == null) {
            cards = new ArrayList<>();
        }
        cards.add(card);
    }

    public void removeCard(Card card) {
        if(cards != null) {
            cards.remove(card);
        }
    }
}

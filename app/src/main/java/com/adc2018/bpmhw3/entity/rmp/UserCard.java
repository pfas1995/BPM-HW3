package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;
import java.util.Objects;

public class UserCard implements Serializable {

    private static final String type = "usercard";

    private String id;
    private User user;
    private Card card;
    private Template template;
    private Long create_time;

    public static final UserCard Factory(User user, Card card, Template template) {
        UserCard userCard = new UserCard();
        userCard.user = user;
        userCard.card = card;
        userCard.template = template;
        userCard.create_time = System.currentTimeMillis();
        return  userCard;
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

    public Card getCard() {
        return card;
    }

    public static String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "UserCard{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", card=" + card +
                ", template=" + template +
                ", create_time=" + create_time +
                '}';
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCard userCard = (UserCard) o;
        return Objects.equals(id, userCard.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;

public class UserCard implements Serializable {
    private String id;
    private User user;
    private Card card;
    private Template template;
    private Long create_time;

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
}

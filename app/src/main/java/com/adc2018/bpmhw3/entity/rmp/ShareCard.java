package com.adc2018.bpmhw3.entity.rmp;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ShareCard {

    private static final String type = "Sharecard";


    private String id;
    private User to_user;

    @SerializedName("cards")
    private Card card;
    private Long create_time;

    public static ShareCard Factory(User to_user, Card card) {
        ShareCard shareCard = new ShareCard();
        shareCard.to_user = to_user;
        shareCard.card = card;
        shareCard.create_time = System.currentTimeMillis();
        return shareCard;
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

    public User getTo_user() {
        return to_user;
    }

    public void setTo_user(User to_user) {
        this.to_user = to_user;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
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
        ShareCard shareCard = (ShareCard) o;
        return Objects.equals(id, shareCard.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ShareCard{" +
                "id='" + id + '\'' +
                ", to_user=" + to_user +
                ", card=" + card +
                ", create_time=" + create_time +
                '}';
    }
}

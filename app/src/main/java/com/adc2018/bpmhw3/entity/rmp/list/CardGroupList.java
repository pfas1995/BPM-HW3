package com.adc2018.bpmhw3.entity.rmp.list;

import com.adc2018.bpmhw3.entity.rmp.CardGroup;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CardGroupList {

    @SerializedName("Cardgroup")
    private List<CardGroup> cardGroups;

    public List<CardGroup> getCardGroups() {
        if(cardGroups == null) cardGroups =  new ArrayList<>();
        return cardGroups;
    }

    public void setCardGroups(List<CardGroup> cardGroups) {
        this.cardGroups = cardGroups;
    }
}

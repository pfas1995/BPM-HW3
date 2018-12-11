package com.adc2018.bpmhw3.entity.rmp.list;

import com.adc2018.bpmhw3.entity.rmp.ShareCard;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ShareCardList {

    @SerializedName("Sharecard")
    private List<ShareCard> shareCards;

    public List<ShareCard> getShareCards() {
        if(shareCards == null) shareCards = new ArrayList<>();
        return shareCards;
    }

    public void setShareCards(List<ShareCard> shareCards) {
        this.shareCards = shareCards;
    }

    public Boolean empty() {
        return shareCards == null || shareCards.size() == 0;
    }

    public ShareCard getFirstSharecard() {
        if(!this.empty()) {
            return shareCards.get(0);
        }
        return null;
    }
}

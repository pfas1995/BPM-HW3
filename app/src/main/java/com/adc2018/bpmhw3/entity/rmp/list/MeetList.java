package com.adc2018.bpmhw3.entity.rmp.list;

import com.adc2018.bpmhw3.entity.rmp.Meet;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MeetList {

    @SerializedName("Meet")
    private List<Meet> meetList;

    public List<Meet> getMeetList() {
        if(meetList == null) meetList = new ArrayList<>();
        return meetList;
    }

    public void setMeetList(List<Meet> meetList) {
        this.meetList = meetList;
    }
}

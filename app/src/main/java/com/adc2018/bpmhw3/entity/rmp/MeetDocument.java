package com.adc2018.bpmhw3.entity.rmp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MeetDocument implements Serializable {

    private static final String type = "Meetdocument";

    private String id;
    private Long meet_time;

    @SerializedName("meet_longitude")
    private Double meet_longitide;
    private Double meet_latitude;
    private String description;


    public static MeetDocument Factory(Long meet_time, Double meet_longitide, Double meet_latitude,
                                       String description) {
        MeetDocument meetDocument = new MeetDocument();
        meetDocument.meet_time = meet_time;
        meetDocument.meet_longitide = meet_longitide;
        meetDocument.meet_latitude = meet_latitude;
        meetDocument.description = description;
        return meetDocument;

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMeet_time() {
        return meet_time;
    }

    public void setMeet_time(Long meet_time) {
        this.meet_time = meet_time;
    }

    public Double getMeet_longitide() {
        return meet_longitide;
    }

    public void setMeet_longitide(Double meet_longitide) {
        this.meet_longitide = meet_longitide;
    }

    public Double getMeet_latitude() {
        return meet_latitude;
    }

    public void setMeet_latitude(Double meet_latitude) {
        this.meet_latitude = meet_latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MeetDocument{" +
                "id='" + id + '\'' +
                ", meet_time=" + meet_time +
                ", meet_longitide=" + meet_longitide +
                ", meet_latitude=" + meet_latitude +
                ", description='" + description + '\'' +
                '}';
    }
}

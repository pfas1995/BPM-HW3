package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;

public class MeetDocument implements Serializable {

    private String id;
    private Long meet_time;
    private Double meet_longitide;
    private Double meet_latitude;
    private String description;

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

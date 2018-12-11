package com.adc2018.bpmhw3.entity.rmp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Meet implements Serializable {

    private static final String type = "Meet";

    private String id;
    private User persona;
    private Card personb;

    @SerializedName("meetdocument")
    private MeetDocument meetDocument;

    public static Meet Factory(User persona, Card card, MeetDocument meetDocument) {
        Meet meet = new Meet();
        meet.setPersona(persona);
        meet.setPersonb(card);
        meet.setMeetDocument(meetDocument);
        return meet;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getPersona() {
        return persona;
    }

    public void setPersona(User persona) {
        this.persona = persona;
    }

    public Card getPersonb() {
        return personb;
    }

    public void setPersonb(Card personb) {
        this.personb = personb;
    }

    public MeetDocument getMeetDocument() {
        return meetDocument;
    }

    public void setMeetDocument(MeetDocument meetDocument) {
        this.meetDocument = meetDocument;
    }

    public static String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Meet{" +
                "id='" + id + '\'' +
                ", persona=" + persona +
                ", personb=" + personb +
                ", meetDocument=" + meetDocument +
                '}';
    }
}

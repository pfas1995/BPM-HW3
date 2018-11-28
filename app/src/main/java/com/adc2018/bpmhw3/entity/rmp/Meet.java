package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;

public class Meet implements Serializable {

    private String id;
    private User persona;
    private Card personb;
    private MeetDocument meetDocument;

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

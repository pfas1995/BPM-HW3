package com.adc2018.bpmhw3.entity.rmp;

import java.io.Serializable;

public class Template implements Serializable {
    private String id;
    private String tname;
    private String turi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTuri() {
        return turi;
    }

    public void setTuri(String turi) {
        this.turi = turi;
    }

    @Override
    public String toString() {
        return "Template{" +
                "id='" + id + '\'' +
                ", tname='" + tname + '\'' +
                ", turi='" + turi + '\'' +
                '}';
    }
}

package com.adc2018.bpmhw3.entity;

public class Test {
    String id;
    String tid;
    String tname;

    @Override
    public String toString() {
        return "Test{" +
                "id='" + id + '\'' +
                ", tid='" + tid + '\'' +
                ", tname='" + tname + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

}

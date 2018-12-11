package com.adc2018.bpmhw3.entity.rmp;

public class Group {
    private static final String type = "Group";

    private String id;
    private String name;


    public static Group Factory(String name) {
        Group group = new Group();
        group.name = name;
        return group;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

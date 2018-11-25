package com.adc2018.bpmhw3.entity;

public class CardImage {
    String image;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "CardImage{" +
                "image='" + image + '\'' +
                '}';
    }
}

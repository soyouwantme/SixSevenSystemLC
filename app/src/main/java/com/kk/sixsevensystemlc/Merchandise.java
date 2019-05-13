package com.kk.sixsevensystemlc;

public class Merchandise {

    private String name;

    private int imageId;

    private Double price;

    public Merchandise(String name,int imageId,Double price){
        this.name=name;
        this.imageId=imageId;
        this.price=price;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return imageId;
    }

    public Double getPrice(){
        return price;
    }
}

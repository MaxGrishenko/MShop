package com.bandit.mshop.adapters;

import android.graphics.Bitmap;

public class ItemAdapterModel {
    private Integer[] id;
    private String[] name;
    private Integer[] price;
    private Bitmap[] image;

    public ItemAdapterModel(Integer[] id, String[] name, Integer[] price, Bitmap[] image){
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Integer[] getId() {
        return id;
    }
    public String[] getName() {
        return name;
    }
    public Integer[] getPrice() {
        return price;
    }
    public Bitmap[] getImage() {
        return image;
    }
}

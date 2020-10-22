package com.bandit.mshop.adapters;

import android.graphics.Bitmap;

public class ItemAdapterModel {
    private Integer[] id;
    private String[] name;
    private Integer[] price;
    private Bitmap[] image;
    private Integer[] discount;

    public ItemAdapterModel(Integer[] id, String[] name, Integer[] price, Bitmap[] image, Integer[] discount){
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.discount = discount;
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
    public Integer[] getDiscount() {
        return discount;
    }
}

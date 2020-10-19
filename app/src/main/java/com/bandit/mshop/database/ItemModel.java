package com.bandit.mshop.database;

import android.graphics.Bitmap;

public class ItemModel {
    private String name;
    private Bitmap image;
    private String description;
    private int price;

    public ItemModel(String name, Bitmap image, String description, int price){
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public Bitmap getImage() {
        return image;
    }
    public int getPrice() {
        return price;
    }
    public String getDescription() {
        return description;
    }
}
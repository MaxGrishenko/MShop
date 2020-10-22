package com.bandit.mshop.database;

import android.graphics.Bitmap;

public class ItemModel {
    private int id;
    private String name;
    private Bitmap image;
    private String description;
    private int price;
    private int discount;

    public ItemModel(int id, String name, Bitmap image, String description, int price, int discount){
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.discount = discount;
    }

    public int getId() {
        return id;
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
    public int getDiscount() {
        return discount;
    }
}
package com.bandit.mshop.adapters;

import android.graphics.Bitmap;

public class CategoryAdapterModel {
    private Integer[] id;
    private String[] name;
    private Bitmap[] image;


    public CategoryAdapterModel(Integer[] id, String[] name, Bitmap[] image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Integer[] getId() {
        return this.id;
    }
    public String[] getName() {
        return this.name;
    }
    public Bitmap[] getImage() {
        return image;
    }
}

package com.bandit.mshop.adapters;

public class CartAdapterModel {
    private Integer[] id;
    private String[] name;
    private Integer[] price;
    private Integer[] amount;

    public CartAdapterModel(Integer[] id, String[] name, Integer[] price, Integer[] amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public Integer[] getId() {
        return this.id;
    }
    public String[] getName() {
        return this.name;
    }
    public Integer[] getPrice() { return this.price; }
    public Integer[] getAmount() { return this.amount; }

}
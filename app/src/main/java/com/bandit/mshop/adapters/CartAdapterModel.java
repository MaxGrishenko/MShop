package com.bandit.mshop.adapters;

public class CartAdapterModel {
    private Integer[] id;
    private String[] name;
    private Integer[] price;
    private Integer[] discount;
    private Integer[] amount;

    public CartAdapterModel(Integer[] id, String[] name, Integer[] price, Integer[] discount, Integer[] amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.amount = amount;
    }

    public Integer[] getId() {
        return this.id;
    }
    public String[] getName() {
        return this.name;
    }
    public Integer[] getPrice() { return this.price; }
    public Integer[] getDiscount() {
        return discount;
    }
    public Integer[] getAmount() { return this.amount; }

}
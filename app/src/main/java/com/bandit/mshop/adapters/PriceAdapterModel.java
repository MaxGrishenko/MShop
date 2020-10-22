package com.bandit.mshop.adapters;

public class PriceAdapterModel {
    private Integer[] itemId;
    private String[] categoryName;
    private String[] itemName;
    private Integer[] itemPrice;
    private Integer[] itemDiscount;

    public PriceAdapterModel(Integer[] itemId, String[] categoryName, String[] itemName, Integer[] itemPrice, Integer[] itemDiscount) {
        this.itemId = itemId;
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDiscount = itemDiscount;
    }

    public Integer[] getItemId() {
        return itemId;
    }
    public String[] getCategoryName() {
        return categoryName;
    }
    public String[] getItemName() {
        return itemName;
    }
    public Integer[] getItemPrice() {
        return itemPrice;
    }
    public Integer[] getItemDiscount() {
        return itemDiscount;
    }
}
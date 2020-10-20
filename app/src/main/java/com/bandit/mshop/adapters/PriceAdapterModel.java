package com.bandit.mshop.adapters;

public class PriceAdapterModel {
    private Integer[] itemId;
    private String[] categoryName;
    private String[] itemName;
    private Integer[] itemPrice;

    public PriceAdapterModel(Integer[] itemId, String[] categoryName, String[] itemName, Integer[] itemPrice) {
        this.itemId = itemId;
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
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
}
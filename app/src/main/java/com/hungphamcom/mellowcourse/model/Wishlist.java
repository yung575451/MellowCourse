package com.hungphamcom.mellowcourse.model;

public class Wishlist {
    private String itemId;

    public Wishlist( String itemId) {
        this.itemId = itemId;
    }

    public Wishlist() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}

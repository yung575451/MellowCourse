package com.hungphamcom.mellowcourse.model;


import java.util.List;

public class Wishlist {

    private List<String> WishItem;

    public Wishlist(List<String> wishItem) {
        WishItem = wishItem;
    }

    public Wishlist() {
    }

    public List<String> getWishItem() {
        return WishItem;
    }

    public void setWishItem(List<String> wishItem) {
        WishItem = wishItem;
    }
}

package com.me.challange.milan.challangeme.GetterSetter;

/**
 * Created by milan on 11/26/2017.
 */
public class GiftCardsRows {
    String amt,coins,brandName,imageUrl;

    public GiftCardsRows() {

    }

    public GiftCardsRows(String amt, String coins, String brandName, String imageUrl) {
        this.amt = amt;
        this.coins = coins;
        this.brandName = brandName;
        this.imageUrl = imageUrl;
    }

    public String getAmt() {
        return amt;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getCoins() {
        return coins;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}

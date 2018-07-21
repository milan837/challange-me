package com.me.challange.milan.challangeme.GetterSetter;

/**
 * Created by milan on 11/24/2017.
 */
public class ShopCoinRows {
    String amt,coin;

    public ShopCoinRows() {

    }

    public ShopCoinRows(String amt, String coin) {
        this.amt = amt;
        this.coin = coin;
    }

    public String getCoin() {
        return coin;
    }

    public String getAmt() {
        return amt;
    }
}

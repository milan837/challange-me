package com.me.challange.milan.challangeme.GetterSetter;

/**
 * Created by milan on 11/8/2017.
 */
public class UserLoginList {
    String coins,facebookId,facebookToken,gender,imageUrl,level,looseMatch,name,totalChallange,winMatch;

    public UserLoginList() {
    }

    public UserLoginList(String coins, String facebookId, String facebookToken, String gender, String imageUrl, String level, String looseMatch, String name, String totalChallange, String winMatch) {
        this.coins = coins;
        this.facebookId = facebookId;
        this.facebookToken = facebookToken;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.level = level;
        this.looseMatch = looseMatch;
        this.name = name;
        this.totalChallange = totalChallange;
        this.winMatch = winMatch;
    }

    public String getWinMatch() {
        return winMatch;
    }

    public String getTotalChallange() {
        return totalChallange;
    }

    public String getLooseMatch() {
        return looseMatch;
    }

    public String getLevel() {
        return level;
    }

    public String getCoins() {
        return coins;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public String getGender() {
        return gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}

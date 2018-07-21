package com.me.challange.milan.challangeme.GetterSetter;

/**
 * Created by milan on 11/18/2017.
 */
public class ChallangeResultListRows {
    String challange_coins, challange_date,challange_key,challange_looser,challange_points,challange_subject,challange_time,challange_type,challange_winner;

    public ChallangeResultListRows() {

    }

    public ChallangeResultListRows(String challange_coins, String challange_date, String challange_key, String challange_looser, String challange_points, String challange_subject, String challange_time, String challange_type, String challange_winner) {
        this.challange_coins = challange_coins;
        this.challange_date = challange_date;
        this.challange_key = challange_key;
        this.challange_looser = challange_looser;
        this.challange_points = challange_points;
        this.challange_subject = challange_subject;
        this.challange_time = challange_time;
        this.challange_type = challange_type;
        this.challange_winner = challange_winner;
    }

    public String getChallange_subject() {
        return challange_subject;
    }

    public String getChallange_points() {
        return challange_points;
    }

    public String getChallange_time() {
        return challange_time;
    }

    public String getChallange_coins() {
        return challange_coins;
    }

    public String getChallange_date() {
        return challange_date;
    }

    public String getChallange_key() {
        return challange_key;
    }

    public String getChallange_looser() {
        return challange_looser;
    }

    public String getChallange_type() {
        return challange_type;
    }

    public String getChallange_winner() {
        return challange_winner;
    }
}

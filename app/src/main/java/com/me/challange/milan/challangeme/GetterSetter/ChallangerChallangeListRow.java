package com.me.challange.milan.challangeme.GetterSetter;

/**
 * Created by milan on 11/17/2017.
 */
public class ChallangerChallangeListRow {
    String challange_coins,challange_created_by,challange_created_date,challange_created_for,challange_points,challange_subject,challange_time;

    public ChallangerChallangeListRow() {

    }

    public ChallangerChallangeListRow(String challange_coins, String challange_created_by, String challange_created_date,String challange_created_for, String challange_points, String challange_subject, String challange_time) {
        this.challange_coins = challange_coins;
        this.challange_created_by = challange_created_by;
        this.challange_created_date = challange_created_date;
        this.challange_points = challange_points;
        this.challange_subject = challange_subject;
        this.challange_time = challange_time;
        this.challange_created_for=challange_created_for;
    }

    public String getChallange_created_by() {
        return challange_created_by;
    }

    public String getChallange_time() {
        return challange_time;
    }

    public String getChallange_coins() {
        return challange_coins;
    }

    public String getChallange_created_date() {
        return challange_created_date;
    }

    public String getChallange_points() {
        return challange_points;
    }

    public String getChallange_subject() {
        return challange_subject;
    }

    public String getChallange_created_for() {
        return challange_created_for;
    }
}

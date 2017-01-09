package com.company;

import java.util.ArrayList;

/**
 * Created by michaelmernin on 12/8/16.
 */
public class DateLog {

    int id;
    int month;
    int day;
    ArrayList<Observations> aryObs = new ArrayList<>();

    public DateLog(int id, int month, int day, ArrayList<Observations> aryObs) {
        this.id = id;
        this.month = month;
        this.day = day;
        this.aryObs = aryObs;
    }

    public DateLog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<Observations> getAryObs() {
        return aryObs;
    }

    public void setAryObs(ArrayList<Observations> aryObs) {
        this.aryObs = aryObs;
    }
}








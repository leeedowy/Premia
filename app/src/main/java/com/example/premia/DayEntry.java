package com.example.premia;

import java.util.Calendar;

public class DayEntry {

    public static final String WORK_ACTIVE = "active";
    public static final String WORK_ENDED = "ended";

    private Calendar date;
    private short lineSum;
    private int millisWorked;
    private String status;

    public DayEntry(Calendar date, String status) {
        this.date = date;
        this.lineSum = 0;
        this.millisWorked = 0;
        this.status = status;
    }

    public void save() {

    }

    public void increment() {
        lineSum++;
    }

    public static DayEntry loadActiveEntry() {
        return null;
    }

    public static boolean isActiveEntryAvailible() {
        return false;
    }

    @Override
    public String toString() {
        return "DayEntry{" +
                "date=" + date +
                ", lineSum=" + lineSum +
                ", millisWorked=" + millisWorked +
                ", status='" + status + '\'' +
                '}';
    }
}

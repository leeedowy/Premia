package com.example.premia;

import java.util.Calendar;

public class DayEntry {

    public static final String WORK_ACTIVE = "active";
    public static final String WORK_ENDED = "ended";

    private Calendar workStartDate;
    private short lineSum;
    private int millisWorked;
    private String status;

    public DayEntry() {
        this.workStartDate = Calendar.getInstance();
        workStartDate.set(Calendar.SECOND, 0);
        workStartDate.set(Calendar.MILLISECOND, 0);
        this.lineSum = 0;
        this.millisWorked = 0;
        this.status = WORK_ACTIVE;
    }

    public void save() {

    }

    public void increment() {
        lineSum++;
    }

    public void setWorkStartDate(int year, int month, int day) {
        workStartDate.set(year, month, day);
    }

    public void setWorkStartTime(int hour, int minute) {
        workStartDate.set(Calendar.HOUR_OF_DAY, hour);
        workStartDate.set(Calendar.MINUTE, minute);
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
                "workStartDate=" + workStartDate +
                ", lineSum=" + lineSum +
                ", millisWorked=" + millisWorked +
                ", status='" + status + '\'' +
                '}';
    }
}

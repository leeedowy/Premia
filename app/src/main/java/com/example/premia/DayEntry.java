package com.example.premia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayEntry {

    public static final String WORK_ACTIVE = "active";
    public static final String WORK_PAUSED = "paused";
    public static final String WORK_ENDED = "ended";

    private Calendar workStartDate;
    private Calendar workEndDate;
    private int workLengthMillis;
    private Map<String, Calendar> currentPause;
    private List<Map<String, Calendar>> pauseList;
    private short lineSum;
    private String status;

    public DayEntry() {
        this.workStartDate = Calendar.getInstance();
        workStartDate.set(Calendar.SECOND, 0);
        workStartDate.set(Calendar.MILLISECOND, 0);

        workLengthMillis = 7 * 3600 * 1000;

        currentPause = new HashMap<>();
        pauseList = new ArrayList<>();

        this.lineSum = 0;

        this.status = WORK_ACTIVE;
    }

    public void save() {

    }

    public void pause() {
        status = WORK_PAUSED;
        currentPause.put("start", Calendar.getInstance());
    }

    public void resume() {
        status = DayEntry.WORK_ACTIVE;
        currentPause.put("end", Calendar.getInstance());
        pauseList.add(currentPause);
        currentPause = new HashMap<>();
    }

    public void calculateAverage() {

    }

    public void increment() {
        lineSum++;
    }

    public void decrement() {
        if (lineSum > 0) {
            lineSum--;
        }
    }

    public void setWorkStartDate(int year, int month, int day) {
        workStartDate.set(year, month, day);
    }

    public void setWorkStartTime(int hour, int minute) {
        workStartDate.set(Calendar.HOUR_OF_DAY, hour);
        workStartDate.set(Calendar.MINUTE, minute);
    }

    public short getLineSum() {
        return lineSum;
    }

    public String getStatus() {
        return status;
    }

    public static DayEntry loadActiveEntry() {
        return null;
    }

    public static boolean isActiveEntryAvailable() {
        return false;
    }

    @Override
    public String toString() {
        return "DayEntry{" +
                "workStartDate=" + workStartDate +
                ", workEndDate=" + workEndDate +
                ", workLengthMillis=" + workLengthMillis +
                ", currentPause=" + currentPause.toString() +
                ", pauseList=" + pauseList.toString() +
                ", lineSum=" + lineSum +
                ", status='" + status + '\'' +
                '}';
    }
}

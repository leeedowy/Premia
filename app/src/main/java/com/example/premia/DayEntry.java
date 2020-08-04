package com.example.premia;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DayEntry {
    public static final String TAG = "DayEntry";

    public static final String WORK_ACTIVE = "active";
    public static final String WORK_PAUSED = "paused";
    public static final String WORK_ENDED = "ended";

    private Calendar workStartDate;
    private Calendar workEndDate;
    private short workLengthHours;
    private HashMap<String, Calendar> currentPause;
    private ArrayList<HashMap<String, Calendar>> pauseList;
    private short lineSum;
    private String status;

    public DayEntry() {
        this.workStartDate = Calendar.getInstance();
        workStartDate.set(Calendar.SECOND, 0);
        workStartDate.set(Calendar.MILLISECOND, 0);

        workLengthHours = 7;

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

    public String calculateAverage() {
        Calendar now = Calendar.getInstance();

        long diffInMillis = now.getTimeInMillis() - workStartDate.getTimeInMillis();

        Log.i(TAG, "Before pauses: " + (diffInMillis / 1000) + " seconds");

        for (HashMap<String, Calendar> pause : pauseList) {
            Calendar pauseStart = pause.get("start");
            Calendar pauseEnd = pause.get("end");

            diffInMillis -= pauseEnd.getTimeInMillis() - pauseStart.getTimeInMillis();
        }

        Log.i(TAG, "After pauses: " + (diffInMillis / 1000) + " seconds");

        double diffInHours = diffInMillis / 1000.0 / 3600.0;

        return String.format("%.1f", lineSum / diffInHours);
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

    public Calendar getWorkStartDate() {
        return workStartDate;
    }

    public Calendar getWorkEndDate() {
        Calendar workEndDate = (Calendar) workStartDate.clone();
        workEndDate.add(Calendar.HOUR_OF_DAY, workLengthHours);
        return workEndDate;
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
                ", workLengthHours=" + workLengthHours +
                ", currentPause=" + currentPause.toString() +
                ", pauseList=" + pauseList.toString() +
                ", lineSum=" + lineSum +
                ", status='" + status + '\'' +
                '}';
    }
}

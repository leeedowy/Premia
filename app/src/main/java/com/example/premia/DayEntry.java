package com.example.premia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DayEntry {
    public static final String TAG = "DayEntry";

    public static final String WORK_ACTIVE = "active";
    public static final String WORK_PAUSED = "paused";
    public static final String WORK_ENDED = "ended";

    private Calendar workStartDate;
    private short workLengthHours;

    private HashMap<String, Calendar> currentPause;
    private ArrayList<HashMap<String, Calendar>> pauseList;

    private short linesDone;
    private String status;

    public DayEntry() {
        this.workStartDate = Calendar.getInstance();
        workStartDate.set(Calendar.SECOND, 0);
        workStartDate.set(Calendar.MILLISECOND, 0);

        workLengthHours = 8;

        currentPause = new HashMap<>();
        pauseList = new ArrayList<>();

        this.linesDone = 0;
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

    public short calculateNormPercentage() {
        if (linesDone == 0) {
            return 0;
        }

        Calendar now = Calendar.getInstance();
        long diffInMillis = now.getTimeInMillis() - workStartDate.getTimeInMillis();

        for (HashMap<String, Calendar> pause : pauseList) {
            Calendar pauseStart = pause.get("start");
            Calendar pauseEnd = pause.get("end");

            diffInMillis -= pauseEnd.getTimeInMillis() - pauseStart.getTimeInMillis();
        }

        double diffInHours = diffInMillis / 1000.0 / 3600.0;
        double normPercentage = linesDone / diffInHours / 52.0 * 100;

        return (short) normPercentage;
    }

    public void increment() {
        linesDone++;
    }

    public void decrement() {
        if (linesDone > 0) {
            linesDone--;
        }
    }

    public void setWorkStartDate(int year, int month, int day) {
        workStartDate.set(year, month, day);
    }

    public void setWorkStartTime(int hour, int minute) {
        workStartDate.set(Calendar.HOUR_OF_DAY, hour);
        workStartDate.set(Calendar.MINUTE, minute);
    }

    public short getLinesDone() {
        return linesDone;
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
                ", workLengthHours=" + workLengthHours +
                ", currentPause=" + currentPause.toString() +
                ", pauseList=" + pauseList.toString() +
                ", linesDone=" + linesDone +
                ", status='" + status + '\'' +
                '}';
    }
}

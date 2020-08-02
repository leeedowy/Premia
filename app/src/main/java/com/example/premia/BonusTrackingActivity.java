package com.example.premia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class BonusTrackingActivity extends AppCompatActivity
        implements OnPartialDatePickedListener, OnCompleteDatePickedListener, View.OnClickListener {
    public static final String TAG = "BonusTrackingActivity";

    private DayEntry activeEntry;

    private TextView lineSumTxtV;
    private TextView averageTxtV;
    private TextView startTimeTxtV;
    private TextView endTimeTxtV;
    private TextView currentTimeTxtV;
    private TextView statusTxtV;

    private Button incrementBtn;
    private Button decrementBtn;
    private Button pauseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_tracking);

        lineSumTxtV = findViewById(R.id.lineSumTextView);
        averageTxtV = findViewById(R.id.averageTextView);
        startTimeTxtV = findViewById(R.id.startTimeTextView);
        endTimeTxtV = findViewById(R.id.endTimeTextView);
        currentTimeTxtV = findViewById(R.id.currentTimeTextView);
        statusTxtV = findViewById(R.id.statusTextView);

        incrementBtn = findViewById(R.id.incrementButton);
        decrementBtn = findViewById(R.id.decrementButton);
        pauseBtn = findViewById(R.id.pauseButton);

        incrementBtn.setOnClickListener(this);
        decrementBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (DayEntry.isActiveEntryAvailable()) {
            activeEntry = DayEntry.loadActiveEntry();
        } else {
            activeEntry = new DayEntry();

            DialogFragment newDateFragment = new DatePickerFragment(this);
            newDateFragment.show(getSupportFragmentManager(), "datePicker");
        }

        refreshScreenData();
    }

    public void refreshScreenData() {
        lineSumTxtV.setText(String.valueOf(activeEntry.getLineSum()));

        switch (activeEntry.getStatus()) {
            case DayEntry.WORK_ACTIVE:
                statusTxtV.setText(R.string.status_active);
                break;
            case DayEntry.WORK_PAUSED:
                statusTxtV.setText(R.string.status_paused);
                break;
        }

        startTimeTxtV.setText(toReadableTime(activeEntry.getWorkStartDate()));
        endTimeTxtV.setText(toReadableTime(activeEntry.getWorkEndDate()));
    }

    public String toReadableTime(Calendar date) {
        String result = "";

        result += date.get(Calendar.HOUR_OF_DAY) + ":";

        if (date.get(Calendar.MINUTE) < 10) {
            result += 0 + date.get(Calendar.MINUTE) + "";
        } else {
            result += date.get(Calendar.MINUTE) + "";
        }

        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.incrementButton:
                activeEntry.increment();
                break;
            case R.id.decrementButton:
                activeEntry.decrement();
                break;
            case R.id.pauseButton:
                if (activeEntry.getStatus().equals(DayEntry.WORK_ACTIVE)) {
                    activeEntry.pause();
                } else {
                    activeEntry.resume();
                }
                Log.i(TAG, activeEntry.toString());
                break;
            default:
        }

        refreshScreenData();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private static OnPartialDatePickedListener callback;

        DatePickerFragment(OnPartialDatePickedListener listener) {
            callback = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            callback.onPartialDatePicked(year, month, day);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.pick_date_canceled), Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private static OnCompleteDatePickedListener callback;

        TimePickerFragment(OnCompleteDatePickedListener listener) {
            callback = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            callback.onCompleteDatePicked(hourOfDay, minute);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.pick_date_canceled), Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Override
    public void onPartialDatePicked(int year, int month, int day) {
        activeEntry.setWorkStartDate(year, month, day);

        DialogFragment newTimeFragment = new TimePickerFragment(this);
        newTimeFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onCompleteDatePicked(int hour, int minute) {
        activeEntry.setWorkStartTime(hour, minute);
    }
}

interface OnPartialDatePickedListener {
    void onPartialDatePicked(int year, int month, int day);
}

interface OnCompleteDatePickedListener {
    void onCompleteDatePicked(int hour, int minute);
}

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
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class BonusTrackingActivity extends AppCompatActivity
        implements OnPartialDatePickedListener, OnCompleteDatePickedListener {
    public static final String TAG = "BonusTrackingActivity";

    private DayEntry activeEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_tracking);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (DayEntry.isActiveEntryAvailible()) {
            activeEntry = DayEntry.loadActiveEntry();
        } else {
            activeEntry = new DayEntry();

            DialogFragment newDateFragment = new DatePickerFragment(this);
            newDateFragment.show(getSupportFragmentManager(), "datePicker");
        }
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

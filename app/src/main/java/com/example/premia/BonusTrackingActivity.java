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

public class BonusTrackingActivity extends AppCompatActivity {
    public static final String TAG = "BonusTrackingActivity";

    private DayEntry acitveEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_tracking);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (DayEntry.isActiveEntryAvailible()) {
            acitveEntry = DayEntry.loadActiveEntry();
        } else {
//        DialogFragment newTimeFragment = new TimePickerFragment();
//        newTimeFragment.show(getSupportFragmentManager(), "timePicker");

            DialogFragment newDateFragment = new DatePickerFragment();
            newDateFragment.show(getSupportFragmentManager(), "datePicker");

        }

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

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
            Log.i(TAG, year + " " + month + " " + day);
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
            Log.i(TAG, hourOfDay + ":" + minute);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.pick_date_canceled), Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }
}

package com.example.premia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class BonusTrackingActivity extends AppCompatActivity
        implements OnPartialDatePickedListener, OnCompleteDatePickedListener,
                   View.OnClickListener, Runnable, View.OnLongClickListener, GoBackListener {
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

    private MediaPlayer incrementMP;
    private MediaPlayer decrementMP;
    private MediaPlayer pauseMP;

    private Handler handler;

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
        decrementBtn.setOnLongClickListener(this);
        pauseBtn.setOnLongClickListener(this);

        incrementMP = MediaPlayer.create(this, R.raw.increment);
        decrementMP = MediaPlayer.create(this, R.raw.decrement);
        pauseMP = MediaPlayer.create(this, R.raw.pause);

        handler = new Handler();

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(this, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(this);
    }

    public void setScreenData() {
        lineSumTxtV.setText(String.valueOf(activeEntry.getLineSum()));

        switch (activeEntry.getStatus()) {
            case DayEntry.WORK_ACTIVE:
                statusTxtV.setText(R.string.status_active);
                break;
            case DayEntry.WORK_PAUSED:
                statusTxtV.setText(R.string.status_paused);
                break;
        }

        String startTime = toReadableTime(activeEntry.getWorkStartDate());
        startTimeTxtV.setText(startTime.substring(0, startTime.length() - 3));

        String endTime = toReadableTime(activeEntry.getWorkEndDate());
        endTimeTxtV.setText(endTime.substring(0, endTime.length() - 3));
    }

    public String toReadableTime(Calendar date) {
        String result = "";

        result += date.get(Calendar.HOUR_OF_DAY) + ":";

        if (date.get(Calendar.MINUTE) < 10) {
            result += "0" + date.get(Calendar.MINUTE);
        } else {
            result += date.get(Calendar.MINUTE) + "";
        }

        result += ":";

        if (date.get(Calendar.SECOND) < 10) {
            result += "0" + date.get(Calendar.SECOND);
        } else {
            result += date.get(Calendar.SECOND) + "";
        }

        return result;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.incrementButton) {
            activeEntry.increment();
            lineSumTxtV.setText(String.valueOf(activeEntry.getLineSum()));
            incrementMP.start();

            incrementBtn.setClickable(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    incrementBtn.setClickable(true);
                }
            }, 500);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.decrementButton:
                activeEntry.decrement();
                lineSumTxtV.setText(String.valueOf(activeEntry.getLineSum()));
                decrementMP.start();
                break;
            case R.id.pauseButton:
                if (activeEntry.getStatus().equals(DayEntry.WORK_ACTIVE)) {
                    activeEntry.pause();
                    incrementBtn.setEnabled(false);
                    decrementBtn.setEnabled(false);
                    statusTxtV.setText(R.string.status_paused);
                    statusTxtV.setTextColor(Color.YELLOW);
                } else {
                    activeEntry.resume();
                    incrementBtn.setEnabled(true);
                    decrementBtn.setEnabled(true);
                    statusTxtV.setText(R.string.status_active);
                    statusTxtV.setTextColor(Color.GREEN);
                }

                pauseMP.start();
                break;
            default:
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        BackButtonDialogFragment dialog = new BackButtonDialogFragment(this);
        dialog.show(getSupportFragmentManager(), "backButtonDialog");
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

    public static class BackButtonDialogFragment extends DialogFragment {

        private GoBackListener callback;

        public BackButtonDialogFragment(GoBackListener callback) {
            this.callback = callback;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.back_button_dialog_title)
                    .setPositiveButton(R.string.back_button_dialog_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            callback.onGoBack();
                        }
                    })
                    .setNegativeButton(R.string.back_button_dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
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
        setScreenData();
    }

    @Override
    public void onGoBack() {
        super.onBackPressed();
    }

    @Override
    public void run() {
        if (activeEntry.getStatus().equals(DayEntry.WORK_ACTIVE)) {
            String averageStr = activeEntry.calculateAverage();
            double average = Double.parseDouble(averageStr);

            if (average < 52.0) {
                averageTxtV.setBackgroundColor(Color.RED);
            } else if (average < 73.0) {
                averageTxtV.setBackgroundColor(Color.YELLOW);
            } else if (average < 83.0) {
                averageTxtV.setBackgroundColor(Color.GREEN);
            } else {
                averageTxtV.setBackgroundColor(Color.BLUE);
            }

            averageTxtV.setText(getString(R.string.average_placeholder, averageStr));
        }
        currentTimeTxtV.setText(toReadableTime(Calendar.getInstance()));

        handler.postDelayed(this, 1000);
    }
}

interface OnPartialDatePickedListener {
    void onPartialDatePicked(int year, int month, int day);
}

interface OnCompleteDatePickedListener {
    void onCompleteDatePicked(int hour, int minute);
}

interface GoBackListener {
    void onGoBack();
}

package com.example.sleeptracker;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {
    private TextView timeStart, timeEnd;
    private TextView sleepTime;
    private TextView avgSleepTime;

    private int sleepStartHour = -1, sleepStartMinutes = -1;
    private int sleepEndHour = -1, sleepEndMinutes = -1;

    private int[] totalSleepingTime;
    private ArrayList<int[]> sleepingTimeHistory = null;
    private Button undoBtn;
    private View mininumSleepWarningIcon;

    private SharedPreferences appStorage;
    private static final String KEY_SLEEPING_TIME_HISTORY = "sleeping_time_history";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        appStorage = getApplicationContext().getSharedPreferences("SleepTracker", Context.MODE_PRIVATE);

        timeStart = findViewById(R.id.start_time);
        timeEnd = findViewById(R.id.end_time);
        sleepTime = findViewById(R.id.user_sleep_time);
        avgSleepTime = findViewById(R.id.daily_avg_content);

        timeStart.setOnClickListener((v) -> showTimePickerDialog(true));
        timeEnd.setOnClickListener((v) -> showTimePickerDialog(false));

        Button submitBtn = findViewById(R.id.submit_btn);
        undoBtn = findViewById(R.id.undo_btn);

        submitBtn.setOnClickListener((v) -> {
            if (totalSleepingTime != null) {
                submit(totalSleepingTime);
            } else {
                Toast.makeText(this, "Please set sleeping start and end time", Toast.LENGTH_SHORT).show();
            }
        });
        undoBtn.setOnClickListener((v) -> undoLastSave());

        mininumSleepWarningIcon = findViewById(R.id.warning_icon);

        View settingsBtn = findViewById(R.id.fab_settings);
        settingsBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivityForResult(intent, 101);
        });

        // load sleeping time history from SharedPreferences and update UI with sleep statistics
        loadSleepingTimeHistory();
        calculateAverageSleepingTime();
    }

    public void showTimePickerDialog(boolean startEnd) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);  // 24-hour format
        int minute = calendar.get(Calendar.MINUTE);

        // Create and show the TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                MainActivity.this,
                (view, hourOfDay, minutes) -> {
                    String timeStr = formatTime(hourOfDay, minutes);

                    if (startEnd) {
                        timeStart.setText(timeStr);
                        sleepStartHour = hourOfDay;
                        sleepStartMinutes = minutes;
                    } else {
                        timeEnd.setText(timeStr);
                        sleepEndHour = hourOfDay;
                        sleepEndMinutes = minutes;
                    }

                    if (sleepStartHour != -1 && sleepStartMinutes != -1 && sleepEndHour != -1 && sleepEndMinutes != -1) {
                        int[] totalSleepingTime = timeDifference(sleepStartHour, sleepStartMinutes, sleepEndHour, sleepEndMinutes);
                        this.totalSleepingTime = totalSleepingTime;

                        String sleepTimeStr = String.format(Locale.getDefault(), "%d hours %d minutes", totalSleepingTime[0], totalSleepingTime[1]);
                        sleepTime.setText(sleepTimeStr);
                    }
                },
                hour,
                minute,
                false);  // 'true' for 24-hour format
        timePickerDialog.show();
    }

    private String formatTime(int hour, int minute) {
        String period = (hour < 12) ? "AM" : "PM";
        int hour12 = (hour % 12 == 0) ? 12 : hour % 12;
        return String.format(Locale.getDefault(), "%d:%02d %s", hour12, minute, period);
    }

    private int[] timeDifference(int startHour, int startMinute, int endHour, int endMinute) {
        // Convert both times to minutes since midnight
        int startTotalMinutes = startHour * 60 + startMinute;
        int endTotalMinutes = endHour * 60 + endMinute;

        // If end time is before start time, assume it's on the next day
        if (endTotalMinutes < startTotalMinutes) {
            endTotalMinutes += 24 * 60; // Add 24 hours in minutes
        }

        // Calculate difference in minutes
        int diffMinutes = endTotalMinutes - startTotalMinutes;

        // Convert back to hours and minutes
        int diffHours = diffMinutes / 60;
        int remainingMinutes = diffMinutes % 60;

        return new int[]{diffHours, remainingMinutes};
    }

    private void submit(int[] sleepingTime) {
        sleepingTimeHistory.add(sleepingTime);
        saveSleepingTimeHistory(); // update the sleeping time history

        calculateAverageSleepingTime();
        timeStart.setText("_ _ : _ _");
        timeEnd.setText("_ _ : _ _");

        updateSleepingTimeOnDisplay();
    }

    private void calculateAverageSleepingTime() {

        int totalMinutes = 0;
        int numTimesSubmitted = 0;
        for (int[] individualSleepingTime : sleepingTimeHistory) {
            totalMinutes += (individualSleepingTime[0] * 60) + individualSleepingTime[1];
            numTimesSubmitted ++;
        }

        if (numTimesSubmitted == 0) {
            avgSleepTime.setText("0 hours");
        } else {
            float userAvgSleepingTime = (float) (((float) totalMinutes / 60.0) / (float) numTimesSubmitted);
            String avgSleepTimeStr = String.format(Locale.getDefault(), "%.2f hours", userAvgSleepingTime);
            avgSleepTime.setText(avgSleepTimeStr);
        }
    }

    private void undoLastSave() {
        sleepingTimeHistory.remove(sleepingTimeHistory.size() - 1);
        saveSleepingTimeHistory(); // update sleeping time history

        calculateAverageSleepingTime();
        undoBtn.setEnabled(false);
        undoBtn.setAlpha(0.5f);
        mininumSleepWarningIcon.setVisibility(View.INVISIBLE);
    }

    private void updateSleepingTimeOnDisplay() {
        if (sleepStartHour != -1 && sleepStartMinutes != -1 && sleepEndHour != -1 && sleepEndMinutes != -1) {
            int[] totalSleepingTime = timeDifference(sleepStartHour, sleepStartMinutes, sleepEndHour, sleepEndMinutes);
            this.totalSleepingTime = totalSleepingTime;

            String sleepTimeStr = String.format(Locale.getDefault(), "%d hours %d minutes", totalSleepingTime[0], totalSleepingTime[1]);
            sleepTime.setText(sleepTimeStr);

            int userMinimumSleepHours = appStorage.getInt("minimum_sleep_hours", 6);
            int userMinimumSleepMinutes = appStorage.getInt("minimum_sleep_minutes", 0);
            int userMinimumSleepTime = (userMinimumSleepHours * 60) + userMinimumSleepMinutes;

            int totalRecentSleepTimeMinutes = (totalSleepingTime[0] * 60) + totalSleepingTime[1];

            if (totalRecentSleepTimeMinutes < userMinimumSleepTime) {
                mininumSleepWarningIcon.setVisibility(View.VISIBLE);
            } else {
                mininumSleepWarningIcon.setVisibility(View.INVISIBLE);
            }
        }

        sleepStartHour = -1;
        sleepStartMinutes = -1;
        sleepEndHour = -1;
        sleepEndMinutes = -1;
    }

    private void saveSleepingTimeHistory() {
        SharedPreferences.Editor editor = appStorage.edit();

        JSONArray jsonArray = new JSONArray();
        for (int[] times : sleepingTimeHistory) {
            JSONArray innerArray = new JSONArray();
            for (int time : times) {
                innerArray.put(time);
            }
            jsonArray.put(innerArray);
        }

        editor.putString(KEY_SLEEPING_TIME_HISTORY, jsonArray.toString());
        editor.apply();
        editor.commit();
    }

    public void loadSleepingTimeHistory() {
        if (sleepingTimeHistory == null) {
            sleepingTimeHistory = new ArrayList<>();
        }

        String jsonString = appStorage.getString(KEY_SLEEPING_TIME_HISTORY, null);

        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray innerArray = jsonArray.getJSONArray(i);
                    int[] times = new int[innerArray.length()];
                    for (int j = 0; j < innerArray.length(); j++) {
                        times[j] = innerArray.getInt(j);
                    }
                    sleepingTimeHistory.add(times);
                }
            } catch (JSONException e) {
                Log.d("MainActivity", e.getMessage());
            }
        }
    }
}
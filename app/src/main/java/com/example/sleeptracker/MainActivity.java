package com.example.sleeptracker;

import android.app.TimePickerDialog;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.sleeptracker.databinding.ActivityMainBinding;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }

    public void showTimePickerDialog() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);  // 24-hour format
        int minute = calendar.get(Calendar.MINUTE);

        // Create and show the TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                MainActivity.this,
                (view, hourOfDay, minutes) -> {

                },
                hour,
                minute,
                true);  // 'true' for 24-hour format
        timePickerDialog.show();
    }
}
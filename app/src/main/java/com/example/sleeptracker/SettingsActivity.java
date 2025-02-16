package com.example.sleeptracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.slider.Slider;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.settings);

        SharedPreferences appPrefs = getApplicationContext().getSharedPreferences("SleepTracker", Context.MODE_PRIVATE);

        Slider hoursSlider = findViewById(R.id.hour_slider);
        Slider minuteSlider = findViewById(R.id.minute_slider);
        Button saveBtn = findViewById(R.id.save_settings);
        MaterialToolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_OK);
            SettingsActivity.this.finish();
        });

        float minHourSetting = (float) appPrefs.getInt("minimum_sleep_hours", 6);
        if (minHourSetting < 1f) {
            minHourSetting = 1f;
        }
        hoursSlider.setValue(minHourSetting);

        minuteSlider.setValue((float) appPrefs.getInt("minimum_sleep_minutes", 0));

        System.out.println("Settings Minimum sleep hours: " + hoursSlider.getValue());
        System.out.println("Settings Minimum sleep minutes: " + minuteSlider.getValue());

        SharedPreferences.Editor editor = appPrefs.edit();

        saveBtn.setOnClickListener((v) -> {
            int hours = (int) hoursSlider.getValue();
            int minutes = (int) minuteSlider.getValue();

            editor.putInt("minimum_sleep_hours", hours);
            editor.putInt("minimum_sleep_minutes", minutes);
            editor.apply();
            editor.commit();

            setResult(RESULT_OK);
            SettingsActivity.this.finish();
        });
    }
}

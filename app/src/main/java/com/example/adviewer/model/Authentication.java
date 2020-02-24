package com.example.adviewer.model;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adviewer.view.HomeScreen;
import com.example.adviewer.view.MainActivity;
import com.example.adviewer.view.SplashScreen;

public class Authentication extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activityIntent;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean Islogin = preferences.getBoolean("Islogin", false);

        if (Islogin) {
            activityIntent = new Intent(this, SplashScreen.class);
        } else {
            activityIntent = new Intent(this, MainActivity.class);
        }
        startActivity(activityIntent);
        finish();
    }
}
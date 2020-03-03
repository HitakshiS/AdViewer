package com.example.adviewer.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
            activityIntent = new Intent(this, SignInScreen.class);
        }
        startActivity(activityIntent);
        finish();
    }
}
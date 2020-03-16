package com.example.adviewer.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adviewer.R;


public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent activityIntent;

                SharedPreferences sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                Boolean Islogin = sharedPreferences.getBoolean("Islogin", false);

                if (Islogin) {
                    activityIntent = new Intent(SplashScreen.this, HomeScreen.class);
                } else {
                    activityIntent = new Intent(SplashScreen.this, SignInScreen.class);
                }
                startActivity(activityIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

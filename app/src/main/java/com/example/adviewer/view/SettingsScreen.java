package com.example.adviewer.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.adviewer.R;
import com.example.adviewer.model.NotificationService;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class SettingsScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] intervals = {"1 minute", "5 minute", "10 minute"};
    EditText numberOfAds;
    Button saveButton;
    String intervalPosition;
    int delayMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_screen);

        Spinner spin = findViewById(R.id.interval_spinner);
        spin.setOnItemSelectedListener(this);

        numberOfAds = findViewById(R.id.number_of_ads_text);
        saveButton = findViewById(R.id.save_btn);


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, intervals);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(numberOfAds.getText().toString())) {
                    Toast.makeText(SettingsScreen.this, R.string.ads_notification_count_text, Toast.LENGTH_LONG).show();
                } else {
                    int numOfAds = Integer.valueOf(numberOfAds.getText().toString());
                    if (numOfAds > 0 && numOfAds <= 10) {
                        if (intervalPosition.equals("1 minute")) {
                            delayMinutes = 60;
                        } else if (intervalPosition.equals("5 minute")) {
                            delayMinutes = 300;
                        } else if (intervalPosition.equals("10 minute")) {
                            delayMinutes = 600;
                        } else {
                            delayMinutes = 60;
                        }

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsScreen.this);
                        //int period = delayMinutes * 1000;
                        prefs.edit().putInt("notification", 0).apply();
                        Intent serviceIntent = new Intent(SettingsScreen.this, NotificationService.class);
                        runService(numOfAds);
                        //callAsynchronousTask(period, numOfAds);
                        Intent homeIntent = new Intent(SettingsScreen.this, HomeScreen.class);
                        startActivity(homeIntent);

                    } else if (numOfAds <= 0) {
                        numberOfAds.setText("");
                        Toast.makeText(SettingsScreen.this, R.string.ads_min_limit, Toast.LENGTH_SHORT).show();
                    } else {
                        numberOfAds.setText("");
                        Toast.makeText(SettingsScreen.this, R.string.ads_max_limit, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void runService(int numOfAds) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsScreen.this);
        int getNoOfTime = prefs.getInt("notification", 0);
        Intent serviceIntent;
        serviceIntent = new Intent(SettingsScreen.this, NotificationService.class);
        Log.e("service", "entered");
        if (getNoOfTime == numOfAds - 1) {
            stopService(serviceIntent);
            Log.e("service", "stopped");
            prefs.edit().putInt("notification", 0).apply();
        } else {
            prefs.edit().putInt("notification", getNoOfTime + 1).apply();
            Log.e("service", "started");
            startService(serviceIntent);
        }
    }

    public void callAsynchronousTask(int period, int numOfAds) {
        final Handler handler = new Handler();
        final Timer timer = new Timer();
        final int displayTime = numOfAds;
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsScreen.this);
                            int getNoOfTime = prefs.getInt("notification", 0);
                            if (getNoOfTime == displayTime - 1) {
                                timer.cancel();
                                prefs.edit().putInt("notification", 0).apply();
                            } else {
                                prefs.edit().putInt("notification", getNoOfTime + 1).apply();
                                showNotification();
                            }

                        } catch (Exception e) {
                            Toast.makeText(SettingsScreen.this, R.string.notification_error_msg, Toast.LENGTH_LONG).show();
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, period, period);
    }

    public void showNotification() {
        Intent resultIntent = new Intent(getApplicationContext(), HomeScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        String channelId = "task_channel";
        String channelName = "task_name";

        final Calendar cal = Calendar.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(getString(R.string.ad_notif_title))
                .setContentText(getString(R.string.ad_notif_content))
                .setSmallIcon(R.drawable.add_drawer_logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        manager.notify(1, builder.build());
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        intervalPosition = intervals[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}


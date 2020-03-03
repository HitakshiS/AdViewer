package com.example.adviewer.model;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.adviewer.R;
import com.example.adviewer.view.HomeScreen;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        int alarmId = intent.getExtras().getInt("alarmId");
        int numOfNotification = intent.getExtras().getInt("repeatNumber");
        PendingIntent alarmIntent;
        alarmIntent = PendingIntent.getBroadcast(context, alarmId, new Intent(context, NotificationBroadcastReceiver.class),
                0);
        SharedPreferences sharedPreferences = context.getSharedPreferences("TIMER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int alarmCount = sharedPreferences.getInt("alarmCount", 1);
        showNotification();
        editor.putInt("alarmCount", alarmCount + 1).apply();

        if (alarmCount == numOfNotification) {
            editor.putInt("alarmCount", 1).commit();
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);
        }
    }

    public void showNotification() {
        Intent resultIntent = new Intent(context, HomeScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        String channelId = "task_channel";
        String channelName = "task_name";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("Ad Reminder")
                .setContentText("Hey! It's time to watch new ad")
                .setSmallIcon(R.drawable.add_drawer_logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        manager.notify(1, builder.build());
    }

}

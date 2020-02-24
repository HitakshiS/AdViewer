package com.example.adviewer.model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
        import android.content.Intent;
import android.os.Build;
import android.os.Handler;
        import android.os.HandlerThread;
        import android.os.IBinder;
        import android.os.Looper;
        import android.os.Message;
        import android.os.Process;
        import android.util.Log;
        import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.adviewer.R;
import com.example.adviewer.view.HomeScreen;

import java.util.Calendar;

public class NotificationService extends Service {
    private static final String TAG = "MyService";
    private boolean isRunning  = false;
    private Looper looper;
    private MyServiceHandler myServiceHandler;
    public Handler handler = null;
    public static Runnable runnable = null;
    @Override
    public void onCreate() {
        HandlerThread handlerthread = new HandlerThread("MyThread", Process.THREAD_PRIORITY_BACKGROUND);
        handlerthread.start();
        looper = handlerthread.getLooper();
        myServiceHandler = new MyServiceHandler(looper);
        isRunning = true;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = myServiceHandler.obtainMessage();
        msg.arg1 = startId;
        myServiceHandler.sendMessage(msg);
        Log.e("service", "inside service entered");
        Toast.makeText(this, "MyService Started.", Toast.LENGTH_SHORT).show();
        //If service is killed while starting, it restarts.
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                showNotification();
                Toast.makeText(getApplicationContext(), "Service is still running", Toast.LENGTH_LONG).show();
                handler.postDelayed(runnable, 1000);
            }
        };

        handler.postDelayed(runnable, 1500);
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        isRunning = false;
        Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
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

    private final class MyServiceHandler extends Handler {
        public MyServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            synchronized (this) {
                for (int i = 0; i < 10; i++) {
                    try {
                        Log.i(TAG, "MyService running...");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                    if(!isRunning){
                        break;
                    }
                }
            }
            //stops the service for the start id.
            stopSelfResult(msg.arg1);
        }
    }
}
package com.example.adviewer.viewModel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import androidx.databinding.library.baseAdapters.BR;

import com.example.adviewer.model.NotificationBroadcastReceiver;

@InverseBindingMethods({
        @InverseBindingMethod(type = Spinner.class, attribute = "android:selectedItemPosition"),
})

public class SettingViewModel extends BaseObservable {

    private String numberOfAds;
    ViewModelListener viewModelListener;
    int delayMinutes;
    PendingIntent pendingIntent;
    private final String[] values;
    private final View.OnClickListener onClickListener;

    private String selectedText;
    private int selectedPosition;

    AlarmManager alarmManager;


    private final Context context;

    public SettingViewModel(String[] values, Context context) {
        this.values = values;
        this.context = context;

        if (context instanceof ViewModelListener) {
            viewModelListener = (ViewModelListener) context;
        }
        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedPosition(0);
            }
        };
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Bindable
    public String getSelectedText() {
        return selectedText;

    }

    @Bindable
    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
        notifyPropertyChanged(BR.selectedText);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyPropertyChanged(BR.selectedPosition);
        setSelectedText(this.values[selectedPosition]);
    }

    public String getNumberOfAds() {
        return numberOfAds;
    }

    public void setNumberOfAds(String numberOfAds) {
        this.numberOfAds = numberOfAds;
    }

    public void onSettingsSaveClick(View view) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (numberOfAds.isEmpty()) {
            viewModelListener.onFailure("Please enter the number of times you want to watch the ads.");
        } else {
            int numOfAds = Integer.valueOf(numberOfAds);
            if (numOfAds > 0 && numOfAds <= 10) {
                if (selectedPosition == 0) {
                    delayMinutes = 60;
                } else if (selectedPosition == 1) {
                    delayMinutes = 300;
                } else if (selectedPosition == 2) {
                    delayMinutes = 600;
                } else {
                    delayMinutes = 60;
                }
                final long period = delayMinutes * 1000;
                editor.putInt("intervalPosition", getSelectedPosition()).apply();
                editor.putString("numOfAdCount", getNumberOfAds()).apply();
                startAlarmBroadcastReceiver(context, period);
                viewModelListener.onSuccess();

            } else if (numOfAds <= 0) {
                viewModelListener.onFailure("Ad should be watched at least once");
            } else {
                viewModelListener.onFailure("Maximum 10 ads could be watched");
            }
        }
    }

    public void startAlarmBroadcastReceiver(Context context, long delay) {
        int alarmId = 0;
        int numOfAds = Integer.parseInt(numberOfAds);
        Intent broadcastIntent = new Intent(context, NotificationBroadcastReceiver.class);
        SharedPreferences sharedPreferences = context.getSharedPreferences("TIMER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        broadcastIntent.putExtra("alarmId", alarmId);
        broadcastIntent.putExtra("repeatNumber", numOfAds);
        editor.remove("alarmCount");
        editor.apply();
        long alarmTriggerTime = System.currentTimeMillis() + delay;
        pendingIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTriggerTime,
                delay, pendingIntent);
        Log.e("alarm", "set");
    }

}

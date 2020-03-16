package com.example.adviewer.viewModel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.adviewer.R;
import com.example.adviewer.model.UserDatabase;
import com.example.adviewer.utility.InputValidation;
import com.example.adviewer.view.SignUpScreen;

public class LoginViewModel extends ViewModel {

    private String email;
    private String password;
    ViewModelListener viewModelListener;
    private InputValidation inputValidation;
    private UserDatabase databaseHelper;
    boolean isLoggedIn = false;
    private final Context context;

    public LoginViewModel(Context context) {
        this.context = context;
        inputValidation = new InputValidation(context);

        if (context instanceof ViewModelListener) {
            viewModelListener = (ViewModelListener) context;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void onLoginButtonClick(View view) {
        databaseHelper = new UserDatabase(context);
        Log.e("onLoginButtonClick: ", "1");
        if (email.isEmpty()) {
            viewModelListener.onFailure(context.getString(R.string.error_message_email));
        } else if (password.isEmpty()) {
            viewModelListener.onFailure(context.getString(R.string.error_message_password));
        } else if (inputValidation.isInputEditTextEmail(email)) {
            viewModelListener.onFailure(context.getString(R.string.error_message_invalid_email));
        } else if (databaseHelper.checkUser(email, password)) {
            restoreInitialState();
            viewModelListener.onSuccess();

        } else {
            viewModelListener.onFailure(context.getString(R.string.login_fail_error));
        }
    }

    public void restoreInitialState() {
        SharedPreferences loginSharedPreferences = context.getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginSharedPreferences.edit();
        SharedPreferences statsSharedPreferences = context.getSharedPreferences("CALENDAR", Context.MODE_PRIVATE);
        SharedPreferences.Editor statsEditor = statsSharedPreferences.edit();
        SharedPreferences settingsSharedPreferences = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        SharedPreferences.Editor settingsEditor = settingsSharedPreferences.edit();
        isLoggedIn = true;
        editor.putBoolean("Islogin", isLoggedIn).apply();
        statsEditor.putString("rewardAdsMonth", "0").apply();
        statsEditor.putString("interAdsMonth", "0").apply();
        statsEditor.putString("rewardAdsDay", "0").apply();
        statsEditor.putString("interAdsDay", "0").apply();
        statsEditor.putString("interstitialAds", "0").apply();
        statsEditor.putString("rewardAds", "0").apply();
        statsEditor.putString("rewardAdDuration", "0").apply();
        settingsEditor.putInt("intervalPosition", 0).apply();
        settingsEditor.putString("numOfAdCount", "0").apply();
    }

    public void onSignUpButtonClick(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, SignUpScreen.class);
        context.startActivity(intent);
    }

}

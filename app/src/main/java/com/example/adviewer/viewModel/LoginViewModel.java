package com.example.adviewer.viewModel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import androidx.lifecycle.ViewModel;

import com.example.adviewer.model.UserDatabase;
import com.example.adviewer.view.SignUpScreen;

public class LoginViewModel extends ViewModel {

    private String email;
    private String password;
    ViewModelListener viewModelListener;

    private UserDatabase databaseHelper;
    boolean isLoggedIn = false;
    private final Context context;

    public LoginViewModel(Context context) {
        this.context = context;

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
            viewModelListener.onFailure("Enter Email");
        } else if (password.isEmpty()) {
            viewModelListener.onFailure("Enter password");
        } else if (!isInputEditTextEmail(email)) {
            viewModelListener.onFailure("Invalid email entered");
        } else if (databaseHelper.checkUser(email, password)) {
            viewModelListener.onSuccess();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            isLoggedIn = true;
            prefs.edit().putBoolean("Islogin", isLoggedIn).apply();
            //prefs.edit().putString("userEmail", email).commit();
        } else {
            viewModelListener.onFailure("You are not registered");
        }
    }

    public void onSignUpButtonClick(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, SignUpScreen.class);
        context.startActivity(intent);
    }

    private boolean isInputEditTextEmail(String value) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return value.matches(regex);
    }
}

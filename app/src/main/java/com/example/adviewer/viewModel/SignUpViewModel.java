package com.example.adviewer.viewModel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import androidx.lifecycle.ViewModel;
import com.example.adviewer.model.User;
import com.example.adviewer.model.UserDatabase;
import com.example.adviewer.utility.InputValidation;
import com.example.adviewer.view.SignInScreen;
import java.util.Calendar;
import java.util.Date;

public class SignUpViewModel extends ViewModel {

    private  String name;
    private String email;
    private String password;
    ViewModelListener viewModelListener;
    private User user = new User();
    private InputValidation inputValidation;
    public UserDatabase databaseHelper;
    private final Context context;

    public SignUpViewModel(Context context) {
        this.context = context;
        inputValidation = new InputValidation(context);

        if(context instanceof ViewModelListener) {
            viewModelListener = (ViewModelListener) context;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public void onRegisterClick(View view) {
        databaseHelper = new UserDatabase(context);
        if(name.isEmpty()){
            viewModelListener.onFailure("Enter name");
        }
        else if (email.isEmpty()) {
            viewModelListener.onFailure("Enter Email");
        } else if (password.isEmpty()) {
            viewModelListener.onFailure("Enter password");
        } else if (!inputValidation.isInputEditTextEmail(email)) {
            viewModelListener.onFailure("Invalid email entered");
        } else if (!databaseHelper.checkUser(email)) {
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            databaseHelper.addUser(user);
            Date date= new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DATE);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences.edit().putInt("initialMonth", month).apply();
            preferences.edit().putInt("initialDay", day).apply();
            viewModelListener.onSuccess();
        }
        else{
            viewModelListener.onFailure("Email already exists");
        }
    }

    public void onGoToLoginClick(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, SignInScreen.class);
        context.startActivity(intent);
    }

}

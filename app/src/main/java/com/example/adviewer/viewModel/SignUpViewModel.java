package com.example.adviewer.viewModel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import androidx.lifecycle.ViewModel;

import com.example.adviewer.R;
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
            viewModelListener.onFailure(context.getString(R.string.error_message_name));
        }
        else if (email.isEmpty()) {
            viewModelListener.onFailure(context.getString(R.string.error_message_email));
        } else if (password.isEmpty()) {
            viewModelListener.onFailure(context.getString(R.string.error_message_password));
        } else if (inputValidation.isInputEditTextEmail(email)) {
            viewModelListener.onFailure(context.getString(R.string.error_message_invalid_email));
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

            SharedPreferences sharedPreferences = context.getSharedPreferences("CALENDAR", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("initialMonth", month).apply();
            editor.putInt("initialDay", day).apply();
            viewModelListener.onSuccess();
        }
        else{
            viewModelListener.onFailure(context.getString(R.string.error_email_exists));
        }
    }

    public void onGoToLoginClick(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, SignInScreen.class);
        context.startActivity(intent);
    }

}

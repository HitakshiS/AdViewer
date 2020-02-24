package com.example.adviewer.view;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adviewer.R;
import com.example.adviewer.model.InputValidation;
import com.example.adviewer.model.UserDatabase;

public class SignInScreen extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = SignInScreen.this;

    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPassword;

    private Button appCompatButtonLogin;

    private Button textViewLinkRegister;

    private InputValidation inputValidation;
    private UserDatabase databaseHelper;
    Boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_screen);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }


    private void initViews() {

        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextPassword = findViewById(R.id.password);
        appCompatButtonLogin = findViewById(R.id.btnLogin);
        textViewLinkRegister = findViewById(R.id.btnLinkToRegisterScreen);

    }


    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    private void initObjects() {
        databaseHelper = new UserDatabase(activity);
        inputValidation = new InputValidation(activity);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                verifyFromSQLite();
                break;
            case R.id.btnLinkToRegisterScreen:
                Intent intentRegister = new Intent(getApplicationContext(), SignUpScreen.class);
                startActivity(intentRegister);
                break;
        }
    }

    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail)) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_message_email), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail)) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_message_invalid_email), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword)) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_message_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {
            emptyInputEditText();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignInScreen.this);
            isLoggedIn = true;
            prefs.edit().putBoolean("Islogin", isLoggedIn).apply();
            prefs.edit().putString("userEmail", textInputEditTextEmail.getText().toString().trim()).commit();
            Intent homeIntent = new Intent(SignInScreen.this, HomeScreen.class);
            startActivity(homeIntent);

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_valid_email_password), Toast.LENGTH_SHORT).show();
        }
    }


    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}


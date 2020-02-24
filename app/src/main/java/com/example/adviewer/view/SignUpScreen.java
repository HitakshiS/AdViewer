package com.example.adviewer.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adviewer.R;
import com.example.adviewer.model.InputValidation;
import com.example.adviewer.model.User;
import com.example.adviewer.model.UserDatabase;

public class SignUpScreen extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = SignUpScreen.this;

    private EditText textInputEditTextName;
    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPassword;

    private Button appCompatButtonRegister;
    private Button appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    private UserDatabase databaseHelper;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {

        textInputEditTextName = findViewById(R.id.name);
        textInputEditTextEmail =  findViewById(R.id.email);
        textInputEditTextPassword =  findViewById(R.id.password);
        appCompatButtonRegister =  findViewById(R.id.btnRegister);
        appCompatTextViewLoginLink =  findViewById(R.id.btnLinkToLoginScreen);

    }

    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);

    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new UserDatabase(activity);
        user = new User();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnRegister:
                postDataToSQLite();
                break;

            case R.id.btnLinkToLoginScreen:
                Intent signInIntent = new Intent(SignUpScreen.this, SignInScreen.class);
                startActivity(signInIntent);
                break;
        }
    }

    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName)) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_message_name), Toast.LENGTH_SHORT).show();
            return;
        }
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

        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());

            databaseHelper.addUser(user);

            showConfimationDialog();
            emptyInputEditText();

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_email_exists), Toast.LENGTH_LONG).show();
        }
    }

    private void showConfimationDialog() {
        LayoutInflater li = LayoutInflater.from(SignUpScreen.this);
        final View popUpView = li.inflate(R.layout.popup_dialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUpScreen.this);
        alertDialogBuilder.setView(popUpView);

        TextView popUpHeadingText = popUpView.findViewById(R.id.alert_text);
        popUpHeadingText.setText(getString(R.string.success_message));

        TextView messageText = popUpView.findViewById(R.id.message_text);
        messageText.setText(getString(R.string.success_message_text));

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok_text),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent signInIntent = new Intent(SignUpScreen.this, SignInScreen.class);
                                startActivity(signInIntent);
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
package com.example.adviewer.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.adviewer.R;
import com.example.adviewer.databinding.SignupScreenBinding;
import com.example.adviewer.viewModel.ViewModelListener;
import com.example.adviewer.viewModel.SignUpViewModel;

public class SignUpScreen extends AppCompatActivity implements ViewModelListener {

    private EditText textInputEditTextName;
    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPassword;

    SignupScreenBinding binding;
    SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signup_screen);
        signUpViewModel = new SignUpViewModel(this);
        binding.setViewModel(signUpViewModel);
        getSupportActionBar().hide();
        initViews();
    }

    private void initViews() {

        textInputEditTextName = binding.name;
        signUpViewModel.setName(textInputEditTextName.getText().toString());

        textInputEditTextEmail = binding.email;
        signUpViewModel.setEmail(textInputEditTextName.getText().toString());

        textInputEditTextPassword = binding.password;
        signUpViewModel.setPassword(textInputEditTextName.getText().toString());

        final Button appCompatButtonRegister = binding.btnRegister;
        appCompatButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpViewModel.onRegisterClick(appCompatButtonRegister);
            }
        });

        Button appCompatTextViewLoginLink = findViewById(R.id.btnLinkToLoginScreen);
        appCompatTextViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(SignUpScreen.this, SignInScreen.class);
                startActivity(signInIntent);

            }
        });
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
                            public void onClick(DialogInterface dialog, int id) {
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

    @Override
    public void onSuccess() {
        showConfimationDialog();
        emptyInputEditText();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
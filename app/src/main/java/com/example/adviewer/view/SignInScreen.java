package com.example.adviewer.view;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.adviewer.R;
import com.example.adviewer.databinding.SigninScreenBinding;
import com.example.adviewer.viewModel.ViewModelListener;
import com.example.adviewer.viewModel.LoginViewModel;

public class SignInScreen extends AppCompatActivity implements View.OnClickListener, ViewModelListener {
    private final AppCompatActivity activity = SignInScreen.this;

    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPassword;

    private Button appCompatButtonLogin;

    private Button textViewLinkRegister;

    SigninScreenBinding binding;
    LoginViewModel loginModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SignInScreen.this, R.layout.signin_screen);
        loginModel = new LoginViewModel(SignInScreen.this);
        binding.setViewModel(loginModel);
        getSupportActionBar().hide();

        textInputEditTextEmail = binding.email;
        textInputEditTextPassword = binding.password;
        appCompatButtonLogin = binding.btnLogin;
        appCompatButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginModel.onLoginButtonClick(appCompatButtonLogin);
            }
        });

        textViewLinkRegister = binding.btnLinkToRegisterScreen;
        textViewLinkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginModel.onLoginButtonClick(textViewLinkRegister);
            }
        });
        initListeners();
        loginModel.setEmail(textInputEditTextEmail.getText().toString());
        loginModel.setPassword(textInputEditTextPassword.getText().toString());

    }


    private void initListeners() {
        textViewLinkRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLinkToRegisterScreen:
                Intent intentRegister = new Intent(getApplicationContext(), SignUpScreen.class);
                startActivity(intentRegister);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    @Override
    public void onSuccess() {
        emptyInputEditText();
        Intent homeIntent = new Intent(SignInScreen.this, HomeScreen.class);
        startActivity(homeIntent);
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}


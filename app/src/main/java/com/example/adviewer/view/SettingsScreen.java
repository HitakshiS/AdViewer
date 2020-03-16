package com.example.adviewer.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.adviewer.R;
import com.example.adviewer.databinding.SettingScreenBinding;
import com.example.adviewer.viewModel.SettingViewModel;
import com.example.adviewer.viewModel.ViewModelListener;

public class SettingsScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ViewModelListener {

    EditText numberOfAds;
    Button saveButton;
    SettingScreenBinding binding;
    SettingViewModel settingViewModel;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.setting_screen);
        settingViewModel = new SettingViewModel(getResources().getStringArray(R.array.interval), this);
        binding.setSetting(settingViewModel);

        SharedPreferences sharedPreferences = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        int selectedPosition = sharedPreferences.getInt("intervalPosition", 0);
        String numOfAdSelected = sharedPreferences.getString("numOfAdCount", "0");

        spin = binding.intervalSpinner;
        spin.setOnItemSelectedListener(this);
        settingViewModel.setSelectedPosition(selectedPosition);

        numberOfAds = binding.numberOfAdsText;
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    numberOfAds.setSelection(numberOfAds.getText().length());
                }
            }
        };

        if (!numOfAdSelected.equals("0")) {
            numberOfAds.setText(numOfAdSelected);
            numberOfAds.setOnFocusChangeListener(onFocusChangeListener);
        }
        int textLength = numberOfAds.getText().length();
        numberOfAds.setSelection(textLength, textLength);
        settingViewModel.setNumberOfAds(numberOfAds.getText().toString());

        saveButton = binding.saveBtn;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingViewModel.onSettingsSaveClick(saveButton);
            }
        });

        ImageView settingsBack = findViewById(R.id.settings_back);
        settingsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(SettingsScreen.this, HomeScreen.class);
                startActivity(homeIntent);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        settingViewModel.setSelectedPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

        settingViewModel.setSelectedPosition(0);
    }

    @Override
    public void onSuccess() {
        Intent homeIntent = new Intent(SettingsScreen.this, HomeScreen.class);
        startActivity(homeIntent);

    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}


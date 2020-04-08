package com.example.adviewer.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.adviewer.R;
import com.example.adviewer.model.AdsStats;
import com.example.adviewer.model.AdsStatsDatabase;
import com.example.adviewer.utility.AppUtilities;
import com.example.adviewer.viewModel.HomeViewModel;
import com.example.adviewer.databinding.HomeScreenBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class HomeScreen extends AppCompatActivity {

    private ActionBarDrawerToggle t;
    HomeScreenBinding binding;
    HomeViewModel homeModel;
    AdsStats adsStats = new AdsStats();
    AppUtilities appUtilities = new AppUtilities(HomeScreen.this);
    AdsStatsDatabase adsStatsDatabase = new AdsStatsDatabase(HomeScreen.this);
    String userEmail;
    String numOfInterstitialAds;
    String numOfRewardAds;
    String rewardAdDuration;
    String interDayCount;
    String interMonthCount;
    String rewardDayCount;
    String rewardMonthCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(HomeScreen.this, R.layout.home_screen);
        homeModel = new HomeViewModel(HomeScreen.this);
        binding.setViewModel(homeModel);

        SharedPreferences userPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        userEmail = userPreferences.getString("userEmail", "");

        TextView title = binding.gameTitle;
        homeModel.setTitle(getString(R.string.ads_select_text));
        title.setText(homeModel.getTitle());


        TextView interstitialCount = binding.interstitialCountText;
        if (extractUserData()) {
            interstitialCount.setText(numOfInterstitialAds);
        }
        if (!interstitialCount.getText().toString().equals("")) {
            adsStats.setNumberOfInterstitialAdsWatched(Integer.valueOf(interstitialCount.getText().toString()));
        }
        homeModel.setInterstitialCount(interstitialCount.getText().toString());

        TextView rewardCount = binding.rewardCountText;
        if (extractUserData()) {
            rewardCount.setText(numOfRewardAds);
            adsStats.setNumberOfRewardAdsWatched(Integer.valueOf(numOfRewardAds));
        }
        homeModel.setRewardCount(rewardCount.getText().toString());

        TextView durationCount = binding.durationCountText;
        if (extractUserData()) {
            durationCount.setText(rewardAdDuration + " sec");
        }
        homeModel.setDurationCount(durationCount.getText().toString());

        final Button interstitial_button = binding.interstitialButton;
        interstitial_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeModel.onInterstitialClicked(interstitial_button);
            }
        });


        final Button reward_button = binding.rewardButton;
        reward_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeModel.onRewardClicked(reward_button);
            }
        });

        TextView interDay = binding.interstitialDayCountText;
        if (extractUserData()) {
            interDay.setText(interDayCount);
        }
        homeModel.setInterstitialDayCount(interDay.getText().toString());

        TextView interMonth = binding.interstitialMonthCountText;
        if (extractUserData()) {
            interMonth.setText(interMonthCount);
        }
        homeModel.setInterstitialMonthCount(interMonth.getText().toString());

        TextView rewardDay = binding.rewardDayCountText;
        if (extractUserData()) {
            rewardDay.setText(rewardDayCount);
        }
        homeModel.setRewardDayCount(rewardDay.getText().toString());

        TextView rewardMonth = binding.rewardMonthCountText;
        if (extractUserData()) {
            rewardMonth.setText(rewardMonthCount);
        }
        homeModel.setRewardMonthCount(rewardMonth.getText().toString());


        final DrawerLayout dl = findViewById(R.id.home_drawer_layout);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        NavigationView nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.settings: {
                        Intent settingsIntent = new Intent(HomeScreen.this, SettingsScreen.class);
                        startActivity(settingsIntent);
                        dl.closeDrawers();
                        break;
                    }
                    case R.id.logout: {
                        showConfimationDialog();
                        dl.closeDrawers();
                        return true;
                    }
                    case R.id.rateApp: {
                        appUtilities.rateApp();
                        dl.closeDrawers();
                        return true;
                    }
                    case R.id.shareApp: {
                        appUtilities.shareApp();
                        dl.closeDrawers();
                        return true;
                    }
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private void showConfimationDialog() {
        LayoutInflater li = LayoutInflater.from(HomeScreen.this);
        final View popUpView = li.inflate(R.layout.popup_dialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeScreen.this);
        alertDialogBuilder.setView(popUpView);

        TextView popUpHeadingText = popUpView.findViewById(R.id.alert_text);
        popUpHeadingText.setText("Alert");

        TextView messageText = popUpView.findViewById(R.id.message_text);
        messageText.setText("Are you sure you want to log out! ");

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok_text),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences logOutSharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = logOutSharedPreferences.edit();
                                editor.putBoolean("Islogin", false).apply();
                                Intent signInIntent = new Intent(HomeScreen.this, SignInScreen.class);
                                startActivity(signInIntent);

                            }
                        })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeModel.showStats();
    }

    public boolean extractUserData() {
        Cursor rs = adsStatsDatabase.getUserStats(userEmail);
        rs.moveToFirst();
        if (rs.getCount() == 0) {
            return false;
        } else {
            numOfInterstitialAds = String.valueOf(rs.getString(rs.getColumnIndex("ad_interstitial")));
            numOfRewardAds = String.valueOf(rs.getString(rs.getColumnIndex("ad_reward")));
            rewardAdDuration = String.valueOf(rs.getString(rs.getColumnIndex("ad_duration")));
            interDayCount = String.valueOf(rs.getString(rs.getColumnIndex("ad_interstitial_day")));
            interMonthCount = String.valueOf(rs.getString(rs.getColumnIndex("ad_interstitial_month")));
            rewardDayCount = String.valueOf(rs.getString(rs.getColumnIndex("ad_reward_day")));
            rewardMonthCount = String.valueOf(rs.getString(rs.getColumnIndex("ad_reward_month")));
            return true;
        }
    }
}


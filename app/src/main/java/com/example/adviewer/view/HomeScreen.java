package com.example.adviewer.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.adviewer.R;
import com.example.adviewer.model.AdsStats;
import com.example.adviewer.utility.AppUtilities;
import com.example.adviewer.viewModel.HomeViewModel;
import com.example.adviewer.databinding.HomeScreenBinding;
import com.google.android.material.navigation.NavigationView;

public class HomeScreen extends AppCompatActivity {

    private ActionBarDrawerToggle t;
    HomeScreenBinding binding;
    HomeViewModel homeModel;
    AdsStats adsStats = new AdsStats();
    AppUtilities appUtilities = new AppUtilities(HomeScreen.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(HomeScreen.this, R.layout.home_screen);
        homeModel = new HomeViewModel(HomeScreen.this);
        binding.setViewModel(homeModel);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String numOfInterstitialAds = preferences.getString("interstitialAds", "0");
        String numOfRewardAds = preferences.getString("rewardAds", "0");
        String rewardAdDuration = preferences.getString("rewardAdDuration", "0 sec");
        String interDayCount = preferences.getString("interAdsDay", "0");
        String interMonthCount = preferences.getString("interAdsMonth", "0");
        String rewardDayCount = preferences.getString("rewardAdsDay", "0");
        String rewardMonthCount = preferences.getString("rewardAdsMonth", "0");

        TextView title = binding.gameTitle;
        homeModel.setTitle(getString(R.string.ads_select_text));
        title.setText(homeModel.getTitle());


        TextView interstitialCount = binding.interstitialCountText;
        if (!numOfInterstitialAds.equals("0")) {
            interstitialCount.setText(numOfInterstitialAds);
        }
        if(!interstitialCount.getText().toString().equals("")){
            adsStats.setNumberOfInterstitialAdsWatched(Integer.valueOf(interstitialCount.getText().toString()));
        }
        homeModel.setInterstitialCount(interstitialCount.getText().toString());

        TextView rewardCount = binding.rewardCountText;
        if (!numOfRewardAds.equals("0")) {
            rewardCount.setText(numOfRewardAds);
            adsStats.setNumberOfRewardAdsWatched(Integer.valueOf(numOfRewardAds));
        }
        homeModel.setRewardCount(rewardCount.getText().toString());

        TextView durationCount = binding.durationCountText;
        if (!rewardAdDuration.equals("0 sec")) {
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
        if (!interDayCount.equals("0")) {
            interDay.setText(interDayCount);
        }
        homeModel.setInterstitialDayCount(interDay.getText().toString());

        TextView interMonth = binding.interstitialMonthCountText;
        if (!interMonthCount.equals("0")) {
            interMonth.setText(interMonthCount);
        }
        homeModel.setInterstitialMonthCount(interMonth.getText().toString());

        TextView rewardDay = binding.rewardDayCountText;
        if (!rewardDayCount.equals("0")) {
            rewardDay.setText(rewardDayCount);
        }
        homeModel.setRewardDayCount(rewardDay.getText().toString());

        TextView rewardMonth = binding.rewardMonthCountText;
        if (!rewardMonthCount.equals("0")) {
            rewardMonth.setText(rewardMonthCount);
        }
        homeModel.setRewardMonthCount(rewardMonth.getText().toString());


        final DrawerLayout dl = findViewById(R.id.home_drawer_layout);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HomeScreen.this);
                        prefs.edit().putBoolean("Islogin", false).apply();
                        Intent signInIntent = new Intent(HomeScreen.this, SignInScreen.class);
                        startActivity(signInIntent);
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
}


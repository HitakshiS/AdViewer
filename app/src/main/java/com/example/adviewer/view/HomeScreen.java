package com.example.adviewer.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.adviewer.BuildConfig;
import com.example.adviewer.R;
import com.example.adviewer.model.AdsStats;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.navigation.NavigationView;

public class HomeScreen extends AppCompatActivity {

    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    private static final String REWARD_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    
    private static final long COUNTER_TIME = 10;
    private static final int GAME_OVER_REWARD = 1;

    private int coinCount;
    private TextView coinCountText;
    private RewardedAd rewardedAd;

    private InterstitialAd interstitialAd;
    private Button interstitial_button;
    private Button reward_button;
    private DrawerLayout dl;
    private NavigationView nv;
    private ActionBarDrawerToggle t;
    AdsStats stats = new AdsStats(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        dl = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.settings: {
                        Intent settingsIntent = new Intent(HomeScreen.this, SettingsScreen.class);
                        startActivity(settingsIntent);
                        break;
                    }
                    case R.id.logout: {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HomeScreen.this);
                        prefs.edit().putBoolean("Islogin", false).apply();
                        prefs.edit().remove("userEmail").apply();
                        Intent mainIntent = new Intent(HomeScreen.this, MainActivity.class);
                        startActivity(mainIntent);
                        return true;
                    }
                    case R.id.rateApp: {
                        rateApp();
                        return true;
                    }
                    case R.id.shareApp: {
                        shareApp();
                        return true;
                    }
                    default:
                        return true;
                }
                return true;
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        loadRewardedAd();

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(AD_UNIT_ID);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(HomeScreen.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(HomeScreen.this,
                        "onAdFailedToLoad() with error code: " + errorCode,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                startGame();
            }
        });

        interstitial_button = findViewById(R.id.interstitial_button);
        interstitial_button.setVisibility(View.VISIBLE);
        interstitial_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitial();
            }
        });
        startGame();
    }

    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    public void shareApp(){
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Ad Viewer");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void showInterstitial() {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            stats.addNumberOfAdsWatched();
            interstitialAd.show();

        } else {
            Toast.makeText(this, R.string.interstitial_ad_load_fail, Toast.LENGTH_SHORT).show();
            startGame();
        }
    }

    private void startGame() {
        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }

        reward_button = findViewById(R.id.reward_button);
        reward_button.setVisibility(View.VISIBLE);
        reward_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showRewardedVideo();
                    }
                });

        coinCountText = findViewById(R.id.coin_count_text);
        coinCount = 0;
        coinCountText.setText(String.format(getString(R.string.Coins), coinCount));

        startReward();
    }


    private void loadRewardedAd() {
        if (rewardedAd == null || !rewardedAd.isLoaded()) {
            rewardedAd = new RewardedAd(this, REWARD_AD_UNIT_ID);
            rewardedAd.loadAd(
                    new AdRequest.Builder().build(),
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onRewardedAdLoaded() {
                            Toast.makeText(HomeScreen.this, "onRewardedAdLoaded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdFailedToLoad(int errorCode) {
                            Toast.makeText(HomeScreen.this, "onRewardedAdFailedToLoad", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }

    private void addCoins(int coins) {
        coinCount += coins;
        coinCountText.setText(String.format(getString(R.string.Coins), coinCount));
    }

    private void startReward() {
        reward_button.setVisibility(View.VISIBLE);
        if (!rewardedAd.isLoaded()) {
            loadRewardedAd();
        }
    }

    private void showRewardedVideo() {
        reward_button.setVisibility(View.VISIBLE);
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback =
                    new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            stats.addNumberOfAdsWatched();
                            Toast.makeText(HomeScreen.this, "onRewardedAdOpened", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            Toast.makeText(HomeScreen.this, "onRewardedAdClosed", Toast.LENGTH_SHORT).show();
                            HomeScreen.this.loadRewardedAd();
                        }

                        @Override
                        public void onUserEarnedReward(RewardItem rewardItem) {
                            Toast.makeText(HomeScreen.this, "onUserEarnedReward", Toast.LENGTH_SHORT).show();
                            addCoins(rewardItem.getAmount());
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            Toast.makeText(HomeScreen.this, "onRewardedAdFailedToShow", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    };
            rewardedAd.show(this, adCallback);
        }
    }
}


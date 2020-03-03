package com.example.adviewer.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.adviewer.R;
import com.example.adviewer.model.AdsStats;
import com.example.adviewer.model.AdsStatsDatabase;
import com.example.adviewer.model.User;
import com.example.adviewer.utility.AppUtilities;
import com.example.adviewer.view.SplashScreen;
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

import java.util.Calendar;
import java.util.Date;

public class HomeViewModel extends BaseObservable {

    private String title;
    ViewModelListener viewModelListener;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    private static final String REWARD_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private RewardedAd rewardedAd;
    private InterstitialAd interstitialAd;
    private final Context context;
    private AdsStats adsStats = new AdsStats();
    User user = new User();
    private AdsStatsDatabase adsStatsDatabase;
    private String interstitialCount = "0";
    private String rewardCount = "0";
    private String durationCount = "0 sec";
    private static long addOpened, addClosed;
    private int newReward;
    private AppUtilities appUtilities;
    private String interstitialDayCount = "0";
    private String interstitialMonthCount = "0";
    private String rewardDayCount = "0";

    @Bindable
    public String getInterstitialDayCount() {
        return interstitialDayCount;
    }

    @Bindable
    public void setInterstitialDayCount(String interstitialDayCount) {
        this.interstitialDayCount = interstitialDayCount;
        notifyPropertyChanged(BR.interstitialDayCount);
    }

    @Bindable
    public String getInterstitialMonthCount() {
        return interstitialMonthCount;
    }

    @Bindable
    public void setInterstitialMonthCount(String interstitialMonthCount) {
        this.interstitialMonthCount = interstitialMonthCount;
        notifyPropertyChanged(BR.interstitialMonthCount);
    }

    @Bindable
    public String getRewardDayCount() {
        return rewardDayCount;
    }

    @Bindable
    public void setRewardDayCount(String rewardDayCount) {
        this.rewardDayCount = rewardDayCount;
        notifyPropertyChanged(BR.rewardDayCount);
    }

    @Bindable
    public String getRewardMonthCount() {
        return rewardMonthCount;
    }

    @Bindable
    public void setRewardMonthCount(String rewardMonthCount) {
        this.rewardMonthCount = rewardMonthCount;
        notifyPropertyChanged(BR.rewardMonthCount);
    }

    private String rewardMonthCount = "0";

    @Bindable
    public String getInterstitialCount() {
        return String.valueOf(interstitialCount);
    }

    @Bindable
    public void setInterstitialCount(String interstitialCount) {

        this.interstitialCount = interstitialCount;
        notifyPropertyChanged(BR.interstitialCount);
    }

    @Bindable
    public String getRewardCount() {
        return rewardCount;
    }

    @Bindable
    public void setRewardCount(String rewardCount) {
        this.rewardCount = rewardCount;
        notifyPropertyChanged(BR.rewardCount);
    }

    @Bindable
    public String getDurationCount() {
        return durationCount;

    }

    @Bindable
    public void setDurationCount(String durationCount) {
        this.durationCount = durationCount;
        notifyPropertyChanged(BR.durationCount);
    }

    public HomeViewModel(Context context) {
        this.context = context;
        adsStatsDatabase = new AdsStatsDatabase(context);
        if (context instanceof ViewModelListener) {
            viewModelListener = (ViewModelListener) context;
        }
        appUtilities = new AppUtilities(context);
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        setInterstitialAd();
        setRewardedAd();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String numOfInterstitialAds = preferences.getString("rewardAds", "0");
        newReward = adsStats.getNumberOfRewardAdsWatched() + Integer.valueOf(numOfInterstitialAds);

        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        preferences.edit().putInt("currentMonth", month).apply();
        preferences.edit().putInt("currentDate", day).apply();


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void onInterstitialClicked(View view) {
        if (appUtilities.isNetworkAvailable()) {
            showInterstitial();
        } else {
            Toast.makeText(context, R.string.ad_network_error, Toast.LENGTH_SHORT).show();
        }

    }

    public void onRewardClicked(View view) {
        if (appUtilities.isNetworkAvailable()) {
            showRewardedVideo();
        } else {
            Toast.makeText(context, R.string.ad_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void showInterstitial() {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String rewardDuration = preferences.getString("rewardAdDuration", "0");
            interstitialAd.show();
            adsStats.addNumberOfInterstitialAdsWatched();
            preferences.edit().putString("interstitialAds", String.valueOf(adsStats.getNumberOfInterstitialAdsWatched())).apply();
            setInterstitialCount(String.valueOf(adsStats.getNumberOfInterstitialAdsWatched()));
            adsStatsDatabase.addAdsData(adsStats);
        } else {
            Toast.makeText(context, R.string.interstitial_ad_error, Toast.LENGTH_SHORT).show();
            startInterstitial();
        }
    }

    public void setInterstitialAd() {
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(AD_UNIT_ID);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(context, R.string.interstitial_ad_loaded, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (errorCode == 0) {
                    Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                } else if (errorCode == 1) {
                    Toast.makeText(context, R.string.invalid_id_error, Toast.LENGTH_SHORT).show();
                } else if (errorCode == 2) {
                    Toast.makeText(context, R.string.ad_network_error, Toast.LENGTH_SHORT).show();
                } else if (errorCode == 3) {
                    Toast.makeText(context, R.string.lack_of_inventory_error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.ad_error_general, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onAdClosed() {
                startInterstitial();
            }
        });
    }

    private void startInterstitial() {
        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }
    }

    private void setRewardedAd() {
        if (rewardedAd == null || !rewardedAd.isLoaded()) {
            rewardedAd = new RewardedAd(context, REWARD_AD_UNIT_ID);
            rewardedAd.loadAd(
                    new AdRequest.Builder().build(),
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onRewardedAdLoaded() {
                            Toast.makeText(context, R.string.reward_ad_loaded, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdFailedToLoad(int errorCode) {
                            if (errorCode == 0) {
                                Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                            } else if (errorCode == 1) {
                                Toast.makeText(context, R.string.invalid_id_error, Toast.LENGTH_SHORT).show();
                            } else if (errorCode == 2) {
                                Toast.makeText(context, R.string.ad_network_error, Toast.LENGTH_SHORT).show();
                            } else if (errorCode == 3) {
                                Toast.makeText(context, R.string.lack_of_inventory_error, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.ad_error_general, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void startReward() {
        if (!rewardedAd.isLoaded()) {
            setRewardedAd();
        }
    }

    private void showRewardedVideo() {
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback =
                    new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                            addOpened = System.currentTimeMillis();
                            adsStats.addNumberOfRewardAdsWatched();
                            //Toast.makeText(context, "onRewardedAdOpened", Toast.LENGTH_SHORT).show();
                            int currentDate = preferences.getInt("currentDate", 0);
                            int initialDate = preferences.getInt("initialDay", 0);
                            int currentMonth = preferences.getInt("currentMonth", 0);
                            int initialMonth = preferences.getInt("initialMonth", 0);
                            if(currentDate != initialDate){
                                preferences.edit().putInt("initialDay", currentDate).apply();
                                adsStats.setNumberOfRewardAdsDay(1);
                            }
                            else{
                                adsStats.addNumberOfRewardAdsDay();
                            }
                            if(currentMonth != initialMonth){
                                preferences.edit().putInt("initialMonth", currentDate).apply();
                                adsStats.setNumberOfRewardAdsMonth(1);
                            }
                            else{
                                adsStats.addNumberOfRewardAdsMonth();
                            }
                            adsStatsDatabase.addAdsData(adsStats);

                            preferences.edit().putString("rewardAds", String.valueOf(adsStats.getNumberOfRewardAdsWatched())).apply();
                            setRewardCount(String.valueOf(newReward));
                            newReward = 0;
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                            String rewardDuration = preferences.getString("rewardAdDuration", "0");
                            addClosed = System.currentTimeMillis();
                            //Toast.makeText(context, "onRewardedAdClosed", Toast.LENGTH_SHORT).show();
                            setRewardedAd();
                            long duration = addClosed - addOpened;
                            duration = duration / 1000;
                            duration = duration + Long.valueOf(rewardDuration);
                            setDurationCount(String.valueOf(duration) + " sec");
                            preferences.edit().putString("rewardAdDuration", String.valueOf(duration)).apply();
                        }

                        @Override
                        public void onUserEarnedReward(RewardItem rewardItem) {
                            //Toast.makeText(context, "onUserEarnedReward", Toast.LENGTH_SHORT).show();
                            //startReward();
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            Toast.makeText(context, R.string.reward_ad_failed_error, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    };
            rewardedAd.show((Activity) context, adCallback);
        }
    }
}

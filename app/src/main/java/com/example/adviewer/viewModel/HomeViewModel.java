package com.example.adviewer.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.adviewer.R;
import com.example.adviewer.model.AdsStats;
import com.example.adviewer.model.AdsStatsDatabase;
import com.example.adviewer.utility.AppUtilities;
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

public class HomeViewModel extends BaseObservable {

    private String title;
    ViewModelListener viewModelListener;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    private static final String REWARD_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private RewardedAd rewardedAd;
    private InterstitialAd interstitialAd;
    private final Context context;
    private AdsStats adsStats = new AdsStats();
    private String interstitialCount = "0";
    private String rewardCount = "0";
    private String durationCount = "0 sec";
    private static long addOpened, addClosed;
    private AppUtilities appUtilities;
    private String interstitialDayCount = "0";
    private String interstitialMonthCount = "0";
    private String rewardDayCount = "0";
    private String rewardMonthCount = "0";
    public AdsStatsDatabase adsStatsDatabase;


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
        if (context instanceof ViewModelListener) {
            viewModelListener = (ViewModelListener) context;
        }
        appUtilities = new AppUtilities(context);
        adsStatsDatabase = new AdsStatsDatabase(context);
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        setInterstitialAd();
        startInterstitial();
        setRewardedAd();
        startReward();
        showStats();

    }

    public void userDatabaseCheck() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");
        adsStats.setUser(userEmail);
        if (!adsStatsDatabase.checkUserStats(userEmail)) {
            adsStatsDatabase.addAdsData(adsStats);
        }
        else {
            adsStatsDatabase.updateAdsData(adsStats);
        }
    }

    public void showStats() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CALENDAR", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int initialDate = sharedPreferences.getInt("initialDay", 0);
        int initialMonth = sharedPreferences.getInt("initialMonth", 0);

        if (appUtilities.currentDate() != initialDate || (appUtilities.currentDate() == initialDate && appUtilities.currentMonth() != initialMonth )) {
            editor.putInt("initialDay", appUtilities.currentDate()).apply();
            editor.putString("rewardAdsDay", "0").apply();
            editor.putString("interAdsDay", "0").apply();
            adsStats.setNumberOfRewardAdsDay(0);
            adsStats.setNumberOfInterstitialAdsDay(0);
        }

        if (appUtilities.currentMonth() != initialMonth) {
            editor.putInt("initialMonth", appUtilities.currentMonth()).apply();
            editor.putString("rewardAdsMonth", "0").apply();
            editor.putString("interAdsMonth", "0").apply();
            adsStats.setNumberOfRewardAdsMonth(0);
            adsStats.setNumberOfInterstitialAdsMonth(0);
        }
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
            SharedPreferences sharedPreferences = context.getSharedPreferences("CALENDAR", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            interstitialAd.show();

            adsStats.addNumberOfInterstitialAdsDay();
            adsStats.addNumberOfInterstitialAdsMonth();
            adsStats.addNumberOfInterstitialAdsWatched();

            String interstTotal = sharedPreferences.getString("interstitialAds", "0");
            String interstDay = sharedPreferences.getString("interAdsDay", "0");
            String interstMonth = sharedPreferences.getString("interAdsMonth", "0");

            int intTotal = 1 + Integer.valueOf(interstTotal);
            int intDay = 1 + Integer.valueOf(interstDay);
            int intMonth = 1 + Integer.valueOf(interstMonth);

            editor.putString("interAdsDay", String.valueOf(intDay)).apply();
            editor.putString("interAdsMonth", String.valueOf(intMonth)).apply();
            editor.putString("interstitialAds", String.valueOf(intTotal)).apply();

            setInterstitialCount(String.valueOf(intTotal));
            adsStats.setNumberOfInterstitialAdsWatched(intTotal);
            setInterstitialDayCount(String.valueOf(intDay));
            adsStats.setNumberOfInterstitialAdsDay(intDay);
            setInterstitialMonthCount(String.valueOf(intMonth));
            adsStats.setNumberOfInterstitialAdsMonth(intMonth);

            userDatabaseCheck();
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
                //Toast.makeText(context, R.string.interstitial_ad_loaded, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
//                if (errorCode == 0) {
//                    Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
//                } else if (errorCode == 1) {
//                    Toast.makeText(context, R.string.invalid_id_error, Toast.LENGTH_SHORT).show();
//                } else if (errorCode == 2) {
//                    Toast.makeText(context, R.string.ad_network_error, Toast.LENGTH_SHORT).show();
//                } else if (errorCode == 3) {
//                    Toast.makeText(context, R.string.lack_of_inventory_error, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, R.string.ad_error_general, Toast.LENGTH_SHORT).show();
//                }

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
                            //Toast.makeText(context, R.string.reward_ad_loaded, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdFailedToLoad(int errorCode) {
//                            if (errorCode == 0) {
//                                Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
//                            } else if (errorCode == 1) {
//                                Toast.makeText(context, R.string.invalid_id_error, Toast.LENGTH_SHORT).show();
//                            } else if (errorCode == 2) {
//                                Toast.makeText(context, R.string.ad_network_error, Toast.LENGTH_SHORT).show();
//                            } else if (errorCode == 3) {
//                                Toast.makeText(context, R.string.lack_of_inventory_error, Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(context, R.string.ad_error_general, Toast.LENGTH_SHORT).show();
//                            }
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
                            SharedPreferences sharedPreferences = context.getSharedPreferences("CALENDAR", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            addOpened = System.currentTimeMillis();
                            adsStats.addNumberOfRewardAdsWatched();


                            adsStats.addNumberOfRewardAdsMonth();
                            adsStats.addNumberOfRewardAdsDay();

                            String rewardTotal = sharedPreferences.getString("rewardAds", "0");
                            String rewardDay = sharedPreferences.getString("rewardAdsDay", "0");
                            String rewardMonth = sharedPreferences.getString("rewardAdsMonth", "0");

                            int rwdTotal = 1 + Integer.valueOf(rewardTotal);
                            int rwdDay = 1 + Integer.valueOf(rewardDay);
                            int rwdMonth = 1 + Integer.valueOf(rewardMonth);

                            editor.putString("rewardAdsDay", String.valueOf(rwdDay)).apply();
                            editor.putString("rewardAdsMonth", String.valueOf(rwdMonth)).apply();
                            editor.putString("rewardAds", String.valueOf(rwdTotal)).apply();

                            setRewardCount(String.valueOf(rwdTotal));
                            adsStats.setNumberOfRewardAdsWatched(rwdTotal);
                            setRewardDayCount(String.valueOf(rwdDay));
                            adsStats.setNumberOfRewardAdsDay(rwdDay);
                            setRewardMonthCount(String.valueOf(rwdMonth));
                            adsStats.setNumberOfRewardAdsMonth(rwdMonth);

                            userDatabaseCheck();
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            SharedPreferences sharedPreferences = context.getSharedPreferences("CALENDAR", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            String rewardDuration = sharedPreferences.getString("rewardAdDuration", "0");
                            addClosed = System.currentTimeMillis();
                            setRewardedAd();
                            long duration = addClosed - addOpened;
                            duration = duration / 1000;
                            duration = duration + Long.valueOf(rewardDuration);
                            setDurationCount(duration + " sec");
                            adsStats.setDurationOfAd(duration);
                            editor.putString("rewardAdDuration", String.valueOf(duration)).apply();

                            userDatabaseCheck();
                        }

                        @Override
                        public void onUserEarnedReward(RewardItem rewardItem) {
                            //Toast.makeText(context, "onUserEarnedReward", Toast.LENGTH_SHORT).show();
                            //startReward();
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
//                            if (errorCode == 0) {
//                                Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
//                            } else if (errorCode == 1) {
//                                Toast.makeText(context, R.string.invalid_id_error, Toast.LENGTH_SHORT).show();
//                            } else if (errorCode == 2) {
//                                Toast.makeText(context, R.string.ad_network_error, Toast.LENGTH_SHORT).show();
//                            } else if (errorCode == 3) {
//                                Toast.makeText(context, R.string.lack_of_inventory_error, Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(context, R.string.reward_ad_failed_error, Toast.LENGTH_SHORT).show();
//                            }
                        }
                    };
            rewardedAd.show((Activity) context, adCallback);
        }
        else {
            Toast.makeText(context, R.string.reward_retry_error, Toast.LENGTH_SHORT).show();
            startReward();
        }
    }
}

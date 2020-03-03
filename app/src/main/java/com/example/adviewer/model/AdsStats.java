package com.example.adviewer.model;

public class AdsStats {
    private int id;
    private int numberOfRewardAdsWatched = 0;
    private int numberOfInterstitialAdsWatched = 0;
    private int numberOfRewardAdsDay = 0;
    private int numberOfInterstitialAdsDay = 0;
    private int numberOfRewardAdsMonth = 0;
    private int numberOfInterstitialAdsMonth = 0;
    private int durationOfAd = 0;
    private int totalAdsWatched = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfRewardAdsDay() {
        return numberOfRewardAdsDay;
    }

    public void setNumberOfRewardAdsDay(int numberOfRewardAdsDay) {
        this.numberOfRewardAdsDay = numberOfRewardAdsDay;
    }

    public int getNumberOfInterstitialAdsDay() {
        return numberOfInterstitialAdsDay;
    }

    public void setNumberOfInterstitialAdsDay(int numberOfInterstitialAdsDay) {
        this.numberOfInterstitialAdsDay = numberOfInterstitialAdsDay;
    }

    public int getNumberOfRewardAdsMonth() {
        return numberOfRewardAdsMonth;
    }

    public void setNumberOfRewardAdsMonth(int numberOfRewardAdsMonth) {
        this.numberOfRewardAdsMonth = numberOfRewardAdsMonth;
    }

    public int getNumberOfInterstitialAdsMonth() {
        return numberOfInterstitialAdsMonth;
    }

    public void setNumberOfInterstitialAdsMonth(int numberOfInterstitialAdsMonth) {
        this.numberOfInterstitialAdsMonth = numberOfInterstitialAdsMonth;
    }

    public int getTotalAdsWatched() {
        return totalAdsWatched;
    }

    public void setTotalAdsWatched() {
        this.totalAdsWatched = numberOfInterstitialAdsWatched + numberOfRewardAdsWatched;
    }

    public int getNumberOfInterstitialAdsWatched() {
        return numberOfInterstitialAdsWatched;
    }

    public void setNumberOfInterstitialAdsWatched(int numberOfInterstitialAdsWatched) {
        this.numberOfInterstitialAdsWatched = numberOfInterstitialAdsWatched;
    }

    public int getNumberOfRewardAdsWatched() {
        return numberOfRewardAdsWatched;
    }

    public void setNumberOfRewardAdsWatched(int numberOfAdsWatched) {
        this.numberOfRewardAdsWatched = numberOfAdsWatched;
    }

    public int getDurationOfAd() {
        return durationOfAd;
    }

    public void setDurationOfAd(int durationOfAd) {
        this.durationOfAd = durationOfAd;
    }

    public void addNumberOfRewardAdsWatched() {
        this.numberOfRewardAdsWatched += 1;
    }

    public void addNumberOfInterstitialAdsWatched() {
         this.numberOfInterstitialAdsWatched += 1;
    }

    public void addNumberOfRewardAdsDay() {
        this.numberOfRewardAdsWatched += 1;
    }

    public void addNumberOfInterstitialAdsDay() {
        this.numberOfInterstitialAdsWatched += 1;
    }

    public void addNumberOfRewardAdsMonth() {
        this.numberOfRewardAdsWatched += 1;
    }

    public void addNumberOfInterstitialAdsMonth() {
        this.numberOfInterstitialAdsWatched += 1;
    }
}

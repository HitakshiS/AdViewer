package com.example.adviewer.model;

public class AdsStats {
    private int numberOfAdsWatched =0;
    private int durationOfAd;

    public AdsStats(int numberOfAdsWatched) {
        this.numberOfAdsWatched = numberOfAdsWatched;
    }

    public AdsStats(int numberOfAdsWatched, int durationOfAd) {
        this.numberOfAdsWatched = numberOfAdsWatched;
        this.durationOfAd = durationOfAd;
    }

    public int getNumberOfAdsWatched() {
        return numberOfAdsWatched;
    }

    public void setNumberOfAdsWatched(int numberOfAdsWatched) {
        this.numberOfAdsWatched = numberOfAdsWatched;
    }

    public int getDurationOfAd() {
        return durationOfAd;
    }

    public void setDurationOfAd(int durationOfAd) {
        this.durationOfAd = durationOfAd;
    }

    public int addNumberOfAdsWatched() {
        return numberOfAdsWatched+1;
    }
}

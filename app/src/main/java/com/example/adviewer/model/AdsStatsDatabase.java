package com.example.adviewer.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AdsStatsDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public AdsStatsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Database Name
    private static final String DATABASE_NAME = "AdsStatsManager.db";

    // User table name
    private static final String TABLE_ADS = "adsStats";

    // User Table Columns names
    private static final String COLUMN_ADS_ID = "id";
    private static final String COLUMN_ADS_INTERSTITIAL = "ad_interstitial";
    private static final String COLUMN_ADS_REWARD = "user_email";
    private static final String COLUMN_ADS_DURATION = "user_password";

    // create table sql query
    private String CREATE_ADS_TABLE = "CREATE TABLE " + TABLE_ADS + "("
            + COLUMN_ADS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ADS_INTERSTITIAL + " INTEGER,"
            + COLUMN_ADS_REWARD + " INTEGER," + COLUMN_ADS_DURATION + " INTEGER" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_ADS;


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);

    }

    public void addAdsData(AdsStats adsStats) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ADS_INTERSTITIAL, adsStats.getNumberOfInterstitialAdsWatched());
        values.put(COLUMN_ADS_REWARD, adsStats.getNumberOfRewardAdsWatched());
        values.put(COLUMN_ADS_DURATION, adsStats.getDurationOfAd());
        db.insert(TABLE_ADS, null, values);
        db.close();
    }

    public List<AdsStats> getAllAdsData() {
        String[] columns = {
                COLUMN_ADS_ID,
                COLUMN_ADS_REWARD,
                COLUMN_ADS_INTERSTITIAL,
                COLUMN_ADS_DURATION
        };
        String sortOrder =
                COLUMN_ADS_INTERSTITIAL + " ASC";
        List<AdsStats> adsStatsList = new ArrayList<AdsStats>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADS,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()) {
            do {
                AdsStats adsStatsData = new AdsStats();
                adsStatsData.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ADS_ID))));
                adsStatsData.setNumberOfInterstitialAdsWatched(cursor.getInt(cursor.getColumnIndex(COLUMN_ADS_INTERSTITIAL)));
                adsStatsData.setNumberOfRewardAdsWatched(cursor.getInt(cursor.getColumnIndex(COLUMN_ADS_REWARD)));
                adsStatsData.setDurationOfAd(cursor.getInt(cursor.getColumnIndex(COLUMN_ADS_DURATION)));
                adsStatsList.add(adsStatsData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return adsStatsList;
    }

    public void updateAdsData(AdsStats adsStats) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ADS_INTERSTITIAL, adsStats.getNumberOfInterstitialAdsWatched());
        values.put(COLUMN_ADS_REWARD, adsStats.getNumberOfRewardAdsWatched());
        values.put(COLUMN_ADS_DURATION, adsStats.getDurationOfAd());
        db.update(TABLE_ADS, values, COLUMN_ADS_ID + " = ?",
                new String[]{String.valueOf(adsStats.getId())});
        db.close();
    }

    public void deleteAd(AdsStats adsStats) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADS, COLUMN_ADS_ID + " = ?",
                new String[]{String.valueOf(adsStats.getId())});
        db.close();
    }

}

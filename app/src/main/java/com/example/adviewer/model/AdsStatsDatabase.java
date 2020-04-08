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

    private static final String DATABASE_NAME = "AdsStatsManager.db";

    private static final String TABLE_ADS = "adsStats";

    private static final String COLUMN_ADS_ID = "id";
    private static final String USER = "user";
    private static final String COLUMN_ADS_INTERSTITIAL = "ad_interstitial";
    private static final String COLUMN_ADS_INTERSTITIAL_DAY = "ad_interstitial_day";
    private static final String COLUMN_ADS_INTERSTITIAL_MONTH = "ad_interstitial_month";
    private static final String COLUMN_ADS_REWARD = "ad_reward";
    private static final String COLUMN_ADS_DURATION = "ad_duration";
    private static final String COLUMN_ADS_REWARD_DAY = "ad_reward_day";
    private static final String COLUMN_ADS_REWARD_MONTH = "ad_reward_month";

    private String CREATE_ADS_TABLE = "CREATE TABLE " + TABLE_ADS + "("
            + COLUMN_ADS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER + " TEXT,"
            + COLUMN_ADS_INTERSTITIAL + " INTEGER," + COLUMN_ADS_INTERSTITIAL_DAY + " INTEGER," + COLUMN_ADS_INTERSTITIAL_MONTH + " INTEGER,"
            + COLUMN_ADS_REWARD + " INTEGER," + COLUMN_ADS_REWARD_DAY + " INTEGER," + COLUMN_ADS_REWARD_MONTH + " INTEGER,"
            + COLUMN_ADS_DURATION + " INTEGER" + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_ADS;

//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_ADS_TABLE);
//    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table adsStats " +
                        "(id integer primary key NOT NULL, user text NOT NULL, ad_interstitial integer , " +
                        "ad_interstitial_day integer, ad_interstitial_month integer ," +
                        "ad_reward integer, ad_reward_day integer, ad_reward_month integer ," +
                        "ad_duration integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);

    }

    public void addAdsData(AdsStats adsStats) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER, adsStats.getUser());
        values.put(COLUMN_ADS_INTERSTITIAL, adsStats.getNumberOfInterstitialAdsWatched());
        values.put(COLUMN_ADS_INTERSTITIAL_DAY, adsStats.getNumberOfInterstitialAdsDay());
        values.put(COLUMN_ADS_INTERSTITIAL_MONTH, adsStats.getNumberOfInterstitialAdsMonth());
        values.put(COLUMN_ADS_REWARD, adsStats.getNumberOfRewardAdsWatched());
        values.put(COLUMN_ADS_REWARD_DAY, adsStats.getNumberOfRewardAdsDay());
        values.put(COLUMN_ADS_REWARD_MONTH, adsStats.getNumberOfRewardAdsMonth());
        values.put(COLUMN_ADS_DURATION, adsStats.getDurationOfAd());
        db.insert(TABLE_ADS, null, values);
        db.close();
    }

    public List<AdsStats> getAllAdsData() {
        String[] columns = {
                COLUMN_ADS_ID,
                USER,
                COLUMN_ADS_INTERSTITIAL,
                COLUMN_ADS_INTERSTITIAL_DAY,
                COLUMN_ADS_INTERSTITIAL_MONTH,
                COLUMN_ADS_REWARD,
                COLUMN_ADS_REWARD_DAY,
                COLUMN_ADS_REWARD_MONTH,
                COLUMN_ADS_DURATION
        };
        String sortOrder =
                USER + " ASC";
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
                adsStatsData.setUser(cursor.getString(cursor.getColumnIndex(USER)));
                adsStatsData.setNumberOfInterstitialAdsWatched(cursor.getInt(cursor.getColumnIndex(COLUMN_ADS_INTERSTITIAL)));
                adsStatsData.setNumberOfInterstitialAdsDay(cursor.getInt(cursor.getColumnIndex(COLUMN_ADS_INTERSTITIAL_DAY)));
                adsStatsData.setNumberOfInterstitialAdsMonth(cursor.getInt(cursor.getColumnIndex(COLUMN_ADS_INTERSTITIAL_MONTH)));
                adsStatsData.setNumberOfRewardAdsWatched(cursor.getInt(cursor.getColumnIndex(COLUMN_ADS_REWARD)));
                adsStatsData.setNumberOfRewardAdsDay(cursor.getInt(cursor.getColumnIndex(COLUMN_ADS_REWARD_DAY)));
                adsStatsData.setNumberOfRewardAdsMonth(cursor.getInt(cursor.getColumnIndex(COLUMN_ADS_REWARD_MONTH)));
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
        values.put(USER, adsStats.getUser());
        values.put(COLUMN_ADS_INTERSTITIAL, adsStats.getNumberOfInterstitialAdsWatched());
        values.put(COLUMN_ADS_INTERSTITIAL_DAY, adsStats.getNumberOfInterstitialAdsWatched());
        values.put(COLUMN_ADS_INTERSTITIAL_MONTH, adsStats.getNumberOfInterstitialAdsWatched());
        values.put(COLUMN_ADS_REWARD, adsStats.getNumberOfRewardAdsWatched());
        values.put(COLUMN_ADS_REWARD_DAY, adsStats.getNumberOfRewardAdsDay());
        values.put(COLUMN_ADS_REWARD_MONTH, adsStats.getNumberOfRewardAdsMonth());
        values.put(COLUMN_ADS_DURATION, adsStats.getDurationOfAd());
        db.update(TABLE_ADS, values, USER + " = ?",
                new String[]{String.valueOf(adsStats.getUser())});
        db.close();
    }

    public void deleteAd(AdsStats adsStats) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADS, COLUMN_ADS_ID + " = ?",
                new String[]{String.valueOf(adsStats.getId())});
        db.close();
    }

    public boolean checkUserStats(String user) {

        String[] columns = {
                COLUMN_ADS_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = USER + " = ?";

        String[] selectionArgs = {user};

        Cursor cursor = db.query(TABLE_ADS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }

    public Cursor getUserStats(String startUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from adsStats where user = '" + startUser + "'", null );
        return res;
    }
}

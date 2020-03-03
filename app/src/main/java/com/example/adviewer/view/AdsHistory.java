package com.example.adviewer.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.example.adviewer.R;

import me.ithebk.barchart.BarChart;
import me.ithebk.barchart.BarChartModel;

public class AdsHistory extends Activity {

    private String[] mMonth = new String[] {
            "Jan", "Feb" , "Mar", "Apr", "May", "Jun",
            "Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        BarChart barChart = (BarChart) findViewById(R.id.bar_chart_vertical);
        barChart.setBarMaxValue(100);

        BarChartModel interstitialBarChartModel = new BarChartModel();
        interstitialBarChartModel.setBarValue(50);
        interstitialBarChartModel.setBarColor(Color.parseColor("#9C27B0"));
        interstitialBarChartModel.setBarTag("Interstitial"); //You can set your own tag to bar model
        interstitialBarChartModel.setBarText("50");

        barChart.addBar(interstitialBarChartModel);

        BarChartModel rewardBarChartModel = new BarChartModel();
        rewardBarChartModel.setBarValue(100);
        rewardBarChartModel.setBarColor(Color.parseColor("#808080"));
        rewardBarChartModel.setBarTag("Reward"); //You can set your own tag to bar model
        rewardBarChartModel.setBarText("100");

        barChart.addBar(rewardBarChartModel);
    }

}

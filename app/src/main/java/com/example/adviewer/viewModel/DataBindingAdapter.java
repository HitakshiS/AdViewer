package com.example.adviewer.viewModel;

import android.widget.TextView;
import androidx.databinding.BindingAdapter;

public class DataBindingAdapter {
    @BindingAdapter("android:text")
    public static void setTextViewText(TextView textView, String text) {
        if (text != null) {
            textView.setText(text);
        } else {
            textView.setText("");
        }
    }
}
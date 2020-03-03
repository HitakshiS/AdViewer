package com.example.adviewer.viewModel;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.ObservableField;
import androidx.databinding.adapters.AdapterViewBindingAdapter;

import com.example.adviewer.R;

public class DataBindingAdapter {
    @BindingAdapter("android:text")
    public static void setTextViewText(TextView textView, String text) {
        if (text != null) {
            textView.setText(text);
        } else {
            textView.setText("");
        }
    }

//    @InverseBindingAdapter(attribute = "selectedItem", event = "selectedItemAttrChanged")
//    public static String getSelectedItem(Spinner spinner) {
//
//        String selectedItem = spinner.getSelectedItem().toString();
//
//        return selectedItem;
//
//    }

//    @BindingAdapter(value = {"selectedItem"})
//    public static void setSelectedItem(Spinner spinner, String spinnerItem) {
//        if (spinner.getAdapter() == null) {
//            return;
//        }
//        // Other code omitted for simplicity
//    }
//
//    @BindingAdapter("android:selectedItemPosition")
//    public static void setSelectedItemPosition(AdapterView view, int position) {
//        if (view.getSelectedItemPosition() != position) {
//            view.setSelection(position);
//        }
//    }
//
//    @BindingAdapter(value = {"android:onItemSelected", "android:onNothingSelected",
//            "android:selectedItemPositionAttrChanged" }, requireAll = false)
//    public static void setOnItemSelectedListener(AdapterView view, final AdapterViewBindingAdapter.OnItemSelected selected,
//                                                 final AdapterViewBindingAdapter.OnNothingSelected nothingSelected, final InverseBindingListener attrChanged) {
//        if (selected == null && nothingSelected == null && attrChanged == null) {
//            view.setOnItemSelectedListener(null);
//        } else {
//            view.setOnItemSelectedListener(
//                    new AdapterViewBindingAdapter.OnItemSelectedComponentListener(selected, nothingSelected, attrChanged));
//        }
//    }
}
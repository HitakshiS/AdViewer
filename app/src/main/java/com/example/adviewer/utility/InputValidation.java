package com.example.adviewer.utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputValidation {
    private Context context;

        public InputValidation(Context context) {
            this.context = context;
        }

        public boolean isInputEditTextFilled(EditText textInputEditText) {
            String value = textInputEditText.getText().toString().trim();
            if (value.isEmpty()) {
                hideKeyboardFrom(textInputEditText);
                return false;
            } else {
                return true;
            }
        }

        public boolean isInputEditTextEmail(EditText textInputEditText) {
            String value = textInputEditText.getText().toString().trim();
            if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                hideKeyboardFrom(textInputEditText);
                return false;
            } else {
                return true;
            }

        }

    public boolean isInputEditTextEmail(String value) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return value.matches(regex);
    }

        public boolean isInputEditTextMatches(EditText textInputEditText1, EditText textInputEditText2) {
            String value1 = textInputEditText1.getText().toString().trim();
            String value2 = textInputEditText2.getText().toString().trim();
            if (!value1.contentEquals(value2)) {
                hideKeyboardFrom(textInputEditText2);
                return false;
            } else {
               return true;
            }
        }

        private void hideKeyboardFrom(View view) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
}

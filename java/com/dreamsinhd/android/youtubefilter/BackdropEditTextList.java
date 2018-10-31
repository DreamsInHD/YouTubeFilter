package com.dreamsinhd.android.youtubefilter;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.util.Pair;
import android.view.View;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BackdropEditTextList {
    private List<TextInputEditText> views;
    private Context context;

    public BackdropEditTextList(Context context, TextInputEditText... views) {
        this.context = context;
        this.views = new ArrayList<>(Arrays.asList(views));
    }

    public void addOnKeyListenerToEditTextViews(View.OnKeyListener listener) {
        for (TextInputEditText editText : views) {
            editText.setOnKeyListener(listener);
        }
    }

    public boolean validInput() {
        boolean validInput = true;
        for(TextInputEditText editText : views) {
            if(editText.getText() == null || editText.getText().toString().equals("") || isValidBigInt(editText.getText().toString())) {
                editText.setError(null);
            } else if(editText.getText() != null && !isValidBigInt(editText.getText().toString())) {
                editText.setError(context.getString(R.string.not_a_valid_number));
                validInput = false;
            }
        }
        return validInput;
    }

    private boolean isValidBigInt(String bigIntString) {
        if (bigIntString != null) {
            try {
                if (new BigInteger(bigIntString).compareTo(BigInteger.valueOf(0)) < 0) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
        return false;
    }
}

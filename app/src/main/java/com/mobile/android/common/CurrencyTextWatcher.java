package com.mobile.android.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.mobile.android.client.utilities.StringUtilities;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by Josh on 10/21/15.
 */
public class CurrencyTextWatcher implements TextWatcher {
    private final WeakReference<EditText> editTextWeakReference;

    public CurrencyTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<EditText>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null)
            return;

        String string = editable.toString();
        if(StringUtilities.IsNullOrEmpty(string))
            return;

        editText.removeTextChangedListener(this);

        String cleanString = string.replaceAll("[$,.]", "");

        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);

        String formatted = NumberFormat.getCurrencyInstance().format(parsed);

        editText.setText(formatted);

        editText.setSelection(formatted.length());

        editText.addTextChangedListener(this);
    }
}

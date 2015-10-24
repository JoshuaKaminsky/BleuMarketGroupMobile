/*
 * TypefaceTextView.java
 * Simple
 *
 * Copyright 2012 Simple Finance Corporation (https://www.simple.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobile.android.common;

import java.util.Hashtable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.mobile.bmg.R;

public class FontableTextView extends TextView {
	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public FontableTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        
        TypedArray a = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.FontableTextView, 0, 0);
        
        setTypeface(a.getString(R.styleable.FontableTextView_typeface));

        setOpacity(a.getFloat(R.styleable.FontableTextView_alpha, 1));

        a.recycle();
    }

    public void setTypeface(String typefaceName) {
        if (!isInEditMode() && !TextUtils.isEmpty(typefaceName)) {
            synchronized (cache) {
                String assetPath = String.format("fonts/%s", typefaceName);
                Typeface typeface = null;

                if (!cache.containsKey(assetPath)) {
                    try {
                        typeface = Typeface.createFromAsset(getContext().getAssets(), assetPath);
                        cache.put(assetPath, typeface);
                    } catch (Exception e) {
                        Log.e("Typefaces", "Could not get typeface '" + assetPath + "' because " + e.getMessage());
                        return;
                    }
                }

                typeface = cache.get(assetPath);

                setTypeface(typeface);

                setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
        }
    }

    public void setOpacity(float alpha) {
        int alphaValue = (int)(alpha * 255);

        setTextColor(getTextColors().withAlpha(alphaValue));
        setHintTextColor(getHintTextColors().withAlpha(alphaValue));
        setLinkTextColor(getLinkTextColors().withAlpha(alphaValue));
    }
}

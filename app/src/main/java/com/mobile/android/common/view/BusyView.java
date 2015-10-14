package com.mobile.android.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.mobile.bmg.R;

public class BusyView extends ImageView {

    public BusyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAnimation(attrs);
    }

    public BusyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAnimation(attrs);
    }

    public BusyView(Context context) {
        super(context);
    }

    private void setAnimation(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView);
        int frameCount = a.getInt(R.styleable.ProgressView_frameCount, 12);  
        int duration = a.getInt(R.styleable.ProgressView_duration, 1000);
        a.recycle();

        setAnimation(frameCount, duration);
    }

    public void setAnimation(final int frameCount, final int duration) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.busy);
        a.setDuration(duration);
        a.setInterpolator(new Interpolator() {

            @Override
            public float getInterpolation(float input) {
                return (float)Math.floor(input*frameCount)/frameCount;
            }
        });
        startAnimation(a);
    }
}

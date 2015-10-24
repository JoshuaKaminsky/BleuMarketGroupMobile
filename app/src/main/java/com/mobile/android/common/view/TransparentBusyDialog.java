package com.mobile.android.common.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobile.bmg.R;

/**
 * Created by Josh on 10/23/15.
 */
public class TransparentBusyDialog extends Dialog {

        private ImageView imageView;

        public TransparentBusyDialog(Context context, int resourceIdOfImage) {
            super(context, R.style.TransparentProgressDialog);

            WindowManager.LayoutParams windowLayoutParams = getWindow().getAttributes();

            windowLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;

            getWindow().setAttributes(windowLayoutParams);

            setTitle(null);

            setCancelable(false);

            setOnCancelListener(null);

            LinearLayout layout = new LinearLayout(context);

            layout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);

            imageView = new ImageView(context);

            imageView.setImageResource(resourceIdOfImage);

            layout.addView(imageView, params);

            addContentView(layout, params);
        }

        @Override
        public void show() {
            super.show();

            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(3000);

            imageView.setAnimation(anim);
            imageView.startAnimation(anim);
        }
}

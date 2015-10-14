package com.mobile.android.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

public class AnimationUtilities {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static void fadeOut(final View fadeOut, int durationInMilliseconds) {
		fadeOut.setVisibility(View.VISIBLE);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
			fadeOut.setVisibility(View.GONE);
		} else {
			fadeOut.animate().alpha(0f).setDuration(durationInMilliseconds)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					fadeOut.setVisibility(View.GONE);
				}
			});
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static void fadeIn(final View fadeIn, int durationInMilliseconds) {
		fadeIn.setVisibility(View.INVISIBLE);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
			fadeIn.setVisibility(View.VISIBLE);
		} else {
			fadeIn.setAlpha(0f);
			fadeIn.animate().alpha(1f).setDuration(durationInMilliseconds);
			fadeIn.setVisibility(View.VISIBLE);
		}
	}
	
	public static void crossfade(View fadeIn, final View fadeOut) {
		crossfade(fadeIn, fadeOut, 500);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static void crossfade(View fadeIn, final View fadeOut, int durationInMilliseconds) {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
			fadeIn.setVisibility(View.VISIBLE);
			fadeOut.setVisibility(View.GONE);
		} else {

			fadeIn.setAlpha(0f);
			fadeIn.setVisibility(View.VISIBLE);

			fadeIn.animate().alpha(1f).setDuration(durationInMilliseconds).setListener(null);

			fadeOut.animate().alpha(0f).setDuration(durationInMilliseconds)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							fadeOut.setVisibility(View.GONE);
						}
					});
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static void verticalFlip(View flipIn, final View flipOut) {
		verticalFlip(flipIn, flipOut, 500);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static void verticalFlip(View flipIn, final View flipOut, int durationInMilliseconds) {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
			flipIn.setVisibility(View.VISIBLE);
			flipOut.setVisibility(View.GONE);
		} else {

			flipIn.setAlpha(0f);
			flipIn.setVisibility(View.VISIBLE);

			flipIn.animate().rotationX(1f).setDuration(durationInMilliseconds).setListener(null);

			flipOut.animate().rotationX(0f).setDuration(durationInMilliseconds)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							flipOut.setVisibility(View.GONE);
						}
					});
		}
	}
}

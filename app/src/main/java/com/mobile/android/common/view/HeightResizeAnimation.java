package com.mobile.android.common.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class HeightResizeAnimation extends Animation {
	View view;
	
	int startHeight;
	int targetHeight;
	int newHeight;
	
	boolean expand;
	
	public HeightResizeAnimation(View view, int targetHeight, int durationInMilliseconds) {
	    this.view = view;
	    this.startHeight = this.view.getMeasuredWidth();
	    this.targetHeight = targetHeight;
	    this.newHeight = this.startHeight;
	    this.expand = startHeight < targetHeight;
	    
	    this.setDuration(durationInMilliseconds);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
	    newHeight = getNewMargin(newHeight, targetHeight, interpolatedTime);
	    
	    view.getLayoutParams().height = newHeight;

	    view.requestLayout();
	}
	
	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
	    super.initialize(width, height, parentWidth, parentHeight);
	}
	
	@Override
	public boolean willChangeBounds() {
	    return true;
	}
	
	private int getNewMargin(int start, int target, float time) {
		if(start < target) {
			return (int)(start + (target - start) * time);
		} 
		
		return (int) (start - (start - target) * time);
	}
}

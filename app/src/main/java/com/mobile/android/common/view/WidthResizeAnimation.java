package com.mobile.android.common.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class WidthResizeAnimation extends Animation {
	View view;
	
	int startWidth;
	int targetWidth;
	int newWidth;
	
	boolean expand;
	
	public WidthResizeAnimation(View view, int targetWidth) {
	    this.view = view;
	    this.startWidth = this.view.getMeasuredWidth();
	    this.targetWidth = targetWidth;
	    this.newWidth = this.startWidth;
	    this.expand = targetWidth < targetWidth;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
	    newWidth = getNewMargin(newWidth, targetWidth, interpolatedTime);
	    
	    view.getLayoutParams().width = newWidth;

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

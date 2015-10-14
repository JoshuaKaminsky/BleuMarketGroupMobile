package com.mobile.android.common;

import java.util.EnumSet;

import com.mobile.android.contract.ITouchListener;

import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class TouchDetector extends SimpleOnGestureListener {

	private int minSwipeDistance = 120;
    private int thresholdSwipeVelocity = 200;
	
    private ITouchListener listener;

    public TouchDetector(ITouchListener listener) {
    	this.listener = listener;
    }
    
    public TouchDetector(ITouchListener listener, int minDistance, int thresholdVelocity) {
    	this.listener = listener;
    	this.minSwipeDistance = minDistance;
    	this.thresholdSwipeVelocity = thresholdVelocity;
    }
	
	@Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
        	FlingGesture vertical = FlingGesture.None;
        	FlingGesture horizontal = FlingGesture.None;
            
        	//up down swipe
        	if (Math.abs(e1.getY() - e2.getY()) > minSwipeDistance && Math.abs(velocityY) > thresholdSwipeVelocity) {
            	vertical = FlingGesture.Up;
            } else if(Math.abs(e2.getY() - e1.getY()) > minSwipeDistance && Math.abs(velocityY) > thresholdSwipeVelocity) {
            	vertical = FlingGesture.Down;
            }

            // right to left swipe
            if(e1.getX() - e2.getX() > minSwipeDistance && Math.abs(velocityX) > thresholdSwipeVelocity) {
            	horizontal = FlingGesture.Right;
            }  else if (e2.getX() - e1.getX() > minSwipeDistance && Math.abs(velocityX) > thresholdSwipeVelocity) {
            	horizontal = FlingGesture.Left;
            }
            
            EnumSet<FlingGesture> movement = EnumSet.of(vertical, horizontal);
            
            movement.remove(FlingGesture.None);
            
            listener.OnFlingGestureCaptured(movement);
            
        } catch (Exception e) {
            // nothing
        }
        
        return true;
    }
	
	@Override
	public boolean onDown(MotionEvent e) {		
		super.onDown(e);
		listener.OnDown();
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		super.onLongPress(e);
		listener.OnLongPress();
	}
}

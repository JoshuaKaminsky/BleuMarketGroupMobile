package com.mobile.android.contract;

import java.util.EnumSet;

import com.mobile.android.common.FlingGesture;

public interface ITouchListener {
	void OnFlingGestureCaptured(EnumSet<FlingGesture> gesture);
	
	void OnDown();
	
	void OnLongPress();
}

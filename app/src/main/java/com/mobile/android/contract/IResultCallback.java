package com.mobile.android.contract;

public interface IResultCallback<T> {

	/**	 
	 * Retrieve the type parameter for this class
	 * 
	 * @return 
	 * The type of the result
	 */
	Class<T> getType();
	
	/**
	 * The callback to be invoked upon post execute
	 * 
	 * @param result
	 * The result of the asynchronous operation
	 */
	void call(T result);
	
}

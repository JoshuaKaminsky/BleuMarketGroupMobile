package com.mobile.android.client.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mobile.android.contract.IIdentifier;


public class CollectionUtilities {

	public static <I extends IIdentifier> I GetItem(Integer id, I[] items)
	{
		if(id == null)
			return null;
		
		for(I item : items)
		{
			if(item.GetId() == id)
				return item;
		}
		
		return null;
	}
	
	public static <I extends IIdentifier> List<I> GetItems(List<Integer> ids, I[] items)
	{
		List<I> result = new ArrayList<I>();
		
		for(int id : ids)
		{
			//this can be optimized by removing found items from the list
			I item = GetItem(id, items);
			if(item != null)
				result.add(item);
		}
		
		return result;
	}

	public static <TItem> List<TItem> ToList(TItem[] items) {
		if(items == null) {
			return new ArrayList<TItem>();
		}

		return Arrays.asList(items);
	}
}

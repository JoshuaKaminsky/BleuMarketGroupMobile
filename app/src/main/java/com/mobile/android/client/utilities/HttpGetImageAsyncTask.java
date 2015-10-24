package com.mobile.android.client.utilities;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import com.mobile.android.contract.IResultCallback;
import com.squareup.okhttp.Response;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class HttpGetImageAsyncTask extends HttpAsyncTask<Void, Bitmap>{
	
	private static WeakReference<ImageView> _imageViewReference;
	private String url;

	public HttpGetImageAsyncTask(ImageView imageView) {
		super(new IResultCallback<Bitmap>() {

			public Class<Bitmap> getType() {
				return Bitmap.class;
			}

			public void call(Bitmap result) {
				if (_imageViewReference != null) {
		            ImageView imageView = _imageViewReference.get();
		            if (imageView != null) {
		                imageView.setImageBitmap(result);
		            }
		        }
				
				return;	
			}
		});
		
		_imageViewReference = new WeakReference<ImageView>(imageView);
	}		
	
	public HttpGetImageAsyncTask(IResultCallback<Bitmap> callback) {
		super(callback);
	}		
	
	@Override
	public void Get(final String url) {
		this.url = url;
		_httpAction = new Callable<Response>() {

			public Response call() throws Exception {
				return HttpClientUtilities.get(url);				
			}			
		};
		
		execute("");		
	}
	
	@Override
	protected Bitmap doInBackground(String... urls) {
		try {
			response = _httpAction.call();
		} catch (Exception e) {
			Log.e("Http Request", e.getLocalizedMessage());
		}

		InputStream stream = null;

		try {
			stream = response.body().byteStream();
		} catch (Exception e) {
			Log.e("Http Request", e.getMessage());
		}

		Bitmap result = null;

		if(!isCancelled()) {
			 result = BitmapFactory.decodeStream(stream);
		}

		return result;
	}

	public String getUrl() {
		return url;
	}
}
	
	


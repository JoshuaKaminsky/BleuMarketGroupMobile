package com.mobile.android.client.utilities;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

public class ImageDownloader {
	private ImageCache _cache = new ImageCache();

	public void download(String url, ImageView imageView) {
        Bitmap bitmap = null;

        try{
            bitmap = _cache.getBitmapFromCache(url);
        } catch(Exception excpetion) {
            Log.d("Image Downloader", "Error retrieving image from cache with url " + url, excpetion);
        }

        if (bitmap == null) {
            forceDownload(url, imageView);
        } else {
            cancelPotentialDownload(url, imageView);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void download(String url, View view) {
        Bitmap bitmap = null;

        try{
            bitmap = _cache.getBitmapFromCache(url);
        } catch(Exception excpetion) {
            Log.d("Image Downloader", "Error retrieving image from cache with url " + url, excpetion);
        }

        if (bitmap == null) {
            forceDownload(url, view);
        } else {
            cancelPotentialDownload(url, view);
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(new BitmapDrawable(view.getResources(), bitmap));
            } else {
                view.setBackgroundDrawable(new BitmapDrawable(view.getResources(), bitmap));
            }
        }
    }
	
	private void forceDownload(String url, ImageView imageView) {
        if (url == null) {
            imageView.setImageDrawable(null);
            return;
        }

        if (cancelPotentialDownload(url, imageView)) {
            BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
            imageView.setImageDrawable(downloadedDrawable);
            task.execute(url);
        }
    }

    private void forceDownload(String url, View view) {
        if (url == null) {
            view.setBackgroundResource(android.R.color.transparent);
            return;
        }

        if (cancelPotentialDownload(url, view)) {
            BitmapDownloaderTask task = new BitmapDownloaderTask(view);
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(downloadedDrawable);
            } else {
                view.setBackgroundDrawable(downloadedDrawable);
            }
            task.execute(url);
        }
    }
	
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean cancelPotentialDownload(String url, View view) {
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(view);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }
	
	private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    private static BitmapDownloaderTask getBitmapDownloaderTask(View view) {
        if (view != null) {
            Drawable drawable = view.getBackground();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }
	
	Bitmap downloadBitmap(String url) {
        try {
            Response response = HttpClientUtilities.get(url);
            final int statusCode = response.code();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;
            }

            final ResponseBody entity = response.body();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.byteStream();
                    return BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.close();
                }
            }
        } catch (IOException e) {
            Log.w("Image Downloader", "I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e) {
            Log.w("Image Downloader", "Incorrect URL: " + url);
        } catch (Exception e) {
            Log.w("Image Downloader", "Error while retrieving bitmap from " + url, e);
        } finally {
        }
        
        return null;
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break; 
                    } else {
                        bytesSkipped = 1;
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
    
    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private WeakReference<ImageView> imageViewReference = null;
        private WeakReference<View> viewReference = null;

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        public BitmapDownloaderTask(View view) {
            viewReference = new WeakReference<View>(view);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            return downloadBitmap(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            _cache.addBitmapToCache(url, bitmap);

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                
                if (this == bitmapDownloaderTask) {
                    imageView.setImageBitmap(bitmap);
                }
            }

            if(viewReference != null) {
                View view = viewReference.get();
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(view);

                if (this == bitmapDownloaderTask) {
                    if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        view.setBackground(new BitmapDrawable(view.getResources(), bitmap));
                    } else {
                        view.setBackgroundDrawable(new BitmapDrawable(view.getResources(), bitmap));
                    }
                }
            }
        }
    }
    
    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
        	super(Color.TRANSPARENT);
        	bitmapDownloaderTaskReference =
                new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }	
}

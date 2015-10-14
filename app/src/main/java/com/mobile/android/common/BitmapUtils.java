package com.mobile.android.common;

import java.io.File;
import java.io.FileOutputStream;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

public class BitmapUtils {

	private static final int IMAGE_MAX_SIZE = 900;

	public static Bitmap decodeResource(Resources resources, int resourceId) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(resources, resourceId, options);

		int scale = 1;
		if (options.outHeight > IMAGE_MAX_SIZE
				|| options.outWidth > IMAGE_MAX_SIZE) {
			scale = (int) Math.pow(
					2,
					(int) Math.ceil(Math.log(IMAGE_MAX_SIZE
							/ (double) Math.max(options.outHeight,
									options.outWidth))
							/ Math.log(0.5)));
		}

		BitmapFactory.Options outputOptions = new BitmapFactory.Options();
		outputOptions.inSampleSize = scale;

		return BitmapFactory.decodeResource(resources, resourceId,
				outputOptions);
	}
	
	public static Bitmap decodeResourceFull(Resources resources, int resourceId) {
		return BitmapFactory.decodeResource(resources, resourceId);
	}

	public static Bitmap decodeFile(String path, int width, int height) {
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bmOptions);

		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / width, photoH / height);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		return BitmapFactory.decodeFile(path, bmOptions);
	}

	public static Intent performCrop(Uri data, int aspectX, int aspectY,
			int outputX, int outputY) {
		try {
			Intent cropIntent = new Intent("com.android.camera.action.CROP");

			cropIntent.setDataAndType(data, "image/*");
			cropIntent.putExtra("crop", "true");
			cropIntent.putExtra("aspectX", aspectX);
			cropIntent.putExtra("aspectY", aspectY);
			cropIntent.putExtra("outputX", outputX);
			cropIntent.putExtra("outputY", outputY);
			cropIntent.putExtra("return-data", true);

			return cropIntent;
		} catch (ActivityNotFoundException anfe) {
			Log.i("PHOTO:CROP::", anfe.getLocalizedMessage());
			return new Intent();
		}
	}

	public static void saveImage(Bitmap image, String location, Context context) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(context.getFilesDir().getPath()
					.toString()
					+ location);
			image.compress(Bitmap.CompressFormat.JPEG, 100, out);
		} catch (Exception e) {
			Log.e("QC::HOME::SAVE IMAGE", e.getLocalizedMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Throwable ignore) {

			}
		}
	}

	public static Bitmap getImage(String location, Context context) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return BitmapFactory.decodeFile(context.getFilesDir().getPath()
				.toString()
				+ location, options);
	}

	public static Bitmap getClip(Bitmap bitmap) {
		return getClip(bitmap, bitmap.getWidth(), bitmap.getHeight());
	}

	public static Bitmap getClip(Bitmap bitmap, int width, int height) {
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(width / 2, height / 2, width / 2, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, canvas.getClipBounds(), paint);

		return output;
	}

	public static boolean addImageToExternalStorage(Bitmap image, String title, Context context) {
		File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		directory.mkdirs();
		
		String filePath = null;
		FileOutputStream out = null;
		try {
			File file = new File(directory, title + ".jpg");
			filePath = file.toString();
			out = new FileOutputStream(file);
			image.compress(Bitmap.CompressFormat.PNG, 100, out);
		} catch (Exception e) {
			Log.e("QC::HOME::SAVE IMAGE", e.getLocalizedMessage());
			return false;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Throwable ignore) {

			}
		}
		
		ContentValues values = new ContentValues(); 
	    values.put( Media.TITLE, title ); 
	    values.put( Images.Media.DATE_TAKEN, System.currentTimeMillis() );
	    values.put( Images.Media.BUCKET_DISPLAY_NAME, "QuitCharge" );

	    values.put( Images.Media.MIME_TYPE, "image/jpeg" );
	    values.put( MediaStore.MediaColumns.DATA, filePath );
	    Uri uri = context.getContentResolver().insert( Media.EXTERNAL_CONTENT_URI , values );

	    return true;
	}
}

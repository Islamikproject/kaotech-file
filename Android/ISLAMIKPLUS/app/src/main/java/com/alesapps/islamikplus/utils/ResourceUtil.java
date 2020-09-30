package com.alesapps.islamikplus.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResourceUtil {
	public static String RES_DIRECTORY = Environment.getExternalStorageDirectory() + "/alesapps/ISLAMIK/";
	public static String getVideoFilePath(String fileName) {
		String tempDirPath = RES_DIRECTORY;
		String tempFileName = fileName;

		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();
		File tempFile = new File(tempDirPath + tempFileName);
		if (!tempFile.exists())
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return tempDirPath + tempFileName;
	}
	public static String getAvatarFilePath() {
		return getVideoFilePath("avatar.png");
	}
	public static String getPhotoFilePath() {
		return getVideoFilePath("photo.png");
	}

	public static String generatePath(Uri uri, Context context) {
		String filePath = null;
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		if(isKitKat){
			filePath = generateFromKitkat(uri,context);
		}

		if(filePath != null){
			return filePath;
		}

		Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
				filePath = cursor.getString(columnIndex);
			}
			cursor.close();
		}
		return filePath == null ? uri.getPath() : filePath;
	}

	@TargetApi(19)
	private static String generateFromKitkat(Uri uri,Context context){
		String filePath = null;
		if(DocumentsContract.isDocumentUri(context, uri)){
			String wholeID = DocumentsContract.getDocumentId(uri);
			if (wholeID.split(":").length < 2)
				return "";
			String id = wholeID.split(":")[1];

			String[] column = { MediaStore.Video.Media.DATA };
			String sel = MediaStore.Video.Media._ID + "=?";

			Cursor cursor = context.getContentResolver().
					query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
							column, sel, new String[]{ id }, null);

			int columnIndex = cursor.getColumnIndex(column[0]);

			if (cursor.moveToFirst()) {
				filePath = cursor.getString(columnIndex);
			}

			cursor.close();
		}
		return filePath;
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		BitmapFactory.decodeFile(path, options);
		int reqHeight = reqWidth * options.outHeight / options.outWidth;
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (width <= reqWidth && height <= reqHeight)
			return 1;

		float widthRatio = (float)width / reqWidth;
		float heightRatio = (float)height / reqHeight;
		float maxRatio = Math.max(widthRatio, heightRatio);
		inSampleSize = (int)(maxRatio + 0.5);
		return inSampleSize;
	}

	public static Bitmap decodeUri(Context context, Uri selectedImage, int reqSize) throws FileNotFoundException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);
		int width_tmp = options.outWidth, height_tmp = options.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < reqSize
					|| height_tmp / 2 < reqSize) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		options = new BitmapFactory.Options();
		options.inSampleSize = scale;
		return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);
	}

	public static void saveBitmapToSdcard(Bitmap bitmap, String dirPath) {
		File tempFile = new File(dirPath);
		if (tempFile.exists())
			tempFile.delete();

		try {
			FileOutputStream fOut = new FileOutputStream(tempFile);

			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package com.alesapps.islamikplus.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.format.DateFormat;
import androidx.annotation.NonNull;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.AppGlobals;
import com.alesapps.islamikplus.listener.BooleanListener;
import com.alesapps.islamikplus.utils.ResourceUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parse.ParseFile;
import java.io.ByteArrayOutputStream;
import java.util.Date;

public class FileModel {
	public static final int PHOTO_SIZE = 512;
	public static void UploadVideo(final Uri videoURI, final BooleanListener listener) {
		String folder_name = AppConstant.STORAGE_FILE;
		AppGlobals.mStorageReference = AppGlobals.mFirebaseStorage.getReferenceFromUrl(AppConstant.URL_STORAGE_REFERENCE).child(folder_name);
		if (AppGlobals.mStorageReference != null){
			final String file_name = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
			final StorageReference fileRef = AppGlobals.mStorageReference.child(file_name);
			final UploadTask uploadTask = fileRef.putFile(videoURI);
			uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
				@Override
				public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
					if (!task.isSuccessful()) {
						if (listener != null)
							listener.done(false, "", task.getException().toString());
					}
					return fileRef.getDownloadUrl();
				}
			}).addOnCompleteListener(new OnCompleteListener<Uri>() {
				@Override
				public void onComplete(@NonNull Task<Uri> task) {
					if (task.isSuccessful()) {
						Uri downloadUri = task.getResult();
						if (listener != null)
							listener.done(true, file_name, downloadUri.toString());
					} else {
						if (listener != null)
							listener.done(false, "", task.getException().toString());
					}
				}
			});
		} else{
			if (listener != null)
				listener.done(false, "", "Google Play Services error.");
		}
	}

	public static ParseFile createParseFile(String fileName, String filePath) {
		ParseFile parseFile = null;
		Bitmap bm = ResourceUtil.decodeSampledBitmapFromFile(filePath, PHOTO_SIZE);
		parseFile = createParseFile(fileName, bm);
		return parseFile;
	}

	public static ParseFile createParseFile(String fileName, Bitmap bitmap) {
		ParseFile parseFile = null;
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] bitmapdata = stream.toByteArray();

			parseFile = new ParseFile(fileName, bitmapdata);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parseFile;
	}
}

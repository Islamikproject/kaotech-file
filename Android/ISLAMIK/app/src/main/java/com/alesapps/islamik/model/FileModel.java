package com.alesapps.islamik.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.format.DateFormat;
import androidx.annotation.NonNull;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.AppGlobals;
import com.alesapps.islamik.listener.BooleanListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parse.ParseFile;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

	public static ParseFile createParseFile(String filePath) {
		ParseFile parseFile = null;
		File audioFile = new File(filePath);
		if (audioFile != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			BufferedInputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(audioFile));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			int read;
			byte[] buff = new byte[1024];
			try {
				while ((read = in.read(buff)) > 0) {
					out.write(buff, 0, read);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] audioBytes = out.toByteArray();
			parseFile = new ParseFile(audioFile.getName() , audioBytes);
		}
		return parseFile;
	}
}

package com.alesapps.islamikplus.model;

import android.net.Uri;
import android.text.format.DateFormat;
import androidx.annotation.NonNull;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.AppGlobals;
import com.alesapps.islamikplus.listener.BooleanListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Date;

public class FileModel {
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
}

package com.alesapps.islamik;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AppGlobals {
    public static int RECITERS_INDEX = 0;
    public static String[] PRAYER_TIME = {"05:26 AM", "12:43 PM", "04:04 PM", "06:37 PM", "07:55 PM"};
    public static FirebaseStorage mFirebaseStorage;
    public static StorageReference mStorageReference;
}

package com.alesapps.islamikplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.multidex.MultiDexApplication;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.storage.FirebaseStorage;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;

public class IslamikPlusApp extends MultiDexApplication {
    public static Context mContext;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mContext = getApplicationContext();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        AppPreference.initialize(pref);

        ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("b0484b2d-2135-4a2d-a924-b916750cf909")
                .clientKey("1761390b-b5d1-4a1a-a7f4-4f4428dc9d09")
                .server("https://parseapps.brainyapps.tk:20009/parse")
                .build());
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        Places.initialize(getApplicationContext(), "AIzaSyCZSV21iRhzX8kEiHLeFYIlAOPIipw1Llg");
        PlacesClient placesClient = Places.createClient(this);
        AppGlobals.mFirebaseStorage = FirebaseStorage.getInstance();
        AppGlobals.mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(AppConstant.URL_STORAGE_REFERENCE).child(AppConstant.STORAGE_FILE);

    }

    public static Context getContext() {
        return mContext;
    }
}

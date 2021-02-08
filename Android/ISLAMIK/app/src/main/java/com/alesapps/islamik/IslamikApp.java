package com.alesapps.islamik;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.storage.FirebaseStorage;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;

public class IslamikApp extends MultiDexApplication {
    private static IslamikApp instance;
    public static Context mContext;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        AppPreference.initialize(pref);

        ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("b0484b2d-2135-4a2d-a924-b916750cf001")
                .clientKey("1761390b-b5d1-4a1a-a7f4-4f4428dc9001")
                .server("http://parse.kaotech.org:20002/parse")
                .build());
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        Places.initialize(getApplicationContext(), getString(R.string.place_api_key));
        Places.createClient(this);
        AppGlobals.mFirebaseStorage = FirebaseStorage.getInstance();
        AppGlobals.mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(AppConstant.URL_STORAGE_REFERENCE).child(AppConstant.STORAGE_FILE);
    }

    public static Context getContext() {
        return mContext;
    }

    public static IslamikApp getInstance() {
        return instance;
    }

}

package com.alesapps.islamik;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.multidex.MultiDexApplication;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;

public class IslamikApp extends MultiDexApplication {
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
                .applicationId("b0484b2d-2135-4a2d-a924-b916750cf001")
                .clientKey("1761390b-b5d1-4a1a-a7f4-4f4428dc9001")
                .server("http://parse.kaotech.org:20001/parse")
                .build());
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        Places.initialize(getApplicationContext(), "AIzaSyCZSV21iRhzX8kEiHLeFYIlAOPIipw1Llg");
        PlacesClient placesClient = Places.createClient(this);
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "413068111620");
        installation.saveInBackground();
    }

    public static Context getContext() {
        return mContext;
    }
}

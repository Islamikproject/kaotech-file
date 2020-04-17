package com.alesapps.kaotech;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KaoTechApp extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mContext = getApplicationContext();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        AppPreference.initialize(pref);
    }

    public static Context getContext() {
        return mContext;
    }
}

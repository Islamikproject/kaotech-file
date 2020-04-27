package com.alesapps.islamikplus.push;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class PushReceiver extends WakefulBroadcastReceiver {
	public static final String intentAction = "com.parse.push.intent.RECEIVE";
    @Override
	public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String message = extras != null ? extras.getString("com.parse.Data") : "";
        try {
			ComponentName comp = new ComponentName(context.getPackageName(), PushIntentService.class.getName());
			startWakefulService(context, (intent.setComponent(comp)));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
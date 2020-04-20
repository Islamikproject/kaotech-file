package com.alesapps.islamikplus.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.alesapps.islamikplus.IslamikPlusApp;
import com.alesapps.islamikplus.ui.activity.SplashActivity;

public class Starter extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Intent splashIntent = new Intent(IslamikPlusApp.getContext(), SplashActivity.class);
			splashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			IslamikPlusApp.getContext().startActivity(splashIntent);
		}
	}
}
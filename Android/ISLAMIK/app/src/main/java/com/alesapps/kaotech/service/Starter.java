package com.alesapps.kaotech.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.alesapps.kaotech.KaoTechApp;
import com.alesapps.kaotech.ui.activity.SplashActivity;

public class Starter extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Intent splashIntent = new Intent(KaoTechApp.getContext(), SplashActivity.class);
			splashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			KaoTechApp.getContext().startActivity(splashIntent);
		}
	}
}
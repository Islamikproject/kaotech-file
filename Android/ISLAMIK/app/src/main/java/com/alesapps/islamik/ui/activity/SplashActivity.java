package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.alesapps.islamik.AppPreference;
import com.alesapps.islamik.R;

public class SplashActivity extends BaseActivity {
	public static SplashActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_splash);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				gotoNextActivity();
			}
		}, 1000);
	}

	private void gotoNextActivity() {
		if (AppPreference.getBool(AppPreference.KEY.AGREE, false))
			startActivity(new Intent(instance, MainActivity.class));
		else
			startActivity(new Intent(instance, OnboardActivity.class));
		finish();
	}
}


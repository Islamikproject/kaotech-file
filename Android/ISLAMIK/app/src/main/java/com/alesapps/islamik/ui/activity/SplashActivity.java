package com.alesapps.islamik.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import com.alesapps.islamik.AppPreference;
import com.alesapps.islamik.R;

public class SplashActivity extends BaseActivity {
	public static SplashActivity instance = null;
	private static final int REQUEST_PERMISSION = 1;
	private static String[] PERMISSIONS = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE};

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
		verifyStoragePermissions(this);
	}

	public void verifyStoragePermissions(Activity activity) {
		int permission0 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permission0 != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(
					activity,
					PERMISSIONS,
					REQUEST_PERMISSION
			);
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					gotoNextActivity();
				}
			}, 1000);
		}
	}

	private void gotoNextActivity() {
		if (AppPreference.getBool(AppPreference.KEY.AGREE, false))
			startActivity(new Intent(instance, MainActivity.class));
		else
			startActivity(new Intent(instance, OnboardActivity.class));
		finish();
	}
}


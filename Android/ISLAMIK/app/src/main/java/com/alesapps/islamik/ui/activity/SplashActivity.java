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
import com.alesapps.islamik.listener.UserListener;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.utils.BaseTask;
import com.alesapps.islamik.utils.MessageUtil;
import com.parse.ParseUser;

public class SplashActivity extends BaseActivity {
	public static SplashActivity instance = null;
	private static final int REQUEST_PERMISSION = 1;
	private static String[] PERMISSIONS = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.RECORD_AUDIO,
			Manifest.permission.MODIFY_AUDIO_SETTINGS,
			Manifest.permission.CAMERA
	};

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
		int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
		int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
		int permission3 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
		int permission4 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.MODIFY_AUDIO_SETTINGS);
		int permission5 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

		if (permission0 != PackageManager.PERMISSION_GRANTED
				|| permission1 != PackageManager.PERMISSION_GRANTED
				|| permission2 != PackageManager.PERMISSION_GRANTED
				|| permission3 != PackageManager.PERMISSION_GRANTED
				|| permission4 != PackageManager.PERMISSION_GRANTED
				|| permission5 != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(
					activity,
					PERMISSIONS,
					REQUEST_PERMISSION
			);
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					boolean signedAuto = AppPreference.getBool(AppPreference.KEY.SIGN_IN_AUTO, false);
					if (!signedAuto)
						gotoNextActivity(true);
					else
						login();
				}
			}, 1000);
		}
	}


	private void login() {
		final String username = AppPreference.getStr(AppPreference.KEY.PHONE_NUMBER, null);
		final String password = AppPreference.getStr(AppPreference.KEY.PASSWORD, null);
		dlg_progress.show();
		BaseTask.run(new BaseTask.TaskListener() {
			@Override
			public Object onTaskRunning(int taskId, Object data) {
				// TODO Auto-generated method stub
				try {
					ParseUser currentUser = ParseUser.getCurrentUser();
					if (currentUser != null)
						currentUser.logOut();
				} catch (Exception ex) {}
				return null;
			}
			@Override
			public void onTaskResult(int taskId, Object result) {
				// TODO Auto-generated method stub
				UserModel.Login(username, password, new UserListener() {
					@Override
					public void done(ParseUser user, String error) {
						dlg_progress.hide();
						if (error == null) {
							ParseUser currentUser = ParseUser.getCurrentUser();
							if (currentUser.getInt(ParseConstants.KEY_TYPE) == UserModel.TYPE_USER)
								gotoNextActivity(false);
							else
								MessageUtil.showError(instance, R.string.valid_Invalid_customer);
						} else {
							MessageUtil.showToast(instance, error, true);
							gotoNextActivity(true);
						}
					}
				});
			}
			@Override
			public void onTaskProgress(int taskId, Object... values) {}
			@Override
			public void onTaskPrepare(int taskId, Object data) {}
			@Override
			public void onTaskCancelled(int taskId) {}
		});
	}

	private void gotoNextActivity(boolean isLogin) {
		if (isLogin)
			startActivity(new Intent(instance, LoginActivity.class));
		else
			startActivity(new Intent(instance, MainActivity.class));
		finish();
	}
}


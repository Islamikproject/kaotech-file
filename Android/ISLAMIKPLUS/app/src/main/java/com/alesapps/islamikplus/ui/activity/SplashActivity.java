package com.alesapps.islamikplus.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.alesapps.islamikplus.AppPreference;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.UserListener;
import com.alesapps.islamikplus.model.UserModel;
import com.alesapps.islamikplus.utils.BaseTask;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.parse.ParseUser;

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
				boolean signedAuto = AppPreference.getBool(AppPreference.KEY.SIGN_IN_AUTO, false);
				if (!signedAuto)
					gotoNextActivity(true);
				else
					login();
			}
		}, 1000);
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
							gotoNextActivity(false);
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


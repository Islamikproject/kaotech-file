package com.alesapps.islamikplus.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.AppPreference;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.model.UserModel;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.MessageUtil;

public class SettingsActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static SettingsActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.settings, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_settings);
		findViewById(R.id.layout_profile).setOnClickListener(this);
		findViewById(R.id.layout_payment).setOnClickListener(this);
		findViewById(R.id.layout_rate).setOnClickListener(this);
		findViewById(R.id.layout_feedback).setOnClickListener(this);
		findViewById(R.id.layout_about).setOnClickListener(this);
		findViewById(R.id.layout_terms).setOnClickListener(this);
		findViewById(R.id.layout_privacy).setOnClickListener(this);
		findViewById(R.id.layout_logout).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.layout_profile:
				startActivity(new Intent(instance, EditProfileActivity.class));
				return;
			case R.id.layout_payment:
				PaymentActivity.isSignUp = false;
				startActivity(new Intent(instance, PaymentActivity.class));
				return;
			case R.id.layout_rate:
				CommonUtil.launchMarket();
				return;
			case R.id.layout_feedback:
				CommonUtil.SendEmail(instance, AppConstant.ADMIN_EMAIL, getString(R.string.send_feedback),	"", "");
				return;
			case R.id.layout_about:
				startActivity(new Intent(instance, AboutActivity.class));
				return;
			case R.id.layout_privacy:
				startActivity(new Intent(instance, PrivacyPolicyActivity.class));
				return;
			case R.id.layout_terms:
				startActivity(new Intent(instance, TermsConditionActivity.class));
				return;
			case R.id.layout_logout:
				logout();
				return;
		}
	}

	private void logout() {
		new AlertDialog.Builder(instance)
				.setTitle(R.string.logout)
				.setMessage(R.string.label_log_out)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dlg_progress.show();
						UserModel.Logout(new ExceptionListener() {
							@Override
							public void done(String error) {
								// TODO Auto-generated method stub
								dlg_progress.cancel();
								if (error == null) {
									AppPreference.setBool(AppPreference.KEY.SIGN_IN_AUTO, false);
									AppPreference.setStr(AppPreference.KEY.PHONE_NUMBER, "");
									AppPreference.setStr(AppPreference.KEY.PASSWORD, "");
									startActivity(new Intent(instance, LoginActivity.class));
									if (MainActivity.instance != null)
										MainActivity.instance.finish();
									finish();
								} else {
									MessageUtil.showToast(instance, error, true);
								}
							}
						});
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
	}
}


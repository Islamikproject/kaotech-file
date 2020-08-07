package com.alesapps.islamikplus.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.alesapps.islamikplus.AppPreference;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.UserListener;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.UserModel;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.parse.ParseUser;

public class LoginActivity extends BaseActionBarActivity implements OnClickListener {
	public static LoginActivity instance = null;
	// UI
	EditText edt_phone_number;
	EditText edt_password;
	TextView txt_forgot_password;
	TextView txt_signup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.login, 0);
		setContentView(R.layout.activity_login);
		edt_phone_number = findViewById(R.id.edt_phone);
		edt_password = findViewById(R.id.edt_password);
		txt_forgot_password = findViewById(R.id.txt_forgot_password);
		txt_signup = findViewById(R.id.txt_signup);
		findViewById(R.id.txt_forgot_password).setOnClickListener(this);
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.txt_signup).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		txt_forgot_password.setText(Html.fromHtml(getString(R.string.forgot_password)));
		txt_signup.setText(Html.fromHtml(getString(R.string.no_account_signup_here)));
		edt_phone_number.setText(AppPreference.getStr(AppPreference.KEY.PHONE_NUMBER, ""));
		edt_password.setText(AppPreference.getStr(AppPreference.KEY.PASSWORD, ""));
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub]
		CommonUtil.hideKeyboard(instance, edt_phone_number);
		switch (view.getId()) {
			case R.id.txt_signup:
				startActivity(new Intent(instance, SignUpActivity.class));
				break;
			case R.id.txt_forgot_password:
				startActivity(new Intent(instance, ResetPasswordActivity.class));
				break;
			case R.id.btn_login:
				if (isValid())
					login();
				break;
		}
	}

	private boolean isValid() {
		String phone_number = edt_phone_number.getText().toString().trim();
		String password = edt_password.getText().toString().trim();
		if (TextUtils.isEmpty(phone_number) && TextUtils.isEmpty(password)) {
			MessageUtil.showError(instance, R.string.valid_No_phone_number_password);
			edt_phone_number.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(phone_number)) {
			MessageUtil.showError(instance, R.string.valid_No_phone_number);
			edt_phone_number.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			MessageUtil.showError(instance, R.string.valid_No_password);
			edt_password.requestFocus();
			return false;
		}
		return true;
	}

	public void login() {
		final String phone = edt_phone_number.getText().toString().trim();
		String password = edt_password.getText().toString().trim();
		dlg_progress.show();
		UserModel.Login(phone, password, new UserListener() {
			@Override
			public void done(ParseUser userObj, String error) {
				// TODO Auto-generated method stub
				ParseUser currentUser = ParseUser.getCurrentUser();
				if (error == null) {
					dlg_progress.cancel();
					if (currentUser.getInt(ParseConstants.KEY_TYPE) == UserModel.TYPE_MOSQUE)
						gotoNextActivity();
					else
						MessageUtil.showError(instance, R.string.valid_Invalid_mosque);
				} else {
					if (currentUser != null)
						currentUser.logOut();
					UserModel.GetUserFromPhone(phone, new UserListener() {
						@Override
						public void done(ParseUser user, String error) {
							dlg_progress.cancel();
							if (error == null) {
								MessageUtil.showError(instance, R.string.invalid_incorrect_password);
							} else {
								MessageUtil.showError(instance, R.string.valid_No_phone_number_register);
							}
						}
					});
				}
			}
		});
	}

	private void gotoNextActivity() {
		AppPreference.setBool(AppPreference.KEY.SIGN_IN_AUTO, true);
		AppPreference.setStr(AppPreference.KEY.PHONE_NUMBER, edt_phone_number.getText().toString().trim());
		AppPreference.setStr(AppPreference.KEY.PASSWORD, edt_password.getText().toString().trim());
		startActivity(new Intent(instance, MainActivity.class));
		finish();
	}
}

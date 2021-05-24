package com.alesapps.islamik.ui.activity;

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
import com.alesapps.islamik.AppPreference;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.UserListener;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.utils.CommonUtil;
import com.alesapps.islamik.utils.MessageUtil;
import com.parse.ParseUser;

public class LoginActivity extends BaseActionBarActivity implements OnClickListener {
	public static LoginActivity instance = null;
	// UI
	EditText edt_phone_number;
	EditText edt_password;
	TextView txt_forgot_password;
	TextView txt_terms;
	TextView txt_privacy;
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
		txt_terms = findViewById(R.id.txt_terms);
		txt_privacy = findViewById(R.id.txt_privacy);
		txt_signup = findViewById(R.id.txt_signup);
		findViewById(R.id.txt_forgot_password).setOnClickListener(this);
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.txt_terms).setOnClickListener(this);
		findViewById(R.id.txt_privacy).setOnClickListener(this);
		findViewById(R.id.txt_signup).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		txt_forgot_password.setText(Html.fromHtml(getString(R.string.forgot_password)));
		txt_signup.setText(Html.fromHtml(getString(R.string.no_account_signup_here)));
		txt_terms.setText(Html.fromHtml(getString(R.string.terms_conditions_)));
		txt_privacy.setText(Html.fromHtml(getString(R.string.privacy_policy_)));
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
			case R.id.txt_terms:
				startActivity(new Intent(instance, TermsConditionActivity.class));
				break;
			case R.id.txt_privacy:
				startActivity(new Intent(instance, PrivacyPolicyActivity.class));
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
					if (currentUser.getInt(ParseConstants.KEY_TYPE) == UserModel.TYPE_USER)
						gotoNextActivity();
					else
						MessageUtil.showError(instance, R.string.valid_Invalid_customer);
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
								showConfirmRegisterDialog();
							}
						}
					});
				}
			}
		});
	}

	private void showConfirmRegisterDialog() {
		new AlertDialog.Builder(instance)
				.setTitle(R.string.confirm)
				.setMessage(R.string.valid_No_phone_number_register)
				.setPositiveButton(R.string.sign_up, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(instance, SignUpActivity.class));
					}
				})
				.setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						edt_phone_number.requestFocus();
					}
				})
				.show();
	}

	private void gotoNextActivity() {
		AppPreference.setBool(AppPreference.KEY.SIGN_IN_AUTO, true);
		AppPreference.setStr(AppPreference.KEY.PHONE_NUMBER, edt_phone_number.getText().toString().trim());
		AppPreference.setStr(AppPreference.KEY.PASSWORD, edt_password.getText().toString().trim());
		startActivity(new Intent(instance, MainActivity.class));
		finish();
	}
}

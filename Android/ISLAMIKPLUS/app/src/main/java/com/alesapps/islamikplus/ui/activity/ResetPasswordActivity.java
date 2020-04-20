package com.alesapps.islamikplus.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.listener.UserListener;
import com.alesapps.islamikplus.model.UserModel;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.parse.ParseUser;

public class ResetPasswordActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static ResetPasswordActivity instance = null;
	EditText edt_email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.reset_password, 0);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_reset_password);
		edt_email = findViewById(R.id.edt_email);
		findViewById(R.id.btn_reset_password).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		CommonUtil.hideKeyboard(instance, edt_email);
		super.onClick(view);
		switch (view.getId()) {
			case R.id.btn_reset_password:
				 if (isValid())
					doForgotPwd();
			break;
		}
	}

	private boolean isValid() {
		String strEmail = edt_email.getText().toString().trim();
		if (TextUtils.isEmpty(strEmail)) {
			MessageUtil.showError(instance, R.string.valid_No_email_address);
			edt_email.requestFocus();
			return false;
		}
		if (!CommonUtil.isValidEmail(strEmail)) {
			MessageUtil.showError(instance, R.string.valid_Invalid_email);
			edt_email.requestFocus();
			return false;
		}
		return true;
	}

	private void doForgotPwd() {
		final String strEmail = edt_email.getText().toString().trim();
		dlg_progress.show();
		UserModel.GetUserFromEmail(strEmail, new UserListener() {
			@Override
			public void done(ParseUser user, String error) {
				if (error == null) {
					UserModel.RequestPasswordReset(strEmail, new ExceptionListener() {
						@Override
						public void done(String result) {
							dlg_progress.hide();
							if (result == null) {
								new AlertDialog.Builder(instance)
										.setTitle(R.string.success)
										.setMessage(R.string.success_forgot_password)
										.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												myBack();
											}
										}).show();
							} else {
								MessageUtil.showError(instance, R.string.valid_Invalid_email);
							}
						}
					});
				} else {
					dlg_progress.hide();
					showConfirmRegisterDialog();
				}
			}
		});
	}

	private void showConfirmRegisterDialog() {
		new AlertDialog.Builder(instance)
				.setTitle(R.string.confirm)
				.setMessage(R.string.valid_No_email_register)
				.setPositiveButton(R.string.sign_up, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(instance, SignUpActivity.class));
					}
				})
				.setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						edt_email.requestFocus();
					}
				})
				.show();
	}
}

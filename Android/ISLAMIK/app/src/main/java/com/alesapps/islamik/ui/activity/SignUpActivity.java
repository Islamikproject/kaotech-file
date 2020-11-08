package com.alesapps.islamik.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.AppPreference;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.UserListener;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.utils.CommonUtil;
import com.alesapps.islamik.utils.MessageUtil;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends BaseActionBarActivity implements OnClickListener {
	public static SignUpActivity instance = null;

	EditText edt_first_name;
	EditText edt_surname;
	TextView txt_address;
	EditText edt_phone_number;
	EditText edt_email;
	EditText edt_password;
	EditText edt_confirm_password;

	ParseGeoPoint mLatLng = new ParseGeoPoint(37.398160, -122.180831);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.sign_up, 0);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_signup);
		edt_first_name = findViewById(R.id.edt_first_name);
		edt_surname = findViewById(R.id.edt_surname);
		txt_address = findViewById(R.id.txt_address);
		edt_phone_number = findViewById(R.id.edt_phone);
		edt_email = findViewById(R.id.edt_email);
		edt_password = findViewById(R.id.edt_password);
		edt_confirm_password = findViewById(R.id.edt_confirm_password);
		findViewById(R.id.txt_address).setOnClickListener(this);
		findViewById(R.id.btn_signup).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub]
		CommonUtil.hideKeyboard(instance, edt_first_name);
		super.onClick(view);
		switch (view.getId()) {
			case R.id.txt_address:
				List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
				Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(instance);
				startActivityForResult(intent, AppConstant.AUTOCOMPLETE_REQUEST_CODE);
				break;
			case R.id.btn_signup:
				if (isValid())
					signup();
				break;
		}
	}

	private boolean isValid() {
		String first_name = edt_first_name.getText().toString().trim();
		String surname = edt_surname.getText().toString().trim();
		String address = txt_address.getText().toString().trim();
		String phone_number = edt_phone_number.getText().toString().trim();
		String email = edt_email.getText().toString().trim();
		String password = edt_password.getText().toString();
		String confirm_password = edt_confirm_password.getText().toString();

		if (TextUtils.isEmpty(first_name)) {
			MessageUtil.showError(instance, R.string.valid_No_first_name);
			edt_first_name.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(surname)) {
			MessageUtil.showError(instance, R.string.valid_No_surname);
			edt_surname.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(address)) {
			MessageUtil.showError(instance, R.string.valid_No_address);
			return false;
		}
		if (TextUtils.isEmpty(phone_number)) {
			MessageUtil.showError(instance, R.string.valid_No_phone_number);
			edt_phone_number.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(email)) {
			MessageUtil.showError(instance, R.string.valid_No_email_address);
			edt_email.requestFocus();
			return false;
		}
		if (!CommonUtil.isValidEmail(email)) {
			MessageUtil.showError(instance, R.string.valid_Invalid_email);
			edt_email.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			MessageUtil.showError(instance, R.string.valid_No_password);
			edt_password.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(confirm_password)) {
			MessageUtil.showError(instance, R.string.valid_No_confirm_password);
			edt_confirm_password.requestFocus();
			return false;
		}
		if (!password.equals(confirm_password)) {
			MessageUtil.showError(instance, R.string.valid_Invalid_password);
			edt_confirm_password.requestFocus();
			return false;
		}
		return true;
	}

	@SuppressLint("MissingSuperCall")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AppConstant.AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
			Place place = Autocomplete.getPlaceFromIntent(data);
			mLatLng = new ParseGeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
			txt_address.setText(place.getAddress());
		}
	}

	private void signup() {
		String first_name = edt_first_name.getText().toString().trim();
		String surname = edt_surname.getText().toString().trim();
		String address = txt_address.getText().toString().trim();
		String phone_number = edt_phone_number.getText().toString().trim();
		String email = edt_email.getText().toString().trim();
		String password = edt_password.getText().toString();

		UserModel model = new UserModel();
		model.username = phone_number;
		model.email = email;
		model.password = password;
		model.emailAddress = email;
		model.phoneNumber =phone_number;
		model.firstName = first_name;
		model.lastName = surname;
		model.lonLat = mLatLng;
		model.address = address;

		dlg_progress.show();
		UserModel.Register(model, new UserListener() {
			@Override
			public void done(ParseUser user, String error) {
				dlg_progress.cancel();
				if (error == null && user != null) {
					MessageUtil.showToast(instance, R.string.success);
					gotoNextActivity();
				} else {
					MessageUtil.showToast(instance, error);
				}
			}
		});
	}

	private void gotoNextActivity() {
		AppPreference.setBool(AppPreference.KEY.SIGN_IN_AUTO, true);
		AppPreference.setStr(AppPreference.KEY.PHONE_NUMBER, edt_phone_number.getText().toString().trim());
		AppPreference.setStr(AppPreference.KEY.PASSWORD, edt_password.getText().toString().trim());
		if (ResetPasswordActivity.instance != null)
			ResetPasswordActivity.instance.finish();
		myBack();
	}
}

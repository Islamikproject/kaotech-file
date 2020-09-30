package com.alesapps.islamikplus.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.AppPreference;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.model.FileModel;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.ParseErrorHandler;
import com.alesapps.islamikplus.model.UserModel;
import com.alesapps.islamikplus.utils.BaseTask;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.alesapps.islamikplus.utils.ResourceUtil;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class EditProfileActivity extends BaseActionBarActivity implements OnClickListener {
	public static EditProfileActivity instance = null;

	ImageView img_avatar;
	EditText edt_first_name;
	EditText edt_surname;
	EditText edt_mosque;
	TextView txt_address;
	EditText edt_phone_number;
	EditText edt_email;
	EditText edt_password;
	EditText edt_confirm_password;

	ParseGeoPoint mLatLng = new ParseGeoPoint(37.398160, -122.180831);
	final int PICTURE_PICK = 1000;
	final int CAMERA_CAPTURE = 1001;
	boolean isPhotoAdded = false;
	Bitmap mOrgBmp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.edit_profile, 0);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_edit_profile);
		img_avatar = findViewById(R.id.img_avatar);
		edt_first_name = findViewById(R.id.edt_first_name);
		edt_surname = findViewById(R.id.edt_surname);
		edt_mosque = findViewById(R.id.edt_mosque);
		txt_address = findViewById(R.id.txt_address);
		edt_phone_number = findViewById(R.id.edt_phone);
		edt_email = findViewById(R.id.edt_email);
		edt_password = findViewById(R.id.edt_password);
		edt_confirm_password = findViewById(R.id.edt_confirm_password);
		findViewById(R.id.img_avatar).setOnClickListener(this);
		findViewById(R.id.txt_address).setOnClickListener(this);
		findViewById(R.id.btn_save).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		UserModel model = new UserModel();
		model.parse(ParseUser.getCurrentUser());
		edt_first_name.setText(model.firstName);
		edt_surname.setText(model.lastName);
		edt_mosque.setText(model.mosque);
		txt_address.setText(model.address);
		edt_phone_number.setText(model.phoneNumber);
		edt_email.setText(model.emailAddress);
		edt_password.setText(AppPreference.getStr(AppPreference.KEY.PASSWORD, ""));
		edt_confirm_password.setText(AppPreference.getStr(AppPreference.KEY.PASSWORD, ""));
		mLatLng = model.lonLat;
		if (model.avatar != null)
			Picasso.get().load(CommonUtil.getImagePath(model.avatar.getUrl())).into(img_avatar);
		else
			img_avatar.setBackgroundResource(R.drawable.default_profile);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub]
		CommonUtil.hideKeyboard(instance, edt_first_name);
		super.onClick(view);
		switch (view.getId()) {
			case R.id.img_avatar:
				new AlertDialog.Builder(instance)
						.setTitle(R.string.upload_photo)
						.setPositiveButton(R.string.select_gallery, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								chooseTakePhoto(false);
							}
						})
						.setNegativeButton(R.string.take_new_photo, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								chooseTakePhoto(true);
							}
						})
						.show();
				break;
			case R.id.txt_address:
				List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
				Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(instance);
				startActivityForResult(intent, AppConstant.AUTOCOMPLETE_REQUEST_CODE);
				break;
			case R.id.btn_save:
				if (isValid())
					save();
				break;
		}
	}

	private boolean isValid() {
		String first_name = edt_first_name.getText().toString().trim();
		String surname = edt_surname.getText().toString().trim();
		String mosque = edt_mosque.getText().toString().trim();
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
		if (TextUtils.isEmpty(mosque)) {
			MessageUtil.showError(instance, R.string.valid_No_name_mosque);
			edt_mosque.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(address)) {
			MessageUtil.showError(instance, R.string.valid_No_address_mosque);
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

	private void chooseTakePhoto(boolean isTake) {
		if (!isTake) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, PICTURE_PICK);
		} else {
			try {
				Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(ResourceUtil.getAvatarFilePath());
				if (file.exists())
					file.delete();
				Uri photoURI = FileProvider.getUriForFile(instance, getApplicationContext().getPackageName() + ".provider", file);
				captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
				startActivityForResult(captureIntent, CAMERA_CAPTURE);
			} catch (ActivityNotFoundException anfe) {
				String errorMessage = "Whoops - your device doesn't support capturing images!";
				Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

	@SuppressLint("MissingSuperCall")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AppConstant.AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
			Place place = Autocomplete.getPlaceFromIntent(data);
			mLatLng = new ParseGeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
			txt_address.setText(place.getAddress());
		}
		if (requestCode == PICTURE_PICK && resultCode == Activity.RESULT_OK) {
			Uri imageUri = CropImage.getPickImageResultUri(this, data);
			startCropImageActivity(imageUri);
		}

		if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
			File file = new File(ResourceUtil.getAvatarFilePath());
			Uri photoURI = FileProvider.getUriForFile(instance, getApplicationContext().getPackageName() + ".provider", file);
			startCropImageActivity(photoURI);
		}

		if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			CropImage.ActivityResult result = CropImage.getActivityResult(data);
			if (resultCode == RESULT_OK) {
				String strFileName = ResourceUtil.getAvatarFilePath();
				try {
					Bitmap bm = ResourceUtil.decodeUri(instance, result.getUri(), UserModel.AVATAR_SIZE);
					if (bm != null) {
						ResourceUtil.saveBitmapToSdcard(bm, strFileName);
						if (mOrgBmp != null)
							mOrgBmp.recycle();
						mOrgBmp = bm;
						img_avatar.setImageDrawable(new BitmapDrawable(mOrgBmp));
						isPhotoAdded = true;
					} else {
						Log.i(getString(R.string.app_name), "Bitmap is null");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
				MessageUtil.showError(instance, "Cropping failed: " + result.getError());
			}
		}
	}

	private void startCropImageActivity(Uri imageUri) {
		CropImage.activity(imageUri)
				.setGuidelines(CropImageView.Guidelines.ON)
				.setMultiTouchEnabled(true)
				.setAspectRatio(1, 1)
				.start(this);
	}

	private void save() {
		final String first_name = edt_first_name.getText().toString().trim();
		final String surname = edt_surname.getText().toString().trim();
		final String mosque = edt_mosque.getText().toString().trim();
		final String address = txt_address.getText().toString().trim();
		final String phone_number = edt_phone_number.getText().toString().trim();
		final String email = edt_email.getText().toString().trim();
		final String password = edt_password.getText().toString();
		final UserModel model = new UserModel();
		if (isPhotoAdded)
			model.avatar = FileModel.createParseFile("avatar.png", ResourceUtil.getAvatarFilePath());
		dlg_progress.show();
		BaseTask.run(new BaseTask.TaskListener() {

			@Override
			public Object onTaskRunning(int taskId, Object data) {
				// TODO Auto-generated method stub
				try {
					if (isPhotoAdded)
						model.avatar.save();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void onTaskResult(int taskId, Object result) {
				ParseUser currentUser = ParseUser.getCurrentUser();
				currentUser.setUsername(phone_number);
				currentUser.setEmail(email);
				currentUser.setPassword(password);
				currentUser.put(ParseConstants.KEY_EMAIL_ADDRESS, email);
				currentUser.put(ParseConstants.KEY_FIRST_NAME, first_name);
				currentUser.put(ParseConstants.KEY_LAST_NAME, surname);
				currentUser.put(ParseConstants.KEY_PHONE_NUMBER, phone_number);
				currentUser.put(ParseConstants.KEY_MOSQUE, mosque);
				currentUser.put(ParseConstants.KEY_LON_LAT, mLatLng);
				currentUser.put(ParseConstants.KEY_ADDRESS, address);
				if (model.avatar != null)
					currentUser.put(ParseConstants.KEY_AVATAR, model.avatar);
				currentUser.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						dlg_progress.cancel();
						if (e == null) {
							MessageUtil.showToast(instance, R.string.success);
							AppPreference.setStr(AppPreference.KEY.PHONE_NUMBER, phone_number);
							AppPreference.setStr(AppPreference.KEY.PASSWORD, password);
							myBack();
						} else {
							MessageUtil.showToast(instance, ParseErrorHandler.handle(e));
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
}

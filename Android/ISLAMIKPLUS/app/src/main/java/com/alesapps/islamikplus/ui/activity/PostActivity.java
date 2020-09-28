package com.alesapps.islamikplus.ui.activity;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.listener.ObjectListener;
import com.alesapps.islamikplus.model.FileModel;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.ParseErrorHandler;
import com.alesapps.islamikplus.model.PostModel;
import com.alesapps.islamikplus.utils.BaseTask;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.alesapps.islamikplus.utils.ResourceUtil;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;

public class PostActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static PostActivity instance = null;
	ImageView img_photo;
	EditText edt_title;
	EditText edt_description;
	Button btn_create;
	Button btn_save;

	public static ParseObject mPostObj;
	final int PICTURE_PICK = 1000;
	final int CAMERA_CAPTURE = 1001;
	boolean isPhotoAdded = false;
	Bitmap mOrgBmp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.create_post, -1);
		ShowActionBarIcons(true, R.id.action_back, R.id.action_delete);
		setContentView(R.layout.activity_post);
		img_photo = findViewById(R.id.img_photo);
		edt_title = findViewById(R.id.edt_title);
		edt_description = findViewById(R.id.edt_description);
		btn_create = findViewById(R.id.btn_create);
		btn_save = findViewById(R.id.btn_save);

		findViewById(R.id.img_photo).setOnClickListener(this);
		findViewById(R.id.btn_create).setOnClickListener(this);
		findViewById(R.id.btn_save).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		btn_create.setVisibility(View.GONE);
		btn_save.setVisibility(View.GONE);
		if (mPostObj == null) {
			SetTitle(R.string.create_post, -1);
			ShowActionBarIcons(true, R.id.action_back);
			btn_create.setVisibility(View.VISIBLE);
		} else {
			SetTitle(R.string.edit_post, -1);
			ShowActionBarIcons(true, R.id.action_back, R.id.action_delete);
			btn_save.setVisibility(View.VISIBLE);
			edt_title.setText(mPostObj.getString(ParseConstants.KEY_TITLE));
			edt_description.setText(mPostObj.getString(ParseConstants.KEY_DESCRIPTION));
			ParseFile photo = mPostObj.getParseFile(ParseConstants.KEY_PHOTO);
			if (photo != null)
				Picasso.get().load(CommonUtil.getImagePath(photo.getUrl())).into(img_photo);
		}
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.img_photo:
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
						}).show();
				break;
			case R.id.btn_create:
				if (isValid())
					register();
				return;
			case R.id.btn_save:
				if (isValid())
					update();
				break;
			case R.id.action_delete:
				new AlertDialog.Builder(instance)
						.setTitle(R.string.delete)
						.setMessage(R.string.confirm_delete_post)
						.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								delete();
							}
						})
						.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {}
						}).show();
				break;
		}
	}

	private boolean isValid() {
		String title = edt_title.getText().toString().trim();
		String description = edt_description.getText().toString().trim();

		if (TextUtils.isEmpty(title)) {
			MessageUtil.showError(instance, R.string.valid_No_title);
			edt_title.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(description)) {
			MessageUtil.showError(instance, R.string.valid_No_description);
			edt_description.requestFocus();
			return false;
		}
		if (mPostObj == null && !isPhotoAdded) {
			MessageUtil.showError(instance, R.string.valid_No_photo);
			return false;
		}
		return true;
	}

	private void register() {
		PostModel model = new PostModel();
		model.owner = ParseUser.getCurrentUser();
		model.title = edt_title.getText().toString().trim();
		model.description = edt_description.getText().toString().trim();
		if (isPhotoAdded)
			model.photo = FileModel.createParseFile("photo.png", ResourceUtil.getPhotoFilePath());
		dlg_progress.show();
		PostModel.Register(model, new ObjectListener() {
			@Override
			public void done(ParseObject object, String error) {
				dlg_progress.cancel();
				if (error == null) {
					MessageUtil.showToast(instance, R.string.success);
					if (PostListActivity.instance != null)
						PostListActivity.instance.list_post.refresh();
					myBack();
				} else {
					MessageUtil.showToast(instance, error);
				}
			}
		});
	}

	private void update() {
		final PostModel model = new PostModel();
		model.parse(mPostObj);
		model.title = edt_title.getText().toString().trim();
		model.description = edt_description.getText().toString().trim();
		if (isPhotoAdded)
			model.photo = FileModel.createParseFile("photo.png", ResourceUtil.getPhotoFilePath());
		dlg_progress.show();
		BaseTask.run(new BaseTask.TaskListener() {

			@Override
			public Object onTaskRunning(int taskId, Object data) {
				// TODO Auto-generated method stub
				try {
					if (isPhotoAdded)
						model.photo.save();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void onTaskResult(int taskId, Object result) {
				mPostObj.put(ParseConstants.KEY_OWNER, ParseUser.getCurrentUser());
				mPostObj.put(ParseConstants.KEY_TITLE, model.title);
				mPostObj.put(ParseConstants.KEY_DESCRIPTION, model.description);
				if (isPhotoAdded) {
					mPostObj.put(ParseConstants.KEY_PHOTO, model.photo);
				}
				mPostObj.saveInBackground(new SaveCallback() {
					public void done(ParseException e) {
						dlg_progress.cancel();
						if (e == null) {
							MessageUtil.showToast(instance, R.string.success);
							if (PostListActivity.instance != null)
								PostListActivity.instance.list_post.refresh();
							myBack();
						} else {
							MessageUtil.showToast(instance, ParseErrorHandler.handle(e));
						}
					}
				});
			}
			public void onTaskProgress(int taskId, Object... values) {}
			public void onTaskPrepare(int taskId, Object data) {}
			public void onTaskCancelled(int taskId) {}
		});
	}

	private void delete() {
		dlg_progress.show();
		PostModel.Delete(mPostObj, new ExceptionListener() {
			@Override
			public void done(String error) {
				dlg_progress.cancel();
				if (error == null) {
					MessageUtil.showToast(instance, R.string.success);
					if (PostListActivity.instance != null)
						PostListActivity.instance.list_post.refresh();
					myBack();
				} else {
					MessageUtil.showToast(instance, error);
				}
			}
		});
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
				File file = new File(ResourceUtil.getPhotoFilePath());
				Uri photoURI = FileProvider.getUriForFile(instance, getApplicationContext().getPackageName() + ".provider", file);
				captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
				startActivityForResult(captureIntent, CAMERA_CAPTURE);
			} catch (ActivityNotFoundException anfe) {
				String errorMessage = "Whoops - your device doesn't support capturing images!";
				Toast.makeText(instance, errorMessage, Toast.LENGTH_SHORT).show();
			}
		}
	}


	@SuppressLint("MissingSuperCall")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICTURE_PICK && resultCode == RESULT_OK) {
			Uri imageUri = CropImage.getPickImageResultUri(instance, data);
			startCropImageActivity(imageUri);
		}

		if (requestCode == CAMERA_CAPTURE && resultCode == RESULT_OK) {
			File file = new File(ResourceUtil.getPhotoFilePath());
			Uri photoURI = FileProvider.getUriForFile(instance, getApplicationContext().getPackageName() + ".provider", file);
			startCropImageActivity(photoURI);
		}

		if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			CropImage.ActivityResult result = CropImage.getActivityResult(data);
			if (resultCode == RESULT_OK) {
				String strFileName = ResourceUtil.getPhotoFilePath();
				try {
					Bitmap bm = ResourceUtil.decodeUri(instance, result.getUri(), FileModel.PHOTO_SIZE);
					if (bm != null) {
						ResourceUtil.saveBitmapToSdcard(bm, strFileName);
						if (mOrgBmp != null)
							mOrgBmp.recycle();
						mOrgBmp = bm;
						img_photo.setImageDrawable(new BitmapDrawable(mOrgBmp));
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
				.setAspectRatio(4, 3)
				.start(instance);
	}
}


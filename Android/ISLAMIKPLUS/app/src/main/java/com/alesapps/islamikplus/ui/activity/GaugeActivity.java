package com.alesapps.islamikplus.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.BooleanListener;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.model.FileModel;
import com.alesapps.islamikplus.model.GaugeModel;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.ParseErrorHandler;
import com.alesapps.islamikplus.utils.BaseTask;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.alesapps.islamikplus.utils.ResourceUtil;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;

public class GaugeActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static GaugeActivity instance = null;
	EditText edt_text;
	EditText edt_weblink;
	Spinner sp_text;
	Spinner sp_size;
	Spinner sp_font;
	Spinner sp_background;
	ImageView img_photo;
	VideoView video_view;
	Button btn_submit;
	final int VIDEO_PICK = 2000;
	final int PICTURE_PICK = 1000;
	final int CAMERA_CAPTURE = 1001;
	boolean isAddedPhoto = false;
	boolean isAddedVideo = false;
	String localVideoPath = "";
	public static ParseObject mGaugeObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.islamophobia_gauge, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_gauge);
		edt_text = findViewById(R.id.edt_text);
		edt_weblink = findViewById(R.id.edt_weblink);
		sp_text = findViewById(R.id.sp_text);
		sp_size = findViewById(R.id.sp_size);
		sp_font = findViewById(R.id.sp_font);
		sp_background = findViewById(R.id.sp_background);
		img_photo = findViewById(R.id.img_photo);
		video_view = findViewById(R.id.video_view);
		btn_submit = findViewById(R.id.btn_submit);
		sp_text.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				edt_text.setTextColor(getResources().getColor(AppConstant.ARRAY_COLOR[position]));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		sp_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				edt_text.setTextSize(Float.valueOf(AppConstant.ARRAY_STRING_SIZE[position]));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		sp_font.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Typeface face = ResourcesCompat.getFont(instance, AppConstant.ARRAY_FONT_VALUE[position]);
				edt_text.setTypeface(face);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		sp_background.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				edt_text.setBackgroundColor(getResources().getColor(AppConstant.ARRAY_COLOR[position]));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});

		findViewById(R.id.layout_photo).setOnClickListener(this);
		findViewById(R.id.layout_video).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		isAddedPhoto = false;
		isAddedVideo = false;
		ShowActionBarIcons(true, R.id.action_back);
		btn_submit.setText(getString(R.string.submit));
		edt_text.setText("");
		edt_weblink.setText("");
		ArrayAdapter<String> adapterColor = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.ARRAY_STRING_COLOR);
		sp_text.setAdapter(adapterColor);
		sp_background.setAdapter(adapterColor);
		sp_text.setSelection(2);
		sp_background.setSelection(1);
		ArrayAdapter<String> adapterSize = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.ARRAY_STRING_SIZE);
		sp_size.setAdapter(adapterSize);
		sp_size.setSelection(0);
		ArrayAdapter<String> adapterFont = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.ARRAY_FONT);
		sp_font.setAdapter(adapterFont);
		sp_font.setSelection(0);

		if (mGaugeObj != null) {
			ShowActionBarIcons(true, R.id.action_back, R.id.action_delete);
			btn_submit.setText(getString(R.string.save_change));
			GaugeModel model = new GaugeModel();
			model.parse(mGaugeObj);
			edt_text.setText(model.description);
			edt_weblink.setText(model.webLink);
			sp_text.setSelection(model.textColor);
			sp_background.setSelection(model.bgColor);
			sp_size.setSelection(model.textSize);
			sp_font.setSelection(model.textFont);
			if (model.photo != null)
				Picasso.get().load(CommonUtil.getImagePath(model.photo.getUrl())).into(img_photo);
			if (!TextUtils.isEmpty(model.video)) {
				video_view.setVideoPath(model.video);
				video_view.setMediaController(new MediaController(instance));
				video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mediaPlayer) {
						video_view.start();
					}
				});
			}
		}
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.layout_photo:
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
			case R.id.layout_video:
				showUploadDialog();
				break;
			case R.id.btn_submit:
				if (isValid())
					uploadVideo();
				return;
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
		String text = edt_text.getText().toString().trim();
		String weblink = edt_weblink.getText().toString().trim();
		if (TextUtils.isEmpty(text) && TextUtils.isEmpty(weblink) && !isAddedPhoto && !isAddedVideo) {
			MessageUtil.showError(instance, R.string.valid_No_gauge);
			return false;
		}
		return true;
	}

	private void showUploadDialog() {
		new AlertDialog.Builder(instance)
				.setTitle(R.string.upload_video_audio)
				.setPositiveButton(R.string.take_new_video, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(instance, CameraActivity.class));
					}
				})
				.setNegativeButton(R.string.select_video_gallery, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("video/*");
						intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"video/*"});
						startActivityForResult(intent, VIDEO_PICK);
					}
				})
				.show();
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
						img_photo.setImageDrawable(new BitmapDrawable(bm));
						isAddedPhoto = true;
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

		if (requestCode == VIDEO_PICK && resultCode == Activity.RESULT_OK) {
			Uri selectedUri = data.getData();
			String[] columns = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media.MIME_TYPE };

			Cursor cursor = getContentResolver().query(selectedUri, columns, null, null, null);
			cursor.moveToFirst();

			int pathColumnIndex     = cursor.getColumnIndex( columns[0] );
			int mimeTypeColumnIndex = cursor.getColumnIndex( columns[1] );

			String contentPath = cursor.getString(pathColumnIndex);
			String mimeType = cursor.getString(mimeTypeColumnIndex);
			cursor.close();

			if(mimeType.startsWith("video")) {
				Uri selectedImageUri = data.getData();
				isAddedVideo = true;
				localVideoPath = ResourceUtil.generatePath(selectedImageUri, this);
			}
		}
	}

	public void addVideo(String path) {
		isAddedVideo = true;
		localVideoPath = path;
	}

	private void uploadVideo(){
		if (isAddedVideo) {
			Uri video_uri = Uri.parse("file://" + localVideoPath);
			dlg_progress.show();
			FileModel.UploadVideo(video_uri, new BooleanListener() {
				@Override
				public void done(boolean flag, String fileName, String error) {
					if (flag) {
						if (mGaugeObj == null)
							register(fileName, error);
						else
							save(fileName, error);
					} else {
						dlg_progress.cancel();
						MessageUtil.showToast(instance, error);
					}
				}
			});
		} else if (mGaugeObj == null){
			register("", "");
		} else {
			save("", "");
		}
	}

	private void register(String video_name, String video_path) {
		final GaugeModel model = new GaugeModel();
		model.owner = ParseUser.getCurrentUser();
		model.description = edt_text.getText().toString().trim();
		model.webLink = edt_weblink.getText().toString().trim();
		model.video = video_path;
		model.videoName = video_name;
		model.bgColor = sp_background.getSelectedItemPosition();
		model.textColor = sp_text.getSelectedItemPosition();
		model.textSize = sp_size.getSelectedItemPosition();
		model.textFont = sp_font.getSelectedItemPosition();
		if (isAddedPhoto)
			model.photo = FileModel.createParseFile("photo.png", ResourceUtil.getPhotoFilePath());

		dlg_progress.show();
		GaugeModel.Register(model, new ExceptionListener() {
			@Override
			public void done(String error) {
				dlg_progress.cancel();
				if (error == null) {
					MessageUtil.showToast(instance, R.string.success);
					if (GaugeListActivity.instance != null)
						GaugeListActivity.instance.list_gauge.refresh();
					myBack();
				} else {
					MessageUtil.showToast(instance, error);
				}
			}
		});
	}


	private void save(String video_name, String video_path) {
		final GaugeModel model = new GaugeModel();
		model.parse(mGaugeObj);
		model.owner = ParseUser.getCurrentUser();
		model.description = edt_text.getText().toString().trim();
		model.webLink = edt_weblink.getText().toString().trim();
		model.video = video_path;
		model.videoName = video_name;
		model.bgColor = sp_background.getSelectedItemPosition();
		model.textColor = sp_text.getSelectedItemPosition();
		model.textSize = sp_size.getSelectedItemPosition();
		model.textFont = sp_font.getSelectedItemPosition();
		if (isAddedPhoto)
			model.photo = FileModel.createParseFile("photo.png", ResourceUtil.getPhotoFilePath());
		dlg_progress.show();
		BaseTask.run(new BaseTask.TaskListener() {

			@Override
			public Object onTaskRunning(int taskId, Object data) {
				// TODO Auto-generated method stub
				try {
					if (isAddedPhoto)
						model.photo.save();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void onTaskResult(int taskId, Object result) {
				mGaugeObj.put(ParseConstants.KEY_OWNER, ParseUser.getCurrentUser());
				mGaugeObj.put(ParseConstants.KEY_DESCRIPTION, model.description);
				mGaugeObj.put(ParseConstants.KEY_WEB_LINK, model.webLink);
				mGaugeObj.put(ParseConstants.KEY_BG_COLOR, model.bgColor);
				mGaugeObj.put(ParseConstants.KEY_TEXT_COLOR, model.textColor);
				if (isAddedPhoto) {
					mGaugeObj.put(ParseConstants.KEY_PHOTO, model.photo);
				}
				mGaugeObj.saveInBackground(new SaveCallback() {
					public void done(ParseException e) {
						dlg_progress.cancel();
						if (e == null) {
							MessageUtil.showToast(instance, R.string.success);
							if (GaugeListActivity.instance != null)
								GaugeListActivity.instance.list_gauge.refresh();
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
		GaugeModel.Delete(mGaugeObj, new ExceptionListener() {
			@Override
			public void done(String error) {
				dlg_progress.cancel();
				if (error == null) {
					MessageUtil.showToast(instance, R.string.success);
					if (GaugeListActivity.instance != null)
						GaugeListActivity.instance.list_gauge.refresh();
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

	private void startCropImageActivity(Uri imageUri) {
		CropImage.activity(imageUri)
				.setGuidelines(CropImageView.Guidelines.ON)
				.setMultiTouchEnabled(true)
				.setAspectRatio(4, 3)
				.start(instance);
	}
}


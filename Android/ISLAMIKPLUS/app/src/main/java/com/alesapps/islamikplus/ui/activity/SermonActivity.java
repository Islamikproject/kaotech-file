package com.alesapps.islamikplus.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.BooleanListener;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.model.FileModel;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.SermonModel;
import com.alesapps.islamikplus.push.PushNoti;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.alesapps.islamikplus.utils.ResourceUtil;
import com.parse.ParseUser;

public class SermonActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static SermonActivity instance = null;
	EditText edt_topic;
	Spinner sp_amount;
	Spinner sp_language;
	Button btn_next;
	Button btn_save;
	public static int type = SermonModel.TYPE_JUMAH;
	final int VIDEO_PICK = 1000;
	String[] languageCode;
	String[] languageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		if (type == SermonModel.TYPE_JUMAH)
			SetTitle(R.string.jumah_sermon, -1);
		else
			SetTitle(R.string.regular_sermon, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_sermon);
		edt_topic = findViewById(R.id.edt_topic);
		sp_amount = findViewById(R.id.sp_amount);
		sp_language = findViewById(R.id.sp_language);
		btn_next = findViewById(R.id.btn_next);
		btn_save = findViewById(R.id.btn_save);

		findViewById(R.id.btn_next).setOnClickListener(this);
		findViewById(R.id.btn_save).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		btn_save.setVisibility(View.GONE);
		btn_next.setVisibility(View.VISIBLE);
		ArrayAdapter<String> adapterAmount = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.STRING_AMOUNT);
		sp_amount.setAdapter(adapterAmount);
		sp_amount.setSelection(0);
		languageCode = CommonUtil.getAllLanguageCode();
		languageName = CommonUtil.getAllLanguageName();
		ArrayAdapter<String> adapterLanguage = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, languageName);
		sp_language.setAdapter(adapterLanguage);
		sp_language.setSelection(0);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.btn_next:
				if (isValid())
					showUploadDialog();
				return;
		}
	}

	private boolean isValid() {
		String topic = edt_topic.getText().toString().trim();
		if (TextUtils.isEmpty(topic)) {
			MessageUtil.showError(instance, R.string.valid_No_topic);
			edt_topic.requestFocus();
			return false;
		}
		return true;
	}

	private void showUploadDialog() {
		new AlertDialog.Builder(instance)
				.setTitle(R.string.upload_video_audio)
				.setPositiveButton(R.string.take_new_video, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(instance, ReadyActivity.class));
					}
				})
				.setNegativeButton(R.string.select_video_gallery, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (Build.VERSION.SDK_INT < 19) {
							Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
							intent.setType("video/*");
							startActivityForResult(intent, VIDEO_PICK);
						} else {
							Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
							intent.addCategory(Intent.CATEGORY_OPENABLE);
							intent.setType("video/*");
							intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"video/*"});
							startActivityForResult(intent, VIDEO_PICK);
						}
					}
				})
				.setNeutralButton(R.string.select_audio_gallery, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (Build.VERSION.SDK_INT < 19) {
							Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
							intent.setType("audio/*");
							startActivityForResult(intent, VIDEO_PICK);
						} else {
							Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
							intent.addCategory(Intent.CATEGORY_OPENABLE);
							intent.setType("audio/*");
							intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"audio/*"});
							startActivityForResult(intent, VIDEO_PICK);
						}
					}
				})
				.show();
	}


	@SuppressLint("MissingSuperCall")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
				String selectedImagePath = ResourceUtil.generatePath(selectedImageUri, this);
				uploadVideo(selectedImagePath, false);
			} else if (mimeType.startsWith("audio")) {
				Uri selectedImageUri = data.getData();
				String selectedImagePath = ResourceUtil.generatePath(selectedImageUri, this);
				uploadVideo(selectedImagePath, true);
			}
		}
	}

	public void uploadVideo(String path, final boolean isAudio){
		Uri video_uri = Uri.parse("file://" + path);
		dlg_progress.show();
		FileModel.UploadVideo(video_uri, new BooleanListener() {
			@Override
			public void done(boolean flag, String fileName, String error) {
				if (flag) {
					save(fileName, error, isAudio);
				} else {
					dlg_progress.cancel();
					MessageUtil.showToast(instance, error);
				}
			}
		});
	}

	private void save(String video_name, String video_path, boolean isAudio) {
		final SermonModel model = new SermonModel();
		model.owner = ParseUser.getCurrentUser();
		model.type = type;
		model.topic = edt_topic.getText().toString().trim();
		model.amount = AppConstant.ARRAY_AMOUNT[sp_amount.getSelectedItemPosition()];
		model.video = video_path;
		model.videoName = video_name;
		model.language = languageCode[sp_language.getSelectedItemPosition()];
		model.isAudio = isAudio;
		SermonModel.Register(model, new ExceptionListener() {
			@Override
			public void done(String error) {
				dlg_progress.cancel();
				if (error == null) {
					String message = String.format(getString(R.string.success_jumah_message), model.owner.getString(ParseConstants.KEY_MOSQUE), model.topic);
					if (type == SermonModel.TYPE_REGULAR)
						message = String.format(getString(R.string.success_regular_message), model.owner.getString(ParseConstants.KEY_MOSQUE), model.topic);
					PushNoti.sendPushAll(type, message, "", null);
					MessageUtil.showToast(instance, R.string.success);
					if (SermonListActivity.instance != null)
						SermonListActivity.instance.list_sermon.refresh();
					myBack();
				} else {
					MessageUtil.showToast(instance, error);
				}
			}
		});
	}
}


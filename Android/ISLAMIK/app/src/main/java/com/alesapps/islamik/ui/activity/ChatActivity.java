package com.alesapps.islamik.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.BooleanListener;
import com.alesapps.islamik.listener.ObjectListListener;
import com.alesapps.islamik.listener.ObjectListener;
import com.alesapps.islamik.model.ChatModel;
import com.alesapps.islamik.model.FileModel;
import com.alesapps.islamik.model.NotificationModel;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.push.PushNoti;
import com.alesapps.islamik.ui.dialog.PhotoDialog;
import com.alesapps.islamik.ui.dialog.VideoDialog;
import com.alesapps.islamik.utils.BaseTask;
import com.alesapps.islamik.utils.CommonUtil;
import com.alesapps.islamik.utils.DateTimeUtils;
import com.alesapps.islamik.utils.MessageUtil;
import com.alesapps.islamik.utils.ResourceUtil;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActionBarActivity implements View.OnClickListener{
	public static ChatActivity instance = null;
	private EditText _edMessage;
	private ListView _lvChat;
	ImageView btnRecStart;
	ImageView btnRecStop;

	private ArrayList<ChatModel> mChatList = new ArrayList<>();
	private List<ParseObject> mServerDataList = new ArrayList<>();
	private ListAdapter adapter;
	public static ParseObject mBookObj;
	public static ParseUser mFriendObj;
	boolean isAudioRecording = false;
	MediaRecorder recorder;
	MediaPlayer mediaPlayer;
	String[] ATTACH_ARRAY = {"Take a new photo", "Select photo from gallery", "Take a new video", "Select video from gallery"};
	final int PICTURE_PICK = 1000;
	final int CAMERA_CAPTURE = 1001;
	final int VIDEO_PICK = 2000;
	boolean isPhotoAdded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(UserModel.GetFullName(mFriendObj), -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_chat);
		btnRecStart = findViewById(R.id.btnRecStart);
		btnRecStop = findViewById(R.id.btnRecStop);
		_edMessage = findViewById(R.id.edMessage);
		_lvChat = findViewById(R.id.lvChat);
		adapter = new ListAdapter();
		_lvChat.setAdapter(adapter);
		findViewById(R.id.btnAttach).setOnClickListener(this);
		findViewById(R.id.btnSend).setOnClickListener(this);
		findViewById(R.id.btnRecStart).setOnClickListener(this);
		findViewById(R.id.btnRecStop).setOnClickListener(this);
		showData();
	}

	@Override
	public void onClick(View view) {
		CommonUtil.hideKeyboard(instance, _edMessage);
		switch (view.getId()) {
			case R.id.action_back:
				onBackPressed();
				break;
			case R.id.btnAttach:
				showChooseDialog();
				break;
			case R.id.btnSend:
				if (isValid())
					send("");
				break;
			case R.id.btnRecStart:
				onRecord(true);
				break;
			case R.id.btnRecStop:
				onRecord(false);
				break;
		}
	}

	private void showChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.upload_photo_video));
		builder.setItems(ATTACH_ARRAY, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0: // Take a new photo
						chooseTakePhoto(true);
						break;
					case 1: // Select photo from gallery
						chooseTakePhoto(false);
						break;
					case 2: // Take a new video
						startActivity(new Intent(instance, CameraActivity.class));
						break;
					case 3: // Select video from gallery
						Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("video/*");
						intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"video/*"});
						startActivityForResult(intent, VIDEO_PICK);
						break;
				}
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	// click record button
	private void onRecord(boolean start) {
		btnRecStart.setVisibility(View.GONE);
		btnRecStop.setVisibility(View.GONE);
		if (start) {
			btnRecStop.setVisibility(View.VISIBLE);
			startRecording();
		} else {
			btnRecStart.setVisibility(View.VISIBLE);
			stopRecording();
		}
	}

	// start recording
	private void startRecording() {
		isAudioRecording = true;
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(ResourceUtil.getRecordFilePath());
		try {
			recorder.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		recorder.start();
	}

	// stop recording
	private void stopRecording() {
		recorder.stop();
		recorder.release();
		send("");
	}

	private boolean isValid() {
		String message = _edMessage.getText().toString().trim();
		if (TextUtils.isEmpty(message)) {
			MessageUtil.showToast(instance, R.string.insert_message);
			return false;
		}
		return true;
	}

	private void showData() {
		dlg_progress.show();
		ChatModel.GetChatList(mBookObj, new ObjectListListener() {
			@Override
			public void done(List<ParseObject> objects, String error) {
				// TODO Auto-generated method stub
				dlg_progress.cancel();
				mServerDataList.clear();
				mChatList.clear();
				if (objects != null && objects.size() > 0) {
					mServerDataList.addAll(objects);
					for (int i = 0; i < objects.size(); i ++) {
						ChatModel chat = new ChatModel();
						chat.message = objects.get(i).getString(ParseConstants.KEY_MESSAGE);
						chat.sender = objects.get(i).getParseUser(ParseConstants.KEY_SENDER);
						chat.receiver = objects.get(i).getParseUser(ParseConstants.KEY_RECEIVER);
						chat.dateTime = DateTimeUtils.dateToString(objects.get(i).getCreatedAt(), DateTimeUtils.DATE_TIME_STRING_FORMAT);
						chat.voiceFile = objects.get(i).getParseFile(ParseConstants.KEY_VOICE_FILE);
						chat.photo = objects.get(i).getParseFile(ParseConstants.KEY_PHOTO);
						chat.video = objects.get(i).getString(ParseConstants.KEY_VIDEO);
						if (chat.sender.getObjectId().equals(ParseUser.getCurrentUser().getObjectId()))
							chat.ismymessage = true;
						else
							chat.ismymessage = false;
						mChatList.add(chat);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	public void uploadFile(Uri video) {
		dlg_progress.show();
		FileModel.UploadVideo(video, new BooleanListener() {
			@Override
			public void done(boolean flag, String fileName, String error) {
				dlg_progress.cancel();
				if (flag) {
					send(error);
				} else {
					dlg_progress.cancel();
					MessageUtil.showToast(instance, error);
				}
			}
		});
	}

	private void send(String video_path) {
		dlg_progress.show();
		final ChatModel model = new ChatModel();
		model.sender = ParseUser.getCurrentUser();
		model.receiver = mFriendObj;
		model.bookObj = mBookObj;
		model.message = _edMessage.getText().toString().trim();
		model.video = video_path;
		if (isAudioRecording) {
			model.message = getString(R.string.attached_voice);
			model.voiceFile = FileModel.createParseFile(ResourceUtil.getRecordFilePath());
		}
		if (isPhotoAdded) {
			model.message = getString(R.string.attached_photo);
			model.photo = FileModel.createParseFile(ResourceUtil.getPhotoFilePath());
		}

		ChatModel.sendMessage(model, new ObjectListener() {
			@Override
			public void done(ParseObject object, String error) {
				// TODO Auto-generated method stub
				dlg_progress.cancel();
				if (error == null) {
					String notiMsg = String.format(getString(R.string.noti_chat), UserModel.GetFullName(ParseUser.getCurrentUser()), model.message);
					PushNoti.sendPush(NotificationModel.TYPE_CHAT, mFriendObj, notiMsg, mBookObj.getObjectId(), null);
					showData();
					_edMessage.setText("");
					isAudioRecording = false;
					isPhotoAdded = false;
				} else {
					Toast.makeText(instance, error, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private class ListAdapter extends BaseAdapter {

		public int getCount() {
			return mChatList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			LinearLayout _lyMy;
			TextView _txtMyMessage;
			TextView _txtMyCreated;
			LinearLayout _lyFriend;
			TextView _txtFriendName;
			TextView _txtFriendMessage;
			TextView _txtFriendCreated;
			ImageView imgMyAvatar;
			ImageView imgFriendAvatar;
			ImageView img_my;
			ImageView img_friend;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(instance, R.layout.item_chat, null);
				holder = new ViewHolder();
				holder._lyMy = convertView.findViewById(R.id.lyMy);
				holder._txtMyMessage = convertView.findViewById(R.id.txtMyMessage);
				holder._txtMyCreated = convertView.findViewById(R.id.txtMyCreated);
				holder._lyFriend = convertView.findViewById(R.id.lyFriend);
				holder._txtFriendName = convertView.findViewById(R.id.txtFriendName);
				holder._txtFriendMessage = convertView.findViewById(R.id.txtFriendMessage);
				holder._txtFriendCreated = convertView.findViewById(R.id.txtFriendCreated);
				holder.imgMyAvatar = convertView.findViewById(R.id.imgMyAvatar);
				holder.imgFriendAvatar = convertView.findViewById(R.id.imgFriendAvatar);
				holder.img_my = convertView.findViewById(R.id.img_my);
				holder.img_friend = convertView.findViewById(R.id.img_friend);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final ChatModel model = mChatList.get(position);
			if (model.ismymessage) {
				holder._lyMy.setVisibility(View.VISIBLE);
				holder._lyFriend.setVisibility(View.GONE);
				if (model.voiceFile == null && model.photo == null && TextUtils.isEmpty(model.video)) {
					holder._txtMyMessage.setVisibility(View.VISIBLE);
					holder.img_my.setVisibility(View.GONE);
					holder._txtMyMessage.setText(model.message);
				} else {
					holder._txtMyMessage.setVisibility(View.GONE);
					holder.img_my.setVisibility(View.VISIBLE);
					if (model.voiceFile != null)
						holder.img_my.setBackgroundResource(R.drawable.img_voice);
					else if (!TextUtils.isEmpty(model.video))
						holder.img_my.setBackgroundResource(R.drawable.img_video);
					else if (model.photo != null)
						Picasso.get().load(CommonUtil.getImagePath(model.photo.getUrl())).into(holder.img_my);
				}
				holder._txtMyCreated.setText(model.dateTime);
				ParseFile myAvatarFile = model.sender.getParseFile(ParseConstants.KEY_AVATAR);
				if (myAvatarFile != null)
					Picasso.get().load(CommonUtil.getImagePath(myAvatarFile.getUrl())).into(holder.imgMyAvatar);
			} else {
				holder._lyMy.setVisibility(View.GONE);
				holder._lyFriend.setVisibility(View.VISIBLE);
				holder._txtFriendMessage.setText(model.message);
				if (model.voiceFile == null && model.photo == null && TextUtils.isEmpty(model.video)) {
					holder._txtFriendMessage.setVisibility(View.VISIBLE);
					holder.img_friend.setVisibility(View.GONE);
					holder._txtFriendMessage.setText(model.message);
				} else {
					holder._txtFriendMessage.setVisibility(View.GONE);
					holder.img_friend.setVisibility(View.VISIBLE);
					if (model.voiceFile != null)
						holder.img_friend.setBackgroundResource(R.drawable.img_voice);
					else if (!TextUtils.isEmpty(model.video))
						holder.img_friend.setBackgroundResource(R.drawable.img_video);
					else if (model.photo != null)
						Picasso.get().load(CommonUtil.getImagePath(model.photo.getUrl())).into(holder.img_friend);
				}
				holder._txtFriendCreated.setText(model.dateTime);
				holder._txtFriendName.setText(UserModel.GetFullName(model.sender));
				ParseFile myFriendFile = model.sender.getParseFile(ParseConstants.KEY_AVATAR);
				if (myFriendFile != null)
					Picasso.get().load(CommonUtil.getImagePath(myFriendFile.getUrl())).into(holder.imgFriendAvatar);
			}
			holder.img_my.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (model.voiceFile != null) {
						play(CommonUtil.getImagePath(model.voiceFile.getUrl()));
					} else if (model.photo != null) {
						PhotoDialog.photoFile = model.photo;
						startActivity(new Intent(instance, PhotoDialog.class));
					} else if (!TextUtils.isEmpty(model.video)) {
						VideoDialog.mVideoPath = model.video;
						startActivity(new Intent(instance, VideoDialog.class));
					}
				}
			});
			holder.img_friend.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (model.voiceFile != null) {
						play(CommonUtil.getImagePath(model.voiceFile.getUrl()));
					}  else if (model.photo != null) {
						PhotoDialog.photoFile = model.photo;
						startActivity(new Intent(instance, PhotoDialog.class));
					} else if (!TextUtils.isEmpty(model.video)) {
						VideoDialog.mVideoPath = model.video;
						startActivity(new Intent(instance, VideoDialog.class));
					}
				}
			});
			return convertView;
		}
	}

	public void refreshData() {
		if (instance == null)
			instance = this;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showData();
			}
		});
	}

	private void play(final String path) {
		BaseTask.run(new BaseTask.TaskListener() {
			@Override
			public Object onTaskRunning(int taskId, Object data) {
				// TODO Auto-generated method stub
				try {
					mediaPlayer = MediaPlayer.create(instance, Uri.parse(path));
					mediaPlayer.start();
				} catch (Exception ex) {}
				return null;
			}
			@Override
			public void onTaskResult(int taskId, Object result) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onTaskProgress(int taskId, Object... values) {}
			@Override
			public void onTaskPrepare(int taskId, Object data) {}
			@Override
			public void onTaskCancelled(int taskId) {}
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
		if (requestCode == PICTURE_PICK && resultCode == Activity.RESULT_OK) {
			Uri imageUri = CropImage.getPickImageResultUri(this, data);
			startCropImageActivity(imageUri);
		}

		if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
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
						isPhotoAdded = true;
						send("");
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
				String selectedImagePath = ResourceUtil.generatePath(selectedImageUri, this);
				uploadFile(selectedImageUri);
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
}
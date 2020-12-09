package com.alesapps.islamik.ui.activity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.ObjectListListener;
import com.alesapps.islamik.listener.ObjectListener;
import com.alesapps.islamik.model.ChatModel;
import com.alesapps.islamik.model.FileModel;
import com.alesapps.islamik.model.NotificationModel;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.push.PushNoti;
import com.alesapps.islamik.utils.BaseTask;
import com.alesapps.islamik.utils.CommonUtil;
import com.alesapps.islamik.utils.DateTimeUtils;
import com.alesapps.islamik.utils.MessageUtil;
import com.alesapps.islamik.utils.ResourceUtil;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import java.io.IOException;
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
			case R.id.btnSend:
				if (isValid())
					send();
				break;
			case R.id.btnRecStart:
				onRecord(true);
				break;
			case R.id.btnRecStop:
				onRecord(false);
				break;
		}
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
		send();
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
	
	private void send() {
		dlg_progress.show();
		final ChatModel model = new ChatModel();
		model.sender = ParseUser.getCurrentUser();
		model.receiver = mFriendObj;
		model.bookObj = mBookObj;
		model.message = _edMessage.getText().toString().trim();
		String path = "";
		if (isAudioRecording) {
			model.message = getString(R.string.attached_voice);
			model.voiceFile = FileModel.createParseFile(ResourceUtil.getRecordFilePath());
		}

		ChatModel.sendMessage(model, path, new ObjectListener() {
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
				if (model.voiceFile == null) {
					holder._txtMyMessage.setVisibility(View.VISIBLE);
					holder.img_my.setVisibility(View.GONE);
					holder._txtMyMessage.setText(model.message);
				} else {
					holder._txtMyMessage.setVisibility(View.GONE);
					holder.img_my.setVisibility(View.VISIBLE);
				}
				holder._txtMyCreated.setText(model.dateTime);
				ParseFile myAvatarFile = model.sender.getParseFile(ParseConstants.KEY_AVATAR);
				if (myAvatarFile != null)
					Picasso.get().load(CommonUtil.getImagePath(myAvatarFile.getUrl())).into(holder.imgMyAvatar);
			} else {
				holder._lyMy.setVisibility(View.GONE);
				holder._lyFriend.setVisibility(View.VISIBLE);
				holder._txtFriendMessage.setText(model.message);
				if (model.voiceFile == null) {
					holder._txtFriendMessage.setVisibility(View.VISIBLE);
					holder.img_friend.setVisibility(View.GONE);
					holder._txtFriendMessage.setText(model.message);
				} else {
					holder._txtFriendMessage.setVisibility(View.GONE);
					holder.img_friend.setVisibility(View.VISIBLE);
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
					play(CommonUtil.getImagePath(model.voiceFile.getUrl()));
				}
			});
			holder.img_friend.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					play(CommonUtil.getImagePath(model.voiceFile.getUrl()));
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
}
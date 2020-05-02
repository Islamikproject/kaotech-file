package com.alesapps.islamik.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import com.alesapps.islamik.R;
import com.alesapps.islamik.model.ParseConstants;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

public class VideoActivity extends BaseActionBarActivity{
	public static VideoActivity instance = null;
	UniversalVideoView mVideoView;
	UniversalMediaController mMediaController;
	public static ParseUser mUser;
	public static ParseObject mSermonObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(null, 0);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_video);
		mVideoView = findViewById(R.id.videoView);
		mMediaController = findViewById(R.id.media_controller);
		mVideoView.setMediaController(mMediaController);

		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mVideoView.start();
			}
		});
		mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				showConfirmDialog();
			}
		});
		initialize();
	}

	private void initialize() {
		String video_path = mSermonObj.getString(ParseConstants.KEY_VIDEO);
		if (!TextUtils.isEmpty(video_path)) {
			mVideoView.setVideoPath(video_path);
			mVideoView.requestFocus();
			mMediaController.showLoading();
		}
	}

	private void showConfirmDialog() {
		String amount = String.valueOf(mSermonObj.getDouble(ParseConstants.KEY_AMOUNT));
		if (amount.equals("0") || amount.equals("0.0"))
			amount = "";
		else
			amount = "$" + amount;
		String message = String.format(getString(R.string.confirm_mosque_virtual_basket), amount);
		new AlertDialog.Builder(instance)
				.setTitle(R.string.app_name)
				.setMessage(message)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						DonationActivity.mSermonObj = mSermonObj;
						startActivity(new Intent(instance, DonationActivity.class));
						finish();
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
	}
}


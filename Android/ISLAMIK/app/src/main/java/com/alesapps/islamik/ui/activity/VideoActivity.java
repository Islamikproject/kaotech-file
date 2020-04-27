package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import com.alesapps.islamik.R;
import com.alesapps.islamik.model.ParseConstants;
import com.parse.ParseFile;
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
				DonationActivity.mSermonObj = mSermonObj;
				startActivity(new Intent(instance, DonationActivity.class));
				finish();
			}
		});
		initialize();
	}

	private void initialize() {
		ParseFile videoFile = mSermonObj.getParseFile(ParseConstants.KEY_VIDEO);
		if (videoFile != null)
			mVideoView.setVideoPath(videoFile.getUrl());
	}
}


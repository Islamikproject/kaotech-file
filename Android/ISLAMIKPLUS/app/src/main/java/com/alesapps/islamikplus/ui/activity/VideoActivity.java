package com.alesapps.islamikplus.ui.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.model.ParseConstants;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

public class VideoActivity extends BaseActionBarActivity{
	public static VideoActivity instance = null;
	UniversalVideoView mVideoView;
	UniversalMediaController mMediaController;
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
		initialize();
	}

	private void initialize() {
		ParseFile videoFile = mSermonObj.getParseFile(ParseConstants.KEY_VIDEO);
		if (videoFile != null)
			mVideoView.setVideoPath(videoFile.getUrl());
	}
}


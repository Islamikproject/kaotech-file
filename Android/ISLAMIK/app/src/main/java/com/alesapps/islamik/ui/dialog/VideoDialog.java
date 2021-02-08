package com.alesapps.islamik.ui.dialog;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;
import com.alesapps.islamik.R;

public class VideoDialog extends Activity implements View.OnClickListener {
	public static VideoDialog instance = null;
	VideoView videoView;
	public static String mVideoPath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.dialog_video);
		Window window = getWindow();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(getResources().getColor(R.color.black));
		}
		videoView = findViewById(R.id.videoView);
		findViewById(R.id.btn_close).setOnClickListener(this);
		initialize();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_close:
				finish();
				return;
		}
	}

	private void initialize() {
		videoView.setVideoPath(mVideoPath);
		videoView.setMediaController(new MediaController(instance));
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				videoView.start();
			}
		});
	}
}


package com.alesapps.islamikplus.ui.activity;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.alesapps.islamikplus.utils.ResourceUtil;
import com.parse.ParseObject;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

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
		videoDownload();
	}

	private void videoDownload() {
		String video_path = mSermonObj.getString(ParseConstants.KEY_VIDEO);
		String video_name = mSermonObj.getString(ParseConstants.KEY_VIDEO_NAME) + ".mp4";
		String local_path = ResourceUtil.getVideoFilePath(video_name);
		File videoFile = new File(local_path);
		if (videoFile.length() > 100) {
			initialize(local_path);
		} else {
			dlg_progress.show();
			new DownloadFile().execute(video_path, local_path);
		}
	}


	private class DownloadFile extends AsyncTask<String, String, String> {
		String path = "";
		@Override
		protected String doInBackground(String... url) {
			int count;
			try {
				path = url[1];
				URL path = new URL(url[0]);
				URLConnection conexion = path.openConnection();
				conexion.connect();
				InputStream input = new BufferedInputStream(path.openStream());
				OutputStream output = new FileOutputStream(url[1]);
				byte data[] = new byte[1024];
				while ((count = input.read(data)) != -1) {
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				return e.toString();
			}
			return null;
		}

		protected void onPostExecute(String result) {
			dlg_progress.cancel();
			if (result == null) {
				initialize(path);
			} else {
				MessageUtil.showToast(instance, result);
			}
		}
	}

	private void initialize(String video_path) {
		mVideoView.setVideoPath(video_path);
		mVideoView.requestFocus();
		mMediaController.showLoading();
	}
}


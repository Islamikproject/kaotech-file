package com.alesapps.islamikplus.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.SermonModel;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.alesapps.islamikplus.utils.ResourceUtil;
import com.parse.ParseObject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class VideoActivity extends BaseActionBarActivity{
	public static VideoActivity instance = null;
	VideoView videoView;
	public static ParseObject mSermonObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(null, 0);
		ShowActionBarIcons(true, R.id.action_back, R.id.action_delete);
		setContentView(R.layout.activity_video);
		videoView = findViewById(R.id.videoView);
		videoDownload();
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.action_delete:
				showDeleteDialog();
				return;
		}
	}

	private void videoDownload() {
		String video_path = mSermonObj.getString(ParseConstants.KEY_VIDEO);
		String video_name = mSermonObj.getString(ParseConstants.KEY_VIDEO_NAME) + ".mp4";
		String local_path = ResourceUtil.getVideoFilePath(video_name);
		File videoFile = new File(local_path);
		if (videoFile.length() > 100) {
			videoLoad(local_path);
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
				videoLoad(path);
			} else {
				MessageUtil.showToast(instance, result);
			}
		}
	}

	private void videoLoad(final String video_path) {
		videoView.setVideoPath(video_path);
		videoView.setMediaController(new MediaController(instance));
		videoView.requestFocus();
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				videoView.start();
			}
		});
		videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				MessageUtil.showToast(instance, "Video is not valid");
				File file = new File(video_path);
				file.delete();
				return true;
			}
		});
	}

	private void showDeleteDialog() {
		new AlertDialog.Builder(instance)
				.setTitle(R.string.delete)
				.setMessage(R.string.confirm_delete_sermon)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						delete();
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
	}

	private void delete() {
		dlg_progress.show();
		SermonModel.Delete(mSermonObj, new ExceptionListener() {
			@Override
			public void done(String error) {
				dlg_progress.cancel();
				if (error == null) {
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


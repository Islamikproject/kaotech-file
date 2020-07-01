package com.alesapps.islamik.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.R;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.utils.MessageUtil;
import com.alesapps.islamik.utils.ResourceUtil;
import com.parse.ParseObject;
import com.parse.ParseUser;
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
	public static ParseUser mUser;
	public static ParseObject mSermonObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(null, 0);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_video);
		videoView = findViewById(R.id.videoView);
		videoDownload();
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
		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				showConfirmDialog();
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
						String url = AppConstant.STRIPE_CONNECT_URL + mSermonObj.getObjectId();
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						startActivity(browserIntent);
						finish();
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
	}
}


package com.alesapps.islamik.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.core.content.FileProvider;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.AppGlobals;
import com.alesapps.islamik.AppPreference;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.ExceptionListener;
import com.alesapps.islamik.listener.ObjectListener;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.PostModel;
import com.alesapps.islamik.model.SermonModel;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.ui.dialog.PhotoDialog;
import com.alesapps.islamik.utils.CommonUtil;
import com.alesapps.islamik.utils.MessageUtil;
import com.alesapps.islamik.utils.ResourceUtil;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends BaseActionBarActivity implements OnClickListener {
	public static MainActivity instance = null;
	TextView txt_fajr_time;
	TextView txt_zuhr_time;
	TextView txt_asr_time;
	TextView txt_maghrib_time;
	TextView txt_isha_time;
	TextView txt_location;
	VideoView videoView;
	ImageView img_main;

	ParseFile mMainImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance = this;
		SetTitle(null, 0);
		ShowActionBarIcons(true, R.id.action_notification, R.id.action_language);
		setContentView(R.layout.activity_main);

		txt_fajr_time = findViewById(R.id.txt_fajr_time);
		txt_zuhr_time = findViewById(R.id.txt_zuhr_time);
		txt_asr_time = findViewById(R.id.txt_asr_time);
		txt_maghrib_time = findViewById(R.id.txt_maghrib_time);
		txt_isha_time = findViewById(R.id.txt_isha_time);
		txt_location = findViewById(R.id.txt_location);
		videoView = findViewById(R.id.videoView);
		img_main = findViewById(R.id.img_main);

		findViewById(R.id.layout_sermon).setOnClickListener(this);
		findViewById(R.id.layout_prayers).setOnClickListener(this);
		findViewById(R.id.layout_quran).setOnClickListener(this);
		findViewById(R.id.layout_messages).setOnClickListener(this);
		findViewById(R.id.layout_settings).setOnClickListener(this);
		findViewById(R.id.layout_order).setOnClickListener(this);
		findViewById(R.id.layout_book).setOnClickListener(this);
		findViewById(R.id.btn_full).setOnClickListener(this);
		findViewById(R.id.btn_share).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		txt_location.setText(ParseUser.getCurrentUser().getString(ParseConstants.KEY_ADDRESS));
		new PrayerTimeConnectAsyncTask().execute();
		getVideo();
		getPost();
	}

	private void getVideo() {
		SermonModel.GetLatestVideo(new ObjectListener() {
			@Override
			public void done(ParseObject object, String error) {
				if (error == null && object != null) {
					String video_path = object.getString(ParseConstants.KEY_VIDEO);
					videoView.setVideoPath(video_path);
					videoView.setMediaController(new MediaController(instance));
					videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mediaPlayer) {
							videoView.start();
						}
					});
				}
			}
		});
	}

	private void getPost() {
		PostModel.GetPost(new ObjectListener() {
			@Override
			public void done(ParseObject object, String error) {
				if (error == null && object != null) {
					mMainImage = object.getParseFile(ParseConstants.KEY_PHOTO);
					String path = mMainImage.getUrl();
					Picasso.get().load(CommonUtil.getImagePath(path)).into(img_main);
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
			case R.id.action_notification:
				startActivity(new Intent(instance, NotificationActivity.class));
				break;
			case R.id.action_language:
				SelectLanguageActivity.isMain = true;
				startActivity(new Intent(instance, SelectLanguageActivity.class));
				break;
			case R.id.layout_sermon:
				startActivity(new Intent(instance, JumahSermonActivity.class));
				break;
			case R.id.layout_prayers:
				startActivity(new Intent(instance, DailyPrayersActivity.class));
				break;
			case R.id.layout_quran:
				startActivity(new Intent(instance, QuranActivity.class));
				break;
			case R.id.layout_messages:
				startActivity(new Intent(instance, MessagesActivity.class));
				break;
			case R.id.layout_settings:
				startActivity(new Intent(instance, SettingsActivity.class));
				break;
			case R.id.layout_order:
				startActivity(new Intent(instance, OrderActivity.class));
				break;
			case R.id.layout_book:
				startActivity(new Intent(instance, BookActivity.class));
				break;
			case R.id.btn_full:
				if (mMainImage != null) {
					PhotoDialog.photoFile = mMainImage;
					startActivity(new Intent(instance, PhotoDialog.class));
				}
				break;
			case R.id.btn_share:
				if (mMainImage != null) {
					dlg_progress.show();
					new DownloadFile().execute(CommonUtil.getImagePath(mMainImage.getUrl()), ResourceUtil.getVideoFilePath("share.png"));
				}
				break;
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
				shareImage(path);
			} else {
				MessageUtil.showToast(instance, result);
			}
		}
	}

	private void shareImage(String path) {
		File imageFile = new File(path);
		Uri imageURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".provider", imageFile);
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("image/*");
		i.putExtra(Intent.EXTRA_STREAM, imageURI);
		startActivity(Intent.createChooser(i, "Share Image"));
	}

	private class PrayerTimeConnectAsyncTask extends AsyncTask<Void, Void, Connection.Response> {
		Document document;
		private ArrayList<String> timeList = new ArrayList<>();
		private PrayerTimeConnectAsyncTask() {}

		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Connection.Response doInBackground(Void... params) {
			try {
				document = Jsoup.connect(AppConstant.PRAYER_TIME_URL).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0").timeout(10000).get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (document != null) {
				Elements lis = document.select("span");
				for (int i = 0; i < lis.size(); i ++) {
					String key = lis.get(i).attr("class");
					String value = lis.get(i).html();
					if (key.equals("todayPrayerTime")) {
						timeList.add(value);
					}
				}
			}
			return null;
		}

		protected void onPostExecute(Connection.Response response) {
			if (response == null) {
				for (int i = 0; i < timeList.size(); i ++) {
					if (i == 0) {
						txt_fajr_time.setText(timeList.get(i));
						AppGlobals.PRAYER_TIME[0] = timeList.get(i);
					} else if (i == 2) {
						txt_zuhr_time.setText(timeList.get(i));
						AppGlobals.PRAYER_TIME[1] = timeList.get(i);
					} else if (i == 3) {
						txt_asr_time.setText(timeList.get(i));
						AppGlobals.PRAYER_TIME[2] = timeList.get(i);
					} else if (i == 4) {
						txt_maghrib_time.setText(timeList.get(i));
						AppGlobals.PRAYER_TIME[3] = timeList.get(i);
					} else if (i == 5) {
						txt_isha_time.setText(timeList.get(i));
						AppGlobals.PRAYER_TIME[4] = timeList.get(i);
					}
				}
			}
		}
	}

	private void logout() {
		new AlertDialog.Builder(instance)
				.setTitle(R.string.logout)
				.setMessage(R.string.label_log_out)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dlg_progress.show();
						UserModel.Logout(new ExceptionListener() {
							@Override
							public void done(String error) {
								// TODO Auto-generated method stub
								dlg_progress.cancel();
								if (error == null) {
									AppPreference.setBool(AppPreference.KEY.SIGN_IN_AUTO, false);
									AppPreference.setStr(AppPreference.KEY.PHONE_NUMBER, "");
									AppPreference.setStr(AppPreference.KEY.PASSWORD, "");
									startActivity(new Intent(instance, LoginActivity.class));
									finish();
								} else {
									MessageUtil.showToast(instance, error, true);
								}
							}
						});
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}

	@Override
	public void onBackPressed() {
		onBackButtonPressed();
	}

	boolean isBackAllowed = false;
	private void onBackButtonPressed() {
		if (!isBackAllowed) {
			Toast.makeText(this, R.string.msg_alert_on_back_pressed, Toast.LENGTH_SHORT).show();
			isBackAllowed = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					isBackAllowed = false;
				}
			}, 2000);
		} else {
			finish();
		}
	}
}
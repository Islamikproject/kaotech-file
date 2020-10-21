package com.alesapps.islamikplus.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.alesapps.islamikplus.AppPreference;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.SermonModel;
import com.alesapps.islamikplus.model.UserModel;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.parse.ParseUser;

public class MainActivity extends BaseActionBarActivity implements OnClickListener {
	public static MainActivity instance = null;
	LinearLayout layout_jumah;
	LinearLayout layout_book;
	LinearLayout layout_post;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance = this;
		SetTitle(null, 0);
		ShowActionBarIcons(true, R.id.action_logout, R.id.action_notification);
		setContentView(R.layout.activity_main);
		layout_jumah = findViewById(R.id.layout_jumah);
		layout_book = findViewById(R.id.layout_book);
		layout_post = findViewById(R.id.layout_post);
		findViewById(R.id.layout_jumah).setOnClickListener(this);
		findViewById(R.id.layout_regular).setOnClickListener(this);
		findViewById(R.id.layout_donation).setOnClickListener(this);
		findViewById(R.id.layout_messages).setOnClickListener(this);
		findViewById(R.id.layout_settings).setOnClickListener(this);
		findViewById(R.id.layout_book).setOnClickListener(this);
		findViewById(R.id.layout_post).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		layout_jumah.setVisibility(View.GONE);
		layout_book.setVisibility(View.GONE);
		layout_post.setVisibility(View.GONE);
		int type = ParseUser.getCurrentUser().getInt(ParseConstants.KEY_TYPE);
		if (type == UserModel.TYPE_ADMIN) {
			layout_jumah.setVisibility(View.VISIBLE);
			layout_post.setVisibility(View.VISIBLE);
		} else if (type == UserModel.TYPE_MOSQUE) {
			layout_jumah.setVisibility(View.VISIBLE);
		} else if (type == UserModel.TYPE_INFLUENCER_WOMEN || type == UserModel.TYPE_INFLUENCER_KID || type == UserModel.TYPE_INFLUENCER_OTHER) {
			layout_book.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
			case R.id.action_logout:
				logout();
				break;
			case R.id.action_notification:
				startActivity(new Intent(instance, NotificationActivity.class));
				break;
			case R.id.action_language:
				SelectLanguageActivity.isMain = true;
				startActivity(new Intent(instance, SelectLanguageActivity.class));
				break;
			case R.id.layout_jumah:
				SermonListActivity.type = SermonModel.TYPE_JUMAH;
				startActivity(new Intent(instance, SermonListActivity.class));
				break;
			case R.id.layout_regular:
				SermonListActivity.type = SermonModel.TYPE_REGULAR;
				startActivity(new Intent(instance, SermonListActivity.class));
				break;
			case R.id.layout_donation:
				startActivity(new Intent(instance, DonationActivity.class));
				break;
			case R.id.layout_messages:
				startActivity(new Intent(instance, MessagesActivity.class));
				break;
			case R.id.layout_settings:
				startActivity(new Intent(instance, SettingsActivity.class));
				break;
			case R.id.layout_book:
				startActivity(new Intent(instance, BookActivity.class));
				break;
			case R.id.layout_post:
				startActivity(new Intent(instance, PostListActivity.class));
				break;
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

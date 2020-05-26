package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.alesapps.islamik.R;

public class MainActivity extends BaseActionBarActivity implements OnClickListener {
	public static MainActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance = this;
		SetTitle(null, 0);
		ShowActionBarIcons(true, R.id.action_language);
		setContentView(R.layout.activity_main);

		findViewById(R.id.layout_sermon).setOnClickListener(this);
		findViewById(R.id.layout_prayers).setOnClickListener(this);
		findViewById(R.id.layout_salat).setOnClickListener(this);
		findViewById(R.id.layout_jumah).setOnClickListener(this);
		findViewById(R.id.layout_nafilah).setOnClickListener(this);
		findViewById(R.id.layout_quran).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
			case R.id.action_language:
				SelectLanguageActivity.isMain = true;
				startActivity(new Intent(instance, SelectLanguageActivity.class));
				break;
			case R.id.layout_sermon:
				startActivity(new Intent(instance, SermonActivity.class));
//				startActivity(new Intent(instance, VerseActivity.class));
				break;
			case R.id.layout_prayers:
				startActivity(new Intent(instance, DailyPrayersActivity.class));
				break;
			case R.id.layout_salat:
				startActivity(new Intent(instance, SalatActivity.class));
				break;
			case R.id.layout_jumah:
				startActivity(new Intent(instance, JumahActivity.class));
				break;
			case R.id.layout_nafilah:
				startActivity(new Intent(instance, NafilahActivity.class));
				break;
			case R.id.layout_quran:
				startActivity(new Intent(instance, QuranActivity.class));
				break;
		}
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

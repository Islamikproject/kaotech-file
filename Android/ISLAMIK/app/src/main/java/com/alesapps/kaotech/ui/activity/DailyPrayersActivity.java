package com.alesapps.kaotech.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.alesapps.kaotech.AppConstant;
import com.alesapps.kaotech.R;

public class DailyPrayersActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static DailyPrayersActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.daily_prayers, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_daily_prayers);

		findViewById(R.id.layout_fajr).setOnClickListener(this);
		findViewById(R.id.layout_zuhr).setOnClickListener(this);
		findViewById(R.id.layout_asr).setOnClickListener(this);
		findViewById(R.id.layout_maghrib).setOnClickListener(this);
		findViewById(R.id.layout_isha).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.layout_fajr:
				gotoNextActivity(AppConstant.TYPE_FAJR);
				break;
			case R.id.layout_zuhr:
				gotoNextActivity(AppConstant.TYPE_ZUHR);
				break;
			case R.id.layout_asr:
				gotoNextActivity(AppConstant.TYPE_ASR);
				break;
			case R.id.layout_maghrib:
				gotoNextActivity(AppConstant.TYPE_MAGHRIB);
				break;
			case R.id.layout_isha:
				gotoNextActivity(AppConstant.TYPE_ISHA);
				break;
		}
	}

	private void gotoNextActivity(int type) {
		PrayersActivity.type = type;
		startActivity(new Intent(instance, PrayersActivity.class));
	}
}

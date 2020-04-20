package com.alesapps.islamikplus.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import com.alesapps.islamikplus.AppPreference;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.utils.CommonUtil;

public class SelectLanguageActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static SelectLanguageActivity instance = null;
	CheckBox check_arabic;
	CheckBox check_english;
	public static boolean isMain = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.select_language, -1);
		ShowActionBarIcons(true, R.id.action_back, R.id.action_done);
		setContentView(R.layout.activity_select_language);
		check_english = findViewById(R.id.check_english);
		check_arabic = findViewById(R.id.check_arabic);
		findViewById(R.id.layout_english).setOnClickListener(this);
		findViewById(R.id.layout_arabic).setOnClickListener(this);
		initialize(AppPreference.getBool(AppPreference.KEY.LANGUAGE_ARABIC, false));
	}

	private void initialize(boolean isarabic) {
		check_english.setChecked(false);
		check_arabic.setChecked(false);
		if (isarabic) {
			check_arabic.setChecked(true);
		} else {
			check_english.setChecked(true);
		}
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.action_done:
				gotoNextActivity();
				return;
			case R.id.layout_arabic:
				initialize(true);
				return;
			case R.id.layout_english:
				initialize(false);
				return;
		}
	}

	private void gotoNextActivity() {
		AppPreference.setBool(AppPreference.KEY.LANGUAGE_ARABIC, check_arabic.isChecked());
		if (!isMain) {
			startActivity(new Intent(instance, MainActivity.class));
		} else {
			MainActivity.instance.recreate();
			CommonUtil.SetLocale(MainActivity.instance);
		}
		finish();
	}
}


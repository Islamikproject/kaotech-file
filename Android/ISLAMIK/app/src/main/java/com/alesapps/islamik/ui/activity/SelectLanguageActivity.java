package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import com.alesapps.islamik.AppPreference;
import com.alesapps.islamik.R;
import com.alesapps.islamik.utils.CommonUtil;

public class SelectLanguageActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static SelectLanguageActivity instance = null;
	CheckBox check_english;
	CheckBox check_arabic;
	CheckBox check_french;
	CheckBox check_bengali;
	CheckBox check_urdu;
	CheckBox check_spanish;
	public static boolean isMain = false;
	String selectedSymbol = "en";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.select_language, -1);
		if (isMain)
			ShowActionBarIcons(true, R.id.action_back, R.id.action_done);
		else
			ShowActionBarIcons(true, R.id.action_done);
		setContentView(R.layout.activity_select_language);
		check_english = findViewById(R.id.check_english);
		check_arabic = findViewById(R.id.check_arabic);
		check_french = findViewById(R.id.check_french);
		check_bengali = findViewById(R.id.check_bengali);
		check_urdu = findViewById(R.id.check_urdu);
		check_spanish = findViewById(R.id.check_spanish);
		findViewById(R.id.layout_english).setOnClickListener(this);
		findViewById(R.id.layout_arabic).setOnClickListener(this);
		findViewById(R.id.layout_french).setOnClickListener(this);
		findViewById(R.id.layout_bengali).setOnClickListener(this);
		findViewById(R.id.layout_urdu).setOnClickListener(this);
		findViewById(R.id.layout_spanish).setOnClickListener(this);
		initialize(AppPreference.getStr(AppPreference.KEY.LANGUAGE_SYMBOL, "en"));
	}

	private void initialize(String symbol) {
		selectedSymbol = symbol;
		check_english.setChecked(false);
		check_arabic.setChecked(false);
		check_french.setChecked(false);
		check_bengali.setChecked(false);
		check_urdu.setChecked(false);
		check_spanish.setChecked(false);
		if (selectedSymbol.equals("en")) {
			check_english.setChecked(true);
		} else if (selectedSymbol.equals("ar")){
			check_arabic.setChecked(true);
		} else if (selectedSymbol.equals("fr")){
			check_french.setChecked(true);
		} else if (selectedSymbol.equals("bn")){
			check_bengali.setChecked(true);
		} else if (selectedSymbol.equals("ur")){
			check_urdu.setChecked(true);
		} else if (selectedSymbol.equals("es")){
			check_spanish.setChecked(true);
		}
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.action_done:
				gotoNextActivity();
				return;
			case R.id.layout_english:
				initialize("en");
				return;
			case R.id.layout_arabic:
				initialize("ar");
				return;
			case R.id.layout_french:
				initialize("fr");
				return;
			case R.id.layout_bengali:
				initialize("bn");
				return;
			case R.id.layout_urdu:
				initialize("ur");
				return;
			case R.id.layout_spanish:
				initialize("es");
				return;
		}
	}

	private void gotoNextActivity() {
		AppPreference.setStr(AppPreference.KEY.LANGUAGE_SYMBOL, selectedSymbol);
		if (!isMain) {
			startActivity(new Intent(instance, LoginActivity.class));
		} else {
			MainActivity.instance.recreate();
			CommonUtil.SetLocale(MainActivity.instance);
		}
		finish();
	}
}


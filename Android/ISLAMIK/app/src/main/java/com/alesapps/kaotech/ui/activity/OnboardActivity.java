package com.alesapps.kaotech.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.alesapps.kaotech.AppPreference;
import com.alesapps.kaotech.R;

public class OnboardActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static OnboardActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle("", -1);
		ShowActionBarIcons(true, R.id.action_next);
		setContentView(R.layout.activity_onboard);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.action_next:
				AppPreference.setBool(AppPreference.KEY.AGREE, true);
				SelectLanguageActivity.isMain = false;
				startActivity(new Intent(instance, SelectLanguageActivity.class));
				break;
		}
	}
}


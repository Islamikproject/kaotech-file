package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.alesapps.islamik.AppPreference;
import com.alesapps.islamik.R;

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
				startActivity(new Intent(instance, TermsConditionActivity.class));
				break;
		}
	}
}


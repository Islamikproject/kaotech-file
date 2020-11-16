package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alesapps.islamik.AppPreference;
import com.alesapps.islamik.R;

public class PrivacyPolicyActivity extends BaseActionBarActivity {
	public static PrivacyPolicyActivity instance = null;
	TextView txt_content;
	Button btn_agree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;;
		SetTitle(R.string.privacy_policy, -1);
		ShowActionBarIcons(true, -1);
		setContentView(R.layout.activity_privacy_policy);
		txt_content = findViewById(R.id.txt_content);
		txt_content.setText(Html.fromHtml(getString(R.string.privacy_label)));
		txt_content.setMovementMethod(LinkMovementMethod.getInstance());
		btn_agree = findViewById(R.id.btn_agree);
		btn_agree.setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		if (AppPreference.getBool(AppPreference.KEY.AGREE, false)) {
			ShowActionBarIcons(true, R.id.action_back);
			btn_agree.setVisibility(View.GONE);
		} else {
			ShowActionBarIcons(true, -1);
			btn_agree.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.btn_agree:
				startActivity(new Intent(instance, TermsConditionActivity.class));
				finish();
				return;
		}
	}
}
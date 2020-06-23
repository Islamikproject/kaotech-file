package com.alesapps.islamikplus.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alesapps.islamikplus.AppPreference;
import com.alesapps.islamikplus.R;

public class TermsConditionActivity extends BaseActionBarActivity {
	public static TermsConditionActivity instance = null;
	TextView txt_content;
	Button btn_agree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;;
		SetTitle(R.string.terms_conditions, -1);
		setContentView(R.layout.activity_terms_condition);
		txt_content = findViewById(R.id.txt_content);
		txt_content.setText(Html.fromHtml(getString(R.string.terms_label)));
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
				AppPreference.setBool(AppPreference.KEY.AGREE, true);
				startActivity(new Intent(instance, LoginActivity.class));
				finish();
				return;
		}
	}
}
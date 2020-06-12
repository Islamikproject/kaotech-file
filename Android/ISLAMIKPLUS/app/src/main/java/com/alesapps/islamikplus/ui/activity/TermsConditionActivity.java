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

	public static int type = 0; // 0: about, 1: privacy, 2: terms
	String title = "";
	String content = "";
	Button btn_agree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;;
		setContentView(R.layout.activity_terms_condition);
		txt_content = findViewById(R.id.txt_content);
		btn_agree = findViewById(R.id.btn_agree);
		btn_agree.setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		if (type == 0) {
			title = getString(R.string.about_app);
			content = getString(R.string.about_label);
		} else if (type == 1){
			title = getString(R.string.privacy_policy);
			content = getString(R.string.privacy_label);
		} else if (type == 2){
			title = getString(R.string.terms_conditions);
			content = getString(R.string.terms_label);
		}
		SetTitle(title, -1);
		txt_content.setText(Html.fromHtml(content));
		txt_content.setMovementMethod(LinkMovementMethod.getInstance());
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
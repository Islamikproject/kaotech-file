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

public class TermsConditionActivity extends BaseActionBarActivity {
	public static TermsConditionActivity instance = null;
	TextView txt_content;
	Button btn_agree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;;
		SetTitle(R.string.terms_conditions, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_terms_condition);
		txt_content = findViewById(R.id.txt_content);
		txt_content.setText(Html.fromHtml(getString(R.string.terms_label)));
		txt_content.setMovementMethod(LinkMovementMethod.getInstance());
		btn_agree = findViewById(R.id.btn_agree);
		btn_agree.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.btn_agree:
				AppPreference.setBool(AppPreference.KEY.AGREE, true);
				SelectLanguageActivity.isMain = false;
				startActivity(new Intent(instance, SelectLanguageActivity.class));
				return;
		}
	}
}
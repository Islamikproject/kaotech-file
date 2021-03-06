package com.alesapps.islamikplus.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import com.alesapps.islamikplus.R;

public class TermsConditionActivity extends BaseActionBarActivity {
	public static TermsConditionActivity instance = null;
	TextView txt_content;
	TextView txt_content_a;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;;
		SetTitle(R.string.terms_conditions, -1);
		setContentView(R.layout.activity_terms_condition);
		ShowActionBarIcons(true, R.id.action_back);
		txt_content = findViewById(R.id.txt_content);
		txt_content_a = findViewById(R.id.txt_content_a);
		initialize();
	}

	private void initialize() {
		txt_content.setText(Html.fromHtml(getString(R.string.terms_label)));
		txt_content.setMovementMethod(LinkMovementMethod.getInstance());
		txt_content_a.setText(Html.fromHtml(getString(R.string.terms_label_a)));
		txt_content_a.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
package com.alesapps.islamikplus.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import com.alesapps.islamikplus.R;

public class AboutActivity extends BaseActionBarActivity {
	public static AboutActivity instance = null;
	TextView txt_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;;
		SetTitle(R.string.about_app, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_about);
		txt_content = findViewById(R.id.txt_content);
		txt_content.setText(Html.fromHtml(getString(R.string.about_label)));
		txt_content.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
package com.alesapps.islamik.ui.activity;

import android.os.Bundle;
import android.view.View;
import com.alesapps.islamik.R;

public class SermonActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static SermonActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.jumah_and_sermon, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_sermon);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		}
	}
}

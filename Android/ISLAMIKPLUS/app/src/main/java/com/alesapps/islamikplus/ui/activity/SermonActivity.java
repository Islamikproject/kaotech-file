package com.alesapps.islamikplus.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.utils.MessageUtil;

public class SermonActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static SermonActivity instance = null;
	EditText edt_topic;
	public static boolean isRegular = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		if (!isRegular)
			SetTitle(R.string.jumah_sermon, -1);
		else
			SetTitle(R.string.regular_sermon, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_sermon);
		edt_topic = findViewById(R.id.edt_topic);

		findViewById(R.id.btn_next).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.btn_next:
				if (isValid())
					startActivity(new Intent(instance, ReadyActivity.class));
				return;
		}
	}

	private boolean isValid() {
		String topic = edt_topic.getText().toString().trim();
		if (TextUtils.isEmpty(topic)) {
			MessageUtil.showError(instance, R.string.valid_No_topic);
			edt_topic.requestFocus();
			return false;
		}
		return true;
	}

	public void submit(String path) {

	}
}


package com.alesapps.islamikplus.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.alesapps.islamikplus.R;

public class ReadyActivity extends BaseActionBarActivity {
	public static ReadyActivity instance = null;

	TextView txt_count;
	Handler handler = new Handler();
	int count = 9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(null, 0);
		ShowActionBarIcons(true, -1);
		setContentView(R.layout.activity_ready);

		txt_count = findViewById(R.id.txt_count);
		handler.post(runnableCode);
	}

	private void initialize() {
		count --;
		if (count > 0) {
			txt_count.setText(String.valueOf(count));
		} else {
			startActivity(new Intent(instance, CameraActivity.class));
			finish();
		}
	}

	private Runnable runnableCode = new Runnable() {
		@Override
		public void run() {
			if (count > 0) {
				initialize();
				handler.postDelayed(runnableCode, 1000);
			}
		}
	};
}


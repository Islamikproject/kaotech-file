package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.R;
import com.alesapps.islamik.model.SurahModel;
import java.util.ArrayList;
import java.util.List;

public class ReadyActivity extends BaseActionBarActivity {
	public static ReadyActivity instance = null;

	TextView txt_count;
	Handler handler = new Handler();
	public static int type = AppConstant.TYPE_FAJR;
	public static String language = "en";
	public static List<SurahModel> mDataList = new ArrayList<>();
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
			if (type == AppConstant.TYPE_FAJR) {
				FajrDetailActivity.mDataList.clear();
				FajrDetailActivity.mDataList.addAll(mDataList);
				FajrDetailActivity.language = language;
				startActivity(new Intent(instance, FajrDetailActivity.class));
			} else if (type == AppConstant.TYPE_ZUHR || type == AppConstant.TYPE_ASR || type == AppConstant.TYPE_ISHA) {
				ZuhrDetailActivity.mDataList.clear();
				ZuhrDetailActivity.mDataList.addAll(mDataList);
				ZuhrDetailActivity.language = language;
				startActivity(new Intent(instance, ZuhrDetailActivity.class));
			} else if (type == AppConstant.TYPE_MAGHRIB) {
				MaghribDetailActivity.mDataList.clear();
				MaghribDetailActivity.mDataList.addAll(mDataList);
				MaghribDetailActivity.language = language;
				startActivity(new Intent(instance, MaghribDetailActivity.class));
			} else if (type == AppConstant.TYPE_QURAN) {
				QuranDetailActivity.mDataList.clear();
				QuranDetailActivity.mDataList.addAll(mDataList);
				QuranDetailActivity.language = language;
				startActivity(new Intent(instance, QuranDetailActivity.class));
			}
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


package com.alesapps.kaotech.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.alesapps.kaotech.AppConstant;
import com.alesapps.kaotech.AppGlobals;
import com.alesapps.kaotech.R;
import com.alesapps.kaotech.model.SurahModel;
import java.util.ArrayList;
import java.util.List;

public class QuranActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static QuranActivity instance;
	Spinner sp_language;
	Spinner sp_chapter;
	Spinner sp_verse_start;
	Spinner sp_verse_end;
	Spinner sp_reciter;
	Spinner sp_speed;

	String[] CHAPTER_ARRAY = new String[AppConstant.CHAPTER_ARRAY.length + 1];
	int[] VERSE_ARRAY = new int[AppConstant.VERSE_ARRAY.length + 1];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.quran, -1);
		ShowActionBarIcons(true, R.id.action_back, R.id.action_done);
		setContentView(R.layout.activity_quran);

		sp_language = findViewById(R.id.sp_language);
		sp_chapter = findViewById(R.id.sp_chapter);
		sp_verse_start = findViewById(R.id.sp_verse_start);
		sp_verse_end = findViewById(R.id.sp_verse_end);
		sp_reciter = findViewById(R.id.sp_reciter);
		sp_chapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				setVerses(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		sp_speed = findViewById(R.id.sp_speed);
		initialize();
	}

	private void initialize() {
		CHAPTER_ARRAY[0] = AppConstant.MAIN_CHAPTER;
		VERSE_ARRAY[0] = AppConstant.MAIN_VERSE;
		for (int i = 0; i < AppConstant.CHAPTER_ARRAY.length; i++) {
			CHAPTER_ARRAY[i + 1] = AppConstant.CHAPTER_ARRAY[i];
			VERSE_ARRAY[i + 1] = AppConstant.VERSE_ARRAY[i];
		}

		ArrayAdapter<String> adapterLanguage = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.LANGUAGE_ARRAY);
		sp_language.setAdapter(adapterLanguage);
		sp_language.setSelection(0);

		ArrayAdapter<String> adapterChapter = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, CHAPTER_ARRAY);
		sp_chapter.setAdapter(adapterChapter);
		sp_chapter.setSelection(0);
		setVerses(0);

		ArrayAdapter<String> adapterReciter = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.RECITER_ARRAY);
		sp_reciter.setAdapter(adapterReciter);
		sp_reciter.setSelection(0);

		ArrayAdapter<String> adapterSpeed = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.SPEED_ARRAY);
		sp_speed.setAdapter(adapterSpeed);
		sp_speed.setSelection(0);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.action_done:
				gotoNextActivity();
				break;
		}
	}

	private void setVerses(int position) {
		String[] verses = new String[getResources().getStringArray(VERSE_ARRAY[position]).length];;
		for (int i = 0; i < verses.length; i ++)
			verses[i] = String.valueOf(i+1);
		ArrayAdapter<String> adapterVerses = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, verses);
		sp_verse_start.setAdapter(adapterVerses);
		sp_verse_start.setSelection(0);
		sp_verse_end.setAdapter(adapterVerses);
		sp_verse_end.setSelection(verses.length - 1);
	}

	private void gotoNextActivity() {
		int language = sp_language.getSelectedItemPosition();
		int speed = sp_speed.getSelectedItemPosition();
		List<SurahModel> dataList = new ArrayList<>();
		dataList.add(SurahModel.GetSurahModel(language, sp_chapter.getSelectedItemPosition()-1,
				sp_verse_start.getSelectedItemPosition(), sp_verse_end.getSelectedItemPosition(), speed));
		ReadyActivity.type = AppConstant.TYPE_QURAN;
		ReadyActivity.mDataList.clear();
		ReadyActivity.mDataList.addAll(dataList);
		ReadyActivity.language = AppConstant.LANGUAGE_SYMBOL[language];
		AppGlobals.RECITERS_INDEX = sp_reciter.getSelectedItemPosition();
		startActivity(new Intent(instance, ReadyActivity.class));
	}
}



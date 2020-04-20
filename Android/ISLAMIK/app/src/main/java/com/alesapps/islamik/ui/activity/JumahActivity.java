package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.R;
import com.alesapps.islamik.model.SurahModel;
import com.alesapps.islamik.utils.MessageUtil;
import java.util.ArrayList;
import java.util.List;

public class JumahActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static JumahActivity instance;

	Spinner sp_language;
	Spinner sp_chapter_first;
	Spinner sp_verse_first_start;
	Spinner sp_verse_first_end;
	Spinner sp_chapter_second;
	Spinner sp_verse_second_start;
	Spinner sp_verse_second_end;
	Spinner sp_speed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.jumah, -1);
		ShowActionBarIcons(true, R.id.action_back, R.id.action_done);
		setContentView(R.layout.activity_jumah);

		sp_language = findViewById(R.id.sp_language);

		sp_chapter_first = findViewById(R.id.sp_chapter_first);
		sp_verse_first_start = findViewById(R.id.sp_verse_first_start);
		sp_verse_first_end = findViewById(R.id.sp_verse_first_end);

		sp_chapter_second = findViewById(R.id.sp_chapter_second);
		sp_verse_second_start = findViewById(R.id.sp_verse_second_start);
		sp_verse_second_end = findViewById(R.id.sp_verse_second_end);

		sp_speed = findViewById(R.id.sp_speed);

		sp_chapter_first.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				setVerses(sp_verse_first_start, sp_verse_first_end, position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		sp_chapter_second.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				setVerses(sp_verse_second_start, sp_verse_second_end, position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		initialize();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.action_done:
				if (isValid())
					gotoNextActivity();
				break;
		}
	}

	private void initialize() {
		ArrayAdapter<String> adapterLanguage = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.LANGUAGE_ARRAY);
		sp_language.setAdapter(adapterLanguage);
		sp_language.setSelection(0);

		setChapter(sp_chapter_first, sp_verse_first_start, sp_verse_first_end);
		setChapter(sp_chapter_second, sp_verse_second_start, sp_verse_second_end);

		ArrayAdapter<String> adapterSpeed = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.SPEED_ARRAY);
		sp_speed.setAdapter(adapterSpeed);
		sp_speed.setSelection(0);
	}

	private void setChapter(Spinner sp_chapter, Spinner sp_start, Spinner sp_end) {
		ArrayAdapter<String> adapterChapter = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.CHAPTER_ARRAY);
		sp_chapter.setAdapter(adapterChapter);
		sp_chapter.setSelection(0);
		setVerses(sp_start, sp_end, 0);
	}

	private void setVerses(Spinner sp_start, Spinner sp_end, int position) {
		String[] verses = new String[getResources().getStringArray(AppConstant.VERSE_ARRAY[position]).length];;
		for (int i = 0; i < verses.length; i ++)
			verses[i] = String.valueOf(i+1);
		ArrayAdapter<String> adapterVerses = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, verses);
		sp_start.setAdapter(adapterVerses);
		sp_start.setSelection(0);
		sp_end.setAdapter(adapterVerses);
		sp_end.setSelection(verses.length - 1);
	}

	private boolean isValid() {
		if (sp_verse_first_start.getSelectedItemPosition() > sp_verse_first_end.getSelectedItemPosition()) {
			MessageUtil.showError(instance, R.string.valid_Invalid_start_end);
			return false;
		}
		if (sp_verse_second_start.getSelectedItemPosition() > sp_verse_second_end.getSelectedItemPosition()) {
			MessageUtil.showError(instance, R.string.valid_Invalid_start_end);
			return false;
		}
		return true;
	}

	private void gotoNextActivity() {
		int language = sp_language.getSelectedItemPosition();
		int speed = sp_speed.getSelectedItemPosition();
		List<SurahModel> dataList = new ArrayList<>();
		dataList.add(SurahModel.GetSurahModel(language, sp_chapter_first.getSelectedItemPosition(),
				sp_verse_first_start.getSelectedItemPosition(), sp_verse_first_end.getSelectedItemPosition(), speed));
		dataList.add(SurahModel.GetSurahModel(language, sp_chapter_second.getSelectedItemPosition(),
				sp_verse_second_start.getSelectedItemPosition(), sp_verse_second_end.getSelectedItemPosition(), speed));
		ReadyActivity.type = AppConstant.TYPE_FAJR;
		ReadyActivity.mDataList.clear();
		ReadyActivity.mDataList.addAll(dataList);
		ReadyActivity.language = AppConstant.LANGUAGE_SYMBOL[language];
		startActivity(new Intent(instance, ReadyActivity.class));
	}
}

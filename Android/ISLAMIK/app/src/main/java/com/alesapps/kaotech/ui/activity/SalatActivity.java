package com.alesapps.kaotech.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.alesapps.kaotech.AppConstant;
import com.alesapps.kaotech.AppGlobals;
import com.alesapps.kaotech.R;
import com.alesapps.kaotech.model.SurahModel;
import com.alesapps.kaotech.utils.MessageUtil;
import java.util.ArrayList;
import java.util.List;

public class SalatActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static SalatActivity instance;

	TextView txt_witr;
	TextView txt_two;
	TextView txt_four;
	Spinner sp_language;
	Spinner sp_chapter_first;
	Spinner sp_verse_first_start;
	Spinner sp_verse_first_end;
	LinearLayout layout_second;
	Spinner sp_chapter_second;
	Spinner sp_verse_second_start;
	Spinner sp_verse_second_end;
	LinearLayout layout_third;
	LinearLayout layout_fourth;
	Spinner sp_speed;

	int type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.salat_layl_tahajjud, -1);
		ShowActionBarIcons(true, R.id.action_back, R.id.action_done);

		setContentView(R.layout.activity_salat);
		txt_witr = findViewById(R.id.txt_witr);
		txt_two = findViewById(R.id.txt_two);
		txt_four = findViewById(R.id.txt_four);

		sp_language = findViewById(R.id.sp_language);

		sp_chapter_first = findViewById(R.id.sp_chapter_first);
		sp_verse_first_start = findViewById(R.id.sp_verse_first_start);
		sp_verse_first_end = findViewById(R.id.sp_verse_first_end);

		layout_second = findViewById(R.id.layout_second);
		sp_chapter_second = findViewById(R.id.sp_chapter_second);
		sp_verse_second_start = findViewById(R.id.sp_verse_second_start);
		sp_verse_second_end = findViewById(R.id.sp_verse_second_end);

		layout_third = findViewById(R.id.layout_third);
		layout_fourth = findViewById(R.id.layout_fourth);

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

		txt_witr.setOnClickListener(this);
		txt_two.setOnClickListener(this);
		txt_four.setOnClickListener(this);
		setType(0);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.txt_witr:
				setType(0);
				break;
			case R.id.txt_two:
				setType(1);
				break;
			case R.id.txt_four:
				setType(2);
				break;
			case R.id.action_done:
				if (isValid())
					gotoNextActivity();
				break;
		}
	}

	private void setType(int index) {
		type = index;
		txt_witr.setBackgroundResource(R.drawable.bg_rectangle_white_line);
		txt_witr.setTextColor(getResources().getColor(R.color.white));
		txt_two.setBackgroundResource(R.drawable.bg_rectangle_white_line);
		txt_two.setTextColor(getResources().getColor(R.color.white));
		txt_four.setBackgroundResource(R.drawable.bg_rectangle_white_line);
		txt_four.setTextColor(getResources().getColor(R.color.white));
		if (type == 0) {
			txt_witr.setBackgroundColor(getResources().getColor(R.color.white));
			txt_witr.setTextColor(getResources().getColor(R.color.green));
		} else if (type == 1) {
			txt_two.setBackgroundColor(getResources().getColor(R.color.white));
			txt_two.setTextColor(getResources().getColor(R.color.green));
		} else if (type == 2) {
			txt_four.setBackgroundColor(getResources().getColor(R.color.white));
			txt_four.setTextColor(getResources().getColor(R.color.green));
		}
		initialize();
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

		layout_second.setVisibility(View.GONE);
		layout_third.setVisibility(View.GONE);
		layout_fourth.setVisibility(View.GONE);
		if (type == 1) {
			layout_second.setVisibility(View.VISIBLE);
		} else if (type == 2) {
			layout_second.setVisibility(View.VISIBLE);
			layout_third.setVisibility(View.VISIBLE);
			layout_fourth.setVisibility(View.VISIBLE);
		}
	}

	private void setChapter(Spinner sp_chapter, Spinner sp_start, Spinner sp_end) {
		sp_chapter.setEnabled(true);
		sp_start.setEnabled(true);
		sp_end.setEnabled(true);
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
		if ((type > 0) && (sp_verse_second_start.getSelectedItemPosition() > sp_verse_second_end.getSelectedItemPosition())) {
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
		ReadyActivity.type = AppConstant.TYPE_SALAT;
		if (type > 0) {
			ReadyActivity.type = AppConstant.TYPE_FAJR;
			dataList.add(SurahModel.GetSurahModel(language, sp_chapter_second.getSelectedItemPosition(),
					sp_verse_second_start.getSelectedItemPosition(), sp_verse_second_end.getSelectedItemPosition(), speed));
		}
		if (type == 2) {
			ReadyActivity.type = AppConstant.TYPE_ZUHR;
		}
		ReadyActivity.mDataList.clear();
		ReadyActivity.mDataList.addAll(dataList);
		ReadyActivity.language = AppConstant.LANGUAGE_SYMBOL[language];
		startActivity(new Intent(instance, ReadyActivity.class));
	}
}

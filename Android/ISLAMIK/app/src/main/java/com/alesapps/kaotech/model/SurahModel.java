package com.alesapps.kaotech.model;

import com.alesapps.kaotech.AppConstant;
import com.alesapps.kaotech.KaoTechApp;

public class SurahModel {
	public int language = 0;
	public int surahId = 0;
	public int verseStart = 0;
	public int verseEnd = 0;
	public String surah = "";
	public String verse = "";
	public int speed = 0;

	public static SurahModel GetSurahModel(int language, int id, int start, int end, int speed) {
		SurahModel model = new SurahModel();
		model.language = language;
		model.surahId = id;
		model.verseStart = start;
		model.verseEnd = end;
		model.speed = speed;
		if (id == -1) {
			model.surah = AppConstant.MAIN_CHAPTER;
		} else {
			model.surah = AppConstant.CHAPTER_ARRAY[id];
			String[] verseArray = KaoTechApp.getContext().getResources().getStringArray(AppConstant.VERSE_ARRAY[id]);
			String verse = "";
			for (int i = start; i <= end; i ++) {
				if (i == start)
					verse = verseArray[i];
				else
					verse = verse + "\n\n" + verseArray[i];
			}
			model.verse = verse;
		}
		return model;
	}
}


package com.alesapps.islamik.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.AppGlobals;
import com.alesapps.islamik.AppPreference;
import java.util.Locale;

public class CommonUtil {
	public static void SetLocale(Context context) {
		String lang = "en";
		if (AppPreference.getBool(AppPreference.KEY.LANGUAGE_ARABIC, false))
			lang = "ar";
		Locale myLocale = new Locale(lang);
		Resources res = context.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
	}

	public static void SetLocale(Context context, String lang) {
		Locale myLocale = new Locale(lang);
		Resources res = context.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
	}

	public static String GetStrMainVerse(Context context) {
		String[] verseArray = context.getResources().getStringArray(AppConstant.MAIN_VERSE);
		String verse = "";
		for (int i = 0; i < verseArray.length; i++) {
			if (i == 0)
				verse = verseArray[i];
			else
				verse = verse + "\n\n" + verseArray[i];
		}
		return verse;
	}

	public static String GetStrVerse(Context context, int id, int start, int end) {
		if (id == -1) {
			String[] verseArray = context.getResources().getStringArray(AppConstant.MAIN_VERSE);
			String verse = "";
			for (int i = start; i <= end; i++) {
				if (i == start)
					verse = verseArray[i];
				else
					verse = verse + "\n\n" + verseArray[i];
			}
			return verse;
		} else {
			String[] verseArray = context.getResources().getStringArray(AppConstant.VERSE_ARRAY[id]);
			String verse = "";
			for (int i = start; i <= end; i++) {
				if (i == start)
					verse = verseArray[i];
				else
					verse = verse + "\n\n" + verseArray[i];
			}
			return verse;
		}
	}

	public static Uri GetReciterPath(int chapter) {
		String path = AppConstant.RECITER_URL;
		if (AppGlobals.RECITERS_INDEX == 1)
			path = path + "azazy/";
		if (chapter < 10)
			path = path + "00" + chapter + ".mp3";
		else if (chapter < 100)
			path = path + "0" + chapter + ".mp3";
		else
			path = path + chapter + ".mp3";
		return Uri.parse(path);
	}
}

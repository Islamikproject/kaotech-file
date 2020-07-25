package com.alesapps.islamik.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.AppPreference;
import com.alesapps.islamik.IslamikApp;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
		if (chapter < 10)
			path = path + "00" + chapter + ".mp3";
		else if (chapter < 100)
			path = path + "0" + chapter + ".mp3";
		else
			path = path + chapter + ".mp3";
		return Uri.parse(path);
	}


	public static void hideKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static boolean isValidEmail(String emailAddr) {
		return !TextUtils.isEmpty(emailAddr) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddr).matches();
	}

	public static void SendEmail(Context context, String toEmail, String subject, String body, String attachment_url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri data = Uri.parse("mailto:"
				+ toEmail
				+ "?subject=" + subject + "&body=" + body +"");
		intent.setData(data);
		if (!TextUtils.isEmpty(attachment_url)) {
			Uri photoURI = Uri.fromFile(new File(attachment_url));
			intent.putExtra(Intent.EXTRA_STREAM, photoURI);
		}
		context.startActivity(intent);
	}

	public static void launchMarket() {
		Uri uri = Uri.parse("market://details?id=" + IslamikApp.getContext().getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			IslamikApp.getContext().startActivity(goToMarket);

		} catch (Exception e) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id=" + IslamikApp.getContext().getPackageName()));
			browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			IslamikApp.getContext().startActivity(browserIntent);
		}
	}


	public static String[] getAllLanguageCode() {
		Locale[] locales = Locale.getAvailableLocales();
		List<String> languageList = new ArrayList<>();
		String selectedLanguage = "";
		for (int i = 0; i < locales.length; i ++) {
			if (!selectedLanguage.equals(locales[i].getLanguage())) {
				languageList.add(locales[i].getLanguage());
				selectedLanguage = locales[i].getLanguage();
			}
		}
		String[] languages = new String[languageList.size()];
		for (int i = 0; i < languageList.size(); i ++) {
			languages[i] = languageList.get(i);
		}
		return languages;
	}

	public static String[] getAllLanguageName() {
		Locale[] locales = Locale.getAvailableLocales();
		List<String>languageList = new ArrayList<>();
		String selectedLanguage = "";
		for (int i = 0; i < locales.length; i ++) {
			if (!selectedLanguage.equals(locales[i].getLanguage())) {
				languageList.add(locales[i].getDisplayLanguage(locales[i]));
				selectedLanguage = locales[i].getLanguage();
			}
		}
		String[] languages = new String[languageList.size()];
		for (int i = 0; i < languageList.size(); i ++) {
			languages[i] = languageList.get(i);
		}
		return languages;
	}

	public static String getLanguageName(String code) {
		Locale locale = new Locale(code);
		return locale.getDisplayLanguage(locale);
	}
}

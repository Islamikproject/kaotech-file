package com.alesapps.islamikplus.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.alesapps.islamikplus.AppPreference;
import com.alesapps.islamikplus.IslamikPlusApp;
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
		Uri uri = Uri.parse("market://details?id=" + IslamikPlusApp.getContext().getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			IslamikPlusApp.getContext().startActivity(goToMarket);

		} catch (Exception e) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id=" + IslamikPlusApp.getContext().getPackageName()));
			browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			IslamikPlusApp.getContext().startActivity(browserIntent);
		}
	}

	public static String getStrDouble(double value) {
		return  String.format("%.2f", value);
	}

	public static String[] getAllLanguageCode() {
		Locale[] locales = Locale.getAvailableLocales();
		List<String>languageList = new ArrayList<>();
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

	public static String getImagePath(String path) {
		return path.replaceAll("https://parse.kaotech.org:20001/", "http://parse.kaotech.org:20002/");
	}
}

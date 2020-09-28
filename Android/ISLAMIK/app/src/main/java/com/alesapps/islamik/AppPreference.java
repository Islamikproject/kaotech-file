package com.alesapps.islamik;

import android.content.SharedPreferences;

public class AppPreference {
	private static SharedPreferences instance = null;

	public static class KEY {
		public static final String AGREE = "AGREE";
		public static final String LANGUAGE_SYMBOL = "LANGUAGE_SYMBOL";
		public static final String SIGN_IN_AUTO = "SIGN_IN_AUTO";
		public static final String PHONE_NUMBER = "PHONE_NUMBER";
		public static final String PASSWORD = "PASSWORD";
	}
	
	public static void initialize(SharedPreferences pref) {
		instance = pref;
	}
	
	// check contain
	public static boolean contains(String key) {
		return instance.contains(key);
	}
	
	// boolean
	public static boolean getBool(String key, boolean def) {
		return instance.getBoolean(key, def);
	}
	public static void setBool(String key, boolean value) {
		SharedPreferences.Editor editor = instance.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	// int
	public static int getInt(String key, int def) {
		return instance.getInt(key, def);
	}
	public static void setInt(String key, int value) {
		SharedPreferences.Editor editor = instance.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	// long
	public static long getLong(String key, long def) {
		return instance.getLong(key, def);
	}
	public static void setLong(String key, long value) {
		SharedPreferences.Editor editor = instance.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	// string
	public static String getStr(String key, String def) {
		return instance.getString(key, def);
	}
	public static void setStr(String key, String value) {
		SharedPreferences.Editor editor = instance.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	// remove
	public static void removeKey(String key) {
		SharedPreferences.Editor editor = instance.edit();
		editor.remove(key);
		editor.commit();
	}
}

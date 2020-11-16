package com.alesapps.islamik.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
	public static String DATE_STRING_FORMAT = "MMM dd, yyyy";
	public static String DATE_TIME_STRING_FORMAT = "MMM dd, yyyy HH:mm:ss";

	public static String dateToString(Date date, String strformat) {
		SimpleDateFormat format = new SimpleDateFormat(strformat);
		return format.format(date);
	}

	public static String convertTime(int time) {
		String strTime = String.valueOf(time);
		if (time < 10)
			strTime = "0" + time;
		return strTime;
	}

	public static Date getDateTime(Date date, int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		return calendar.getTime();
	}
}

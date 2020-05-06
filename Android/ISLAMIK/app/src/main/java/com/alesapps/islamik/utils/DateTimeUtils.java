package com.alesapps.islamik.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
	public static String DATE_STRING_FORMAT = "MMM dd, yyyy";

	public static String dateToString(Date date, String strformat) {
		SimpleDateFormat format = new SimpleDateFormat(strformat);
		return format.format(date);
	}
}
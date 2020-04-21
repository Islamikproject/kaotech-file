package com.alesapps.islamikplus.model;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class SermonModel {
	public static final int TYPE_JUMAH = 0;
	public static final int TYPE_REGULAR = 1;

	public int type = TYPE_JUMAH;
	public ParseUser owner;
	public String topic = "";
	public ParseFile video;

	public void parse(ParseObject object) {
		if (object == null)
			return;
		owner = object.getParseUser(ParseConstants.KEY_EMAIL_ADDRESS);
		topic = object.getString(ParseConstants.KEY_FIRST_NAME);
		type = object.getInt(ParseConstants.KEY_PASSWORD);
		video = object.getParseFile(ParseConstants.KEY_LAST_NAME);
	}



}

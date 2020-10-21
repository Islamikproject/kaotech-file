package com.alesapps.islamikplus.model;

import android.text.TextUtils;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.listener.ObjectListListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.List;

public class SermonModel {
	public static final int TYPE_JUMAH = 0;
	public static final int TYPE_REGULAR = 1;
	public static final int TYPE_RAISE = 2;

	public int type = TYPE_JUMAH;
	public ParseUser owner;
	public String topic = "";
	public String video = "";
	public String videoName = "";
	public String raiser = "";
	public String mosque = "";
	public Double amount = 0.0;
	public boolean isDelete = false;
	public String language = "en";
	public boolean isAudio = false;

	public void parse(ParseObject object) {
		if (object == null)
			return;
		owner = object.getParseUser(ParseConstants.KEY_OWNER);
		topic = object.getString(ParseConstants.KEY_TOPIC);
		type = object.getInt(ParseConstants.KEY_TYPE);
		video = object.getString(ParseConstants.KEY_VIDEO);
		videoName = object.getString(ParseConstants.KEY_VIDEO_NAME);
		raiser = object.getString(ParseConstants.KEY_RAISER);
		mosque = object.getString(ParseConstants.KEY_MOSQUE);
		amount = object.getDouble(ParseConstants.KEY_AMOUNT);
		isDelete = object.getBoolean(ParseConstants.KEY_IS_DELETE);
		language = object.getString(ParseConstants.KEY_LANGUAGE);
		isAudio = object.getBoolean(ParseConstants.KEY_IS_AUDIO);
	}

	public static void GetSermonList(final ParseUser userObj, final int type, String language, final ObjectListListener listener) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TBL_SERMON);
		query.whereEqualTo(ParseConstants.KEY_OWNER, userObj);
		query.whereEqualTo(ParseConstants.KEY_TYPE, type);
		query.whereNotEqualTo(ParseConstants.KEY_IS_DELETE, true);
		if (!TextUtils.isEmpty(language))
			query.whereEqualTo(ParseConstants.KEY_LANGUAGE, language);
		query.include(ParseConstants.KEY_OWNER);
		query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		query.setLimit(ParseConstants.QUERY_FETCH_MAX_COUNT);

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (listener != null)
					listener.done(objects, ParseErrorHandler.handle(e));
			}
		});
	}

	public static void Register(final SermonModel model, final ExceptionListener listener) {
		final ParseObject sermonObj = new ParseObject(ParseConstants.TBL_SERMON);
		sermonObj.put(ParseConstants.KEY_OWNER, ParseUser.getCurrentUser());
		sermonObj.put(ParseConstants.KEY_TYPE, model.type);
		sermonObj.put(ParseConstants.KEY_TOPIC, model.topic);
		sermonObj.put(ParseConstants.KEY_RAISER, model.raiser);
		sermonObj.put(ParseConstants.KEY_MOSQUE, model.mosque);
		sermonObj.put(ParseConstants.KEY_AMOUNT, model.amount);
		sermonObj.put(ParseConstants.KEY_IS_DELETE, model.isDelete);
		sermonObj.put(ParseConstants.KEY_LANGUAGE, model.language);
		sermonObj.put(ParseConstants.KEY_IS_AUDIO, model.isAudio);
		if (!TextUtils.isEmpty(model.video)) {
			sermonObj.put(ParseConstants.KEY_VIDEO, model.video);
			sermonObj.put(ParseConstants.KEY_VIDEO_NAME, model.videoName);
		}
		sermonObj.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if (listener != null)
					listener.done(ParseErrorHandler.handle(e));
			}
		});
	}

	public static void Delete(final ParseObject sermonObj, final ExceptionListener listener) {
		sermonObj.put(ParseConstants.KEY_IS_DELETE, true);
		sermonObj.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (listener != null)
					listener.done(ParseErrorHandler.handle(e));
			}
		});
	}
}

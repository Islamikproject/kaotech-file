package com.alesapps.islamik.model;

import android.text.TextUtils;
import com.alesapps.islamik.listener.ObjectListListener;
import com.alesapps.islamik.listener.ObjectListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.List;

public class SermonModel {
	public static final int TYPE_JUMAH = 0;
	public static final int TYPE_REGULAR = 1;
	public static final int TYPE_RAISE = 2;
	public static final int TYPE_ORDER = 3;

	public int type = TYPE_JUMAH;
	public ParseUser owner;
	public String topic = "";
	public String video = "";
	public String videoName = "";
	public String raiser = "";
	public String mosque = "";
	public Double amount = 0.0;
	public boolean isDelete = false;

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

	public static void GetLatestVideo(final ObjectListener listener) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TBL_SERMON);
		query.whereNotEqualTo(ParseConstants.KEY_IS_DELETE, true);
		query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		query.include(ParseConstants.KEY_OWNER);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null && objects.size() > 0) {
					if (listener != null)
						listener.done(checkAdmin(objects), ParseErrorHandler.handle(e));
				} else {
					if (listener != null)
						listener.done(null, ParseErrorHandler.handle(e));
				}
			}
		});
	}

	private static ParseObject checkAdmin(List<ParseObject> objects) {
		for (int i = 0; i < objects.size(); i ++) {
			int type = objects.get(i).getParseUser(ParseConstants.KEY_OWNER).getInt(ParseConstants.KEY_TYPE);
			if (type == UserModel.TYPE_ADMIN)
				return objects.get(i);
		}
		return null;
	}
}

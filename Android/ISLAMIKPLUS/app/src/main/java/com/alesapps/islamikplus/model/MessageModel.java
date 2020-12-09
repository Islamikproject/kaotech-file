package com.alesapps.islamikplus.model;

import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.listener.ObjectListListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.List;

public class MessageModel {
	public ParseObject sermon;
	public ParseUser owner;
	public ParseUser mosque;
	public String question = "";
	public String answer = "";
	public int rate = 0;

	public void parse(ParseObject object) {
		if (object == null)
			return;
		sermon = object.getParseObject(ParseConstants.KEY_SERMON);
		owner = object.getParseUser(ParseConstants.KEY_OWNER);
		mosque = object.getParseUser(ParseConstants.KEY_MOSQUE);
		question = object.getString(ParseConstants.KEY_QUESTION);
		answer = object.getString(ParseConstants.KEY_ANSWER);
		rate = object.getInt(ParseConstants.KEY_RATE);
	}

	public static void GetMessageList(final ObjectListListener listener) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TBL_MESSAGES);
		query.whereEqualTo(ParseConstants.KEY_MOSQUE, ParseUser.getCurrentUser());
		query.include(ParseConstants.KEY_SERMON);
		query.include(ParseConstants.KEY_MOSQUE);
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

	public static void RegisterAnswer(ParseObject messageObj, String answer, final ExceptionListener listener) {
		messageObj.put(ParseConstants.KEY_ANSWER, answer);
		messageObj.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (listener != null)
					listener.done(ParseErrorHandler.handle(e));
			}
		});
	}
}

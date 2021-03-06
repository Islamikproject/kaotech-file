package com.alesapps.islamik.model;

import com.alesapps.islamik.listener.ExceptionListener;
import com.alesapps.islamik.listener.ObjectListListener;
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
		query.whereEqualTo(ParseConstants.KEY_OWNER, ParseUser.getCurrentUser());
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

	public static void Register(MessageModel model, final ExceptionListener listener) {
		ParseObject messageObj = new ParseObject(ParseConstants.TBL_MESSAGES);
		messageObj.put(ParseConstants.KEY_SERMON, model.sermon);
		messageObj.put(ParseConstants.KEY_OWNER, model.owner);
		messageObj.put(ParseConstants.KEY_MOSQUE, model.mosque);
		messageObj.put(ParseConstants.KEY_QUESTION, model.question);
		messageObj.put(ParseConstants.KEY_ANSWER, model.answer);
		messageObj.put(ParseConstants.KEY_RATE, model.rate);

		messageObj.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (listener != null)
					listener.done(ParseErrorHandler.handle(e));
			}
		});
	}
}

package com.alesapps.islamikplus.model;

import com.alesapps.islamikplus.listener.ObjectListListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.List;

public class PaymentModel {
	public static final int TYPE_SERMON = 0;
	public static final int TYPE_RAISE = 2;
	public static final int TYPE_BASKET = 3;

	public int type = TYPE_SERMON;
	public ParseUser toUser;
	public ParseObject sermon;
	public String name = "";
	public String subject = "";
	public String email = "";
	public String text = "";
	public Double amount = 0.0;

	public void parse(ParseObject object) {
		if (object == null)
			return;
		type = object.getInt(ParseConstants.KEY_TYPE);
		toUser = object.getParseUser(ParseConstants.KEY_TO_USER);
		name = object.getString(ParseConstants.KEY_NAME);
		subject = object.getString(ParseConstants.KEY_SUBJECT);
		email = object.getString(ParseConstants.KEY_EMAIL);
		text = object.getString(ParseConstants.KEY_TEXT);
		sermon = object.getParseObject(ParseConstants.KEY_SERMON);
		amount = object.getDouble(ParseConstants.KEY_AMOUNT);
	}

	public static void GetPaymentList(final ParseUser userObj, final ObjectListListener listener) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TBL_PAYMENT);
		query.whereEqualTo(ParseConstants.KEY_TO_USER, userObj);
		query.include(ParseConstants.KEY_TO_USER);
		query.include(ParseConstants.KEY_SERMON);
		query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		query.setLimit(ParseConstants.QUERY_FETCH_MAX_COUNT);

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (listener != null)
					listener.done(objects, ParseErrorHandler.handle(e));
			}
		});
	}
}

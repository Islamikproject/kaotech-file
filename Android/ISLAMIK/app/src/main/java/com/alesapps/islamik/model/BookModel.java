package com.alesapps.islamik.model;

import com.alesapps.islamik.listener.ObjectListListener;
import com.alesapps.islamik.listener.ObjectListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookModel {
	public static int TYPE_ONE = 1;
	public static int TYPE_GROUP = 2;

	public int type = TYPE_ONE;
	public ParseUser owner;
	public ParseUser toUser;
	public List<String> childName = new ArrayList<>();
	public Date bookDate = Calendar.getInstance().getTime();
	public Double price = 0.0;

	public void parse(ParseObject object) {
		if (object == null)
			return;
		type = object.getInt(ParseConstants.KEY_TYPE);
		owner = object.getParseUser(ParseConstants.KEY_OWNER);
		toUser = object.getParseUser(ParseConstants.KEY_TO_USER);
		childName = object.getList(ParseConstants.KEY_CHILD_NAME);
		bookDate = object.getDate(ParseConstants.KEY_BOOK_DATE);
		price = object.getDouble(ParseConstants.KEY_PRICE);
	}

	public static void Register(BookModel model, final ObjectListener listener) {
		final ParseObject orderObj = new ParseObject(ParseConstants.TBL_BOOK);
		orderObj.put(ParseConstants.KEY_TYPE, model.type);
		orderObj.put(ParseConstants.KEY_OWNER, model.owner);
		orderObj.put(ParseConstants.KEY_TO_USER, model.toUser);
		orderObj.put(ParseConstants.KEY_CHILD_NAME, model.childName);
		orderObj.put(ParseConstants.KEY_BOOK_DATE, model.bookDate);
		orderObj.put(ParseConstants.KEY_PRICE, model.price);

		orderObj.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (listener != null)
					listener.done(orderObj, ParseErrorHandler.handle(e));
			}
		});
	}

	public static void GetBookList(final ParseUser userObj, final ObjectListListener listener) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TBL_BOOK);
		query.whereEqualTo(ParseConstants.KEY_OWNER, userObj);
		query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);

		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (listener != null)
					listener.done(objects, ParseErrorHandler.handle(e));
			}
		});
	}
}

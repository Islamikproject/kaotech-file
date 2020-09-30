package com.alesapps.islamik.model;

import com.alesapps.islamik.listener.ObjectListener;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class OrderModel {
	public int type = UserModel.TYPE_MOSQUE;
	public ParseUser owner;
	public String language = "en";
	public String name = "";
	public String subject = "";
	public String message = "";
	public Double amount = 0.0;
	public ParseUser toUser;

	public void parse(ParseObject object) {
		if (object == null)
			return;
		owner = object.getParseUser(ParseConstants.KEY_OWNER);
		type = object.getInt(ParseConstants.KEY_TYPE);
		language = object.getString(ParseConstants.KEY_LANGUAGE);
		name = object.getString(ParseConstants.KEY_NAME);
		subject = object.getString(ParseConstants.KEY_SUBJECT);
		message = object.getString(ParseConstants.KEY_MESSAGE);
		amount = object.getDouble(ParseConstants.KEY_AMOUNT);
		toUser = object.getParseUser(ParseConstants.KEY_TO_USER);
	}

	public static void Register(OrderModel model, final ObjectListener listener) {
		final ParseObject orderObj = new ParseObject(ParseConstants.TBL_ORDER);
		orderObj.put(ParseConstants.KEY_OWNER, model.owner);
		orderObj.put(ParseConstants.KEY_TYPE, model.type);
		orderObj.put(ParseConstants.KEY_LANGUAGE, model.language);
		orderObj.put(ParseConstants.KEY_NAME, model.name);
		orderObj.put(ParseConstants.KEY_SUBJECT, model.subject);
		orderObj.put(ParseConstants.KEY_MESSAGE, model.message);
		orderObj.put(ParseConstants.KEY_AMOUNT, model.amount);
		orderObj.put(ParseConstants.KEY_TO_USER, model.toUser);

		orderObj.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (listener != null)
					listener.done(orderObj, ParseErrorHandler.handle(e));
			}
		});
	}
}

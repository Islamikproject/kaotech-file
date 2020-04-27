package com.alesapps.islamik.model;

import com.alesapps.islamik.listener.ExceptionListener;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PaymentModel {
	public static final int TYPE_SERMON = 0;
	public static final int TYPE_RAISE = 2;
	public static final int TYPE_BASKET = 3;

	public int type = TYPE_SERMON;
	public ParseUser toUser;
	public ParseObject sermon;
	public String name = "";
	public Double amount = 0.0;
	public String chargeId = "";

	public void parse(ParseObject object) {
		if (object == null)
			return;
		type = object.getInt(ParseConstants.KEY_TYPE);
		toUser = object.getParseUser(ParseConstants.KEY_TO_USER);
		sermon = object.getParseObject(ParseConstants.KEY_SERMON);
		name = object.getString(ParseConstants.KEY_NAME);
		amount = object.getDouble(ParseConstants.KEY_AMOUNT);
		chargeId = object.getString(ParseConstants.KEY_CHARGE_ID);
	}

	public static void Register(final PaymentModel model, final ExceptionListener listener) {
		ParseObject mPaymentObj = new ParseObject(ParseConstants.TBL_PAYMENT);
		mPaymentObj.put(ParseConstants.KEY_TO_USER, model.toUser);
		mPaymentObj.put(ParseConstants.KEY_SERMON, model.sermon);
		mPaymentObj.put(ParseConstants.KEY_NAME, model.name);
		mPaymentObj.put(ParseConstants.KEY_AMOUNT, model.amount);
		mPaymentObj.put(ParseConstants.KEY_CHARGE_ID, model.chargeId);
		mPaymentObj.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (listener != null)
					listener.done(ParseErrorHandler.handle(e));
			}
		});
	}
}

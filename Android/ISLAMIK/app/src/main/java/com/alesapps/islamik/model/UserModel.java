package com.alesapps.islamik.model;

import com.alesapps.islamik.listener.UserListListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.List;

public class UserModel {
	public String username = "";
	public String email = "";
	public String password = "";
	public String emailAddress = "";
	public String firstName = "";
	public String lastName = "";
	public String phoneNumber = "";
	public String mosque = "";
	public ParseGeoPoint lonLat = new ParseGeoPoint();
	public String address = "";
	public String accountId = "";

	public void parse(ParseUser user) {
		if (user == null)
			return;
		username = user.getUsername();
		email = user.getUsername();
		emailAddress = user.getString(ParseConstants.KEY_EMAIL_ADDRESS);
		firstName = user.getString(ParseConstants.KEY_FIRST_NAME);
		lastName = user.getString(ParseConstants.KEY_LAST_NAME);
		password = user.getString(ParseConstants.KEY_PASSWORD);
		phoneNumber = user.getString(ParseConstants.KEY_PHONE_NUMBER);
		mosque = user.getString(ParseConstants.KEY_MOSQUE);
		lonLat = user.getParseGeoPoint(ParseConstants.KEY_LON_LAT);
		address = user.getString(ParseConstants.KEY_ADDRESS);
		accountId = user.getString(ParseConstants.KEY_ACCOUNT_ID);
	}

	public static void GetAllUsers(final UserListListener listener) {
		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		userQuery.whereExists(ParseConstants.KEY_FIRST_NAME);
		userQuery.setLimit(ParseConstants.QUERY_FETCH_MAX_COUNT);
		userQuery.orderByAscending(ParseConstants.KEY_FIRST_NAME);

		userQuery.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> users, ParseException e) {
				// TODO Auto-generated method stub
				if (listener != null)
					listener.done(users, ParseErrorHandler.handle(e));
			}
		});
	}

	public static String GetFullName(final ParseUser mUser) {
		return mUser.getString(ParseConstants.KEY_FIRST_NAME) + " " + mUser.getString(ParseConstants.KEY_LAST_NAME);
	}
}

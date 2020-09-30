package com.alesapps.islamikplus.model;

import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.listener.UserListener;
import com.alesapps.islamikplus.utils.BaseTask;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class UserModel {
	public static final int AVATAR_SIZE = 256;
	public static int TYPE_MOSQUE = 100;
	public static int TYPE_USER = 200;
	public static int TYPE_USTHADH = 300;
	public static int TYPE_INFLUENCER_WOMEN = 400;
	public static int TYPE_INFLUENCER_KID = 401;
	public static int TYPE_INFLUENCER_OTHER = 402;
	public static int TYPE_ADMIN = 500;
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
	public int type = TYPE_MOSQUE;
	public int price = 0;
	public int groupPrice = 0;
	public ParseFile avatar;

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
		type = user.getInt(ParseConstants.KEY_TYPE);
		price = user.getInt(ParseConstants.KEY_PRICE);
		groupPrice = user.getInt(ParseConstants.KEY_GROUP_PRICE);
		avatar = user.getParseFile(ParseConstants.KEY_AVATAR);
	}

	public static void RequestPasswordReset(String strEmail, final ExceptionListener listener) {
		ParseUser.requestPasswordResetInBackground(strEmail, new RequestPasswordResetCallback() {
			public void done(ParseException e) {
				if (listener != null)
					listener.done(e == null ? null : e.getMessage());
			}
		});
	}

	public static void Register(final UserModel model, final UserListener listener) {
		BaseTask.run(new BaseTask.TaskListener() {
			@Override
			public Object onTaskRunning(int taskId, Object data) {
				// TODO Auto-generated method stub
				try {
					if (model.avatar != null)
						model.avatar.save();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void onTaskResult(int taskId, Object result) {
				// TODO Auto-generated method stub
				final ParseUser userObj = new ParseUser();
				userObj.setUsername(model.username);
				userObj.setEmail(model.email);
				userObj.setPassword(model.password);
				userObj.put(ParseConstants.KEY_EMAIL_ADDRESS, model.emailAddress);
				userObj.put(ParseConstants.KEY_FIRST_NAME, model.firstName);
				userObj.put(ParseConstants.KEY_LAST_NAME, model.lastName);
				userObj.put(ParseConstants.KEY_PHONE_NUMBER, model.phoneNumber);
				userObj.put(ParseConstants.KEY_MOSQUE, model.mosque);
				userObj.put(ParseConstants.KEY_LON_LAT, model.lonLat);
				userObj.put(ParseConstants.KEY_ADDRESS, model.address);
				userObj.put(ParseConstants.KEY_ACCOUNT_ID, model.accountId);
				userObj.put(ParseConstants.KEY_TYPE, model.type);
				userObj.put(ParseConstants.KEY_PRICE, model.price);
				userObj.put(ParseConstants.KEY_GROUP_PRICE, model.groupPrice);
				if (model.avatar != null)
					userObj.put(ParseConstants.KEY_AVATAR, model.avatar);

				userObj.signUpInBackground(new SignUpCallback() {
					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							Login(model.username, model.password, new UserListener() {
								@Override
								public void done(ParseUser userObj, String error) {
									// TODO Auto-generated method stub
									if (listener != null)
										listener.done(userObj, error);
								}
							});
						} else {
							if (listener != null)
								listener.done(userObj, ParseErrorHandler.handle(e));
						}
					}
				});
			}
			@Override
			public void onTaskProgress(int taskId, Object... values) {}
			@Override
			public void onTaskPrepare(int taskId, Object data) {}
			@Override
			public void onTaskCancelled(int taskId) {}
		});
	}

	public static void Login(final String username, final String password, final UserListener listener) {
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			@Override
			public void done(final ParseUser userObj, ParseException e) {
				if (e == null) {
					ParseInstallation installation = ParseInstallation.getCurrentInstallation();
					installation.put(ParseConstants.KEY_OWNER, userObj);
					installation.put("GCMSenderId", "413068111620");
					installation.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if (listener != null)
								listener.done(userObj, ParseErrorHandler.handle(e));
						}
					});
				} else {
					if (listener != null)
						listener.done(null, ParseErrorHandler.handle(e));
				}
			}
		});
	}

	public static void Logout(final ExceptionListener listener) {
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.remove(ParseConstants.KEY_OWNER);
		installation.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException arg0) {
				// TODO Auto-generated method stub
				ParseUser.logOutInBackground(new LogOutCallback() {
					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							if (listener != null)
								listener.done(null);
						} else {
							if (listener != null)
								listener.done(ParseErrorHandler.handle(e));
						}
					}
				});
			}
		});
	}

	public static void GetUserFromPhone(final String phone, final UserListener listener) {
		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		userQuery.whereEqualTo(ParseConstants.KEY_USERNAME, phone);
		userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
			@Override
			public void done(ParseUser user, ParseException e) {
				// TODO Auto-generated method stub
				if (listener != null)
					listener.done(user, ParseErrorHandler.handle(e));
			}
		});
	}

	public static void GetUserFromEmail(final String email, final UserListener listener) {
		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		userQuery.whereEqualTo(ParseConstants.KEY_EMAIL_ADDRESS, email);
		userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
			@Override
			public void done(ParseUser user, ParseException e) {
				// TODO Auto-generated method stub
				if (listener != null)
					listener.done(user, ParseErrorHandler.handle(e));
			}
		});
	}

	public static String GetFullName(final ParseUser mUser) {
		return mUser.getString(ParseConstants.KEY_FIRST_NAME) + " " + mUser.getString(ParseConstants.KEY_LAST_NAME);
	}
}

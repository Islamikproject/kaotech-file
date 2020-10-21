package com.alesapps.islamikplus.push;

import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.ParseErrorHandler;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;

public class PushNoti {
	public static void sendPush(int type, ParseUser userObj, String message, String notiData, final ExceptionListener listener) {
		HashMap<String,String> params = new HashMap<String, String>();
		params.put(ParseConstants.KEY_USERNAME, userObj.getUsername());
		params.put(ParseConstants.NOTI_TYPE, String.valueOf(type));
		params.put(ParseConstants.NOTI_ALERT, message);
		params.put(ParseConstants.NOTI_DATA, notiData);
		params.put(ParseConstants.NOTI_BADGE, "Increment");
		params.put(ParseConstants.NOTI_SOUND, "cheering.caf");
		ParseCloud.callFunctionInBackground("SendPush", params, new FunctionCallback<Object>() {
			@Override
			public void done(Object object, ParseException e) {
				if (listener != null)
					listener.done(ParseErrorHandler.handle(e));
			}
		});
	}

	public static void sendPushAll(int type, String message, String notiData, final ExceptionListener listener) {
		HashMap<String,String> params = new HashMap<String, String>();
		params.put(ParseConstants.NOTI_ALERT, message);
		params.put(ParseConstants.NOTI_TYPE, String.valueOf(type));
		params.put(ParseConstants.NOTI_DATA, notiData);
		params.put(ParseConstants.NOTI_BADGE, "Increment");
		params.put(ParseConstants.NOTI_SOUND, "cheering.caf");
		ParseCloud.callFunctionInBackground("SendPushAll", params, new FunctionCallback<Object>() {
			@Override
			public void done(Object object, ParseException e) {
				if (listener != null)
					listener.done(ParseErrorHandler.handle(e));
			}
		});
	}
}

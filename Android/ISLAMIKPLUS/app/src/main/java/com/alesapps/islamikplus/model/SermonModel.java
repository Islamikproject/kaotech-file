package com.alesapps.islamikplus.model;

import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.listener.ObjectListListener;
import com.alesapps.islamikplus.utils.BaseTask;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.List;

public class SermonModel {
	public static final int TYPE_JUMAH = 0;
	public static final int TYPE_REGULAR = 1;
	public static final int TYPE_RAISE = 2;

	public int type = TYPE_JUMAH;
	public ParseUser owner;
	public String topic = "";
	public ParseFile video;
	public String raiser = "";
	public String mosque = "";
	public Double amount = 0.0;

	public void parse(ParseObject object) {
		if (object == null)
			return;
		owner = object.getParseUser(ParseConstants.KEY_OWNER);
		topic = object.getString(ParseConstants.KEY_TOPIC);
		type = object.getInt(ParseConstants.KEY_TYPE);
		video = object.getParseFile(ParseConstants.KEY_VIDEO);
		raiser = object.getString(ParseConstants.KEY_RAISER);
		mosque = object.getString(ParseConstants.KEY_MOSQUE);
		amount = object.getDouble(ParseConstants.KEY_AMOUNT);
	}

	public static void GetSermonList(final ParseUser userObj, final int type, final ObjectListListener listener) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TBL_SERMON);
		query.whereEqualTo(ParseConstants.KEY_OWNER, userObj);
		query.whereEqualTo(ParseConstants.KEY_TYPE, type);
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

	public static void Register(final SermonModel model, final ExceptionListener listener) {
		BaseTask.run(new BaseTask.TaskListener() {

			@Override
			public Object onTaskRunning(int taskId, Object data) {
				// TODO Auto-generated method stub
				try {
					if (model.video != null)
						model.video.save();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void onTaskResult(int taskId, Object result) {
				final ParseObject sermonObj = new ParseObject(ParseConstants.TBL_SERMON);
				sermonObj.put(ParseConstants.KEY_OWNER, ParseUser.getCurrentUser());
				sermonObj.put(ParseConstants.KEY_TYPE, model.type);
				sermonObj.put(ParseConstants.KEY_TOPIC, model.topic);
				sermonObj.put(ParseConstants.KEY_RAISER, model.raiser);
				sermonObj.put(ParseConstants.KEY_MOSQUE, model.mosque);
				sermonObj.put(ParseConstants.KEY_AMOUNT, model.amount);
				if (model.video != null)
					sermonObj.put(ParseConstants.KEY_VIDEO, model.video);
				sermonObj.saveInBackground(new SaveCallback() {
					public void done(ParseException e) {
						if (listener != null)
							listener.done(ParseErrorHandler.handle(e));
					}
				});
			}
			public void onTaskProgress(int taskId, Object... values) {}
			public void onTaskPrepare(int taskId, Object data) {}
			public void onTaskCancelled(int taskId) {}
		});
	}
}

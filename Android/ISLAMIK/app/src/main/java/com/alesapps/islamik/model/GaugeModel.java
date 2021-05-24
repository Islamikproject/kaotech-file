package com.alesapps.islamik.model;

import com.alesapps.islamik.listener.ExceptionListener;
import com.alesapps.islamik.listener.ObjectListListener;
import com.alesapps.islamik.utils.BaseTask;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.List;

public class GaugeModel {
	public ParseUser owner;
	public String description = "";
	public ParseFile photo;
	public String video = "";
	public String videoName = "";
	public String webLink = "";
	public int bgColor = 0;
	public int textColor = 0;
	public int textSize = 0;
	public int textFont = 0;

	public void parse(ParseObject object) {
		if (object == null)
			return;
		owner = object.getParseUser(ParseConstants.KEY_OWNER);
		video = object.getString(ParseConstants.KEY_VIDEO);
		videoName = object.getString(ParseConstants.KEY_VIDEO_NAME);
		description = object.getString(ParseConstants.KEY_DESCRIPTION);
		webLink = object.getString(ParseConstants.KEY_WEB_LINK);
		photo = object.getParseFile(ParseConstants.KEY_PHOTO);
		bgColor = object.getInt(ParseConstants.KEY_BG_COLOR);
		textColor = object.getInt(ParseConstants.KEY_TEXT_COLOR);
		textSize = object.getInt(ParseConstants.KEY_TEXT_SIZE);
		textFont = object.getInt(ParseConstants.KEY_TEXT_FONT);
	}

	public static void GetGaugeList(final ObjectListListener listener) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TBL_GAUGE);
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

	public static void Register(final GaugeModel model, final ExceptionListener listener) {
		BaseTask.run(new BaseTask.TaskListener() {

			@Override
			public Object onTaskRunning(int taskId, Object data) {
				// TODO Auto-generated method stub
				try {
					if (model.photo != null)
						model.photo.save();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void onTaskResult(int taskId, Object result) {
				final ParseObject gaugeObj = new ParseObject(ParseConstants.TBL_GAUGE);
				gaugeObj.put(ParseConstants.KEY_OWNER, ParseUser.getCurrentUser());
				gaugeObj.put(ParseConstants.KEY_DESCRIPTION, model.description);
				gaugeObj.put(ParseConstants.KEY_WEB_LINK, model.webLink);
				gaugeObj.put(ParseConstants.KEY_VIDEO, model.video);
				gaugeObj.put(ParseConstants.KEY_VIDEO_NAME, model.videoName);
				gaugeObj.put(ParseConstants.KEY_BG_COLOR, model.bgColor);
				gaugeObj.put(ParseConstants.KEY_TEXT_COLOR, model.textColor);
				gaugeObj.put(ParseConstants.KEY_TEXT_SIZE, model.textSize);
				gaugeObj.put(ParseConstants.KEY_TEXT_FONT, model.textFont);
				if (model.photo != null) {
					gaugeObj.put(ParseConstants.KEY_PHOTO, model.photo);
				}
				gaugeObj.saveInBackground(new SaveCallback() {
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

	public static void Delete(final ParseObject gaugeObj, final ExceptionListener listener) {
		gaugeObj.deleteInBackground(new DeleteCallback() {
			@Override
			public void done(ParseException e) {
				if (listener != null)
					listener.done(ParseErrorHandler.handle(e));
			}
		});
	}
}

package com.alesapps.islamikplus.model;

import com.alesapps.islamikplus.listener.ObjectListListener;
import com.alesapps.islamikplus.listener.ObjectListener;
import com.alesapps.islamikplus.utils.BaseTask;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.List;

public class ChatModel {
	public ParseUser sender;
	public ParseUser receiver;
	public String message;
	public boolean ismymessage;
	public String dateTime;
	public ParseObject bookObj;
	public ParseFile voiceFile;
	public ParseFile photo;
	public String video = "";

	public void parse(ParseObject object) {
		if (object == null)
			return;
		sender = object.getParseUser(ParseConstants.KEY_SENDER);
		receiver = object.getParseUser(ParseConstants.KEY_RECEIVER);
		message = object.getString(ParseConstants.KEY_MESSAGE);
		bookObj = object.getParseObject(ParseConstants.KEY_BOOK_OBJ);
		voiceFile = object.getParseFile(ParseConstants.KEY_VOICE_FILE);
		photo = object.getParseFile(ParseConstants.KEY_PHOTO);
		video = object.getString(ParseConstants.KEY_VIDEO);
	}

	public static void GetChatList(final ParseObject bookObj, final ObjectListListener listener) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TBL_CHAT);
		query.whereEqualTo(ParseConstants.KEY_BOOK_OBJ, bookObj);
		query.addAscendingOrder(ParseConstants.KEY_CREATED_AT);
		query.setLimit(ParseConstants.QUERY_FETCH_MAX_COUNT);
		query.include(ParseConstants.KEY_SENDER);
		query.include(ParseConstants.KEY_RECEIVER);
		query.include(ParseConstants.KEY_BOOK_OBJ);

		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
				if (listener != null)
					listener.done(objects, ParseErrorHandler.handle(e));
			}
		});
	}

	public static void sendMessage(final ChatModel model, final ObjectListener listener) {
		BaseTask.run(new BaseTask.TaskListener() {

			@Override
			public Object onTaskRunning(int taskId, Object data) {
				// TODO Auto-generated method stub
				try {
					if (model.voiceFile != null)
						model.voiceFile.save();
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
				// TODO Auto-generated method stub
				final ParseObject chatObj = new ParseObject(ParseConstants.TBL_CHAT);
				chatObj.put(ParseConstants.KEY_SENDER, ParseUser.getCurrentUser());
				chatObj.put(ParseConstants.KEY_RECEIVER, model.receiver);
				chatObj.put(ParseConstants.KEY_MESSAGE, model.message);
				chatObj.put(ParseConstants.KEY_BOOK_OBJ, model.bookObj);
				chatObj.put(ParseConstants.KEY_VIDEO, model.video);
				if (model.voiceFile != null)
					chatObj.put(ParseConstants.KEY_VOICE_FILE, model.voiceFile);
				if (model.photo != null)
					chatObj.put(ParseConstants.KEY_PHOTO, model.photo);

				chatObj.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						if (listener != null)
							listener.done(chatObj, ParseErrorHandler.handle(e));
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
}

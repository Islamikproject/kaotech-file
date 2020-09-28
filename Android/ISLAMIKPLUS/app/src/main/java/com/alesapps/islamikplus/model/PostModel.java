package com.alesapps.islamikplus.model;

import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.listener.ObjectListListener;
import com.alesapps.islamikplus.listener.ObjectListener;
import com.alesapps.islamikplus.utils.BaseTask;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.List;

public class PostModel {
    public ParseUser owner;
    public String title = "";
    public String description = "";
    public ParseFile photo;

    public void parse(ParseObject object) {
        if (object != null) {
            owner = object.getParseUser(ParseConstants.KEY_OWNER);
            title = object.getString(ParseConstants.KEY_TITLE);
            description = object.getString(ParseConstants.KEY_DESCRIPTION);
            photo = object.getParseFile(ParseConstants.KEY_PHOTO);
        }
    }

    public static void GetPostList(final ObjectListListener listener) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TBL_POST);
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

    public static void Register(final PostModel model, final ObjectListener listener) {
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
                final ParseObject postObj = new ParseObject(ParseConstants.TBL_POST);
                postObj.put(ParseConstants.KEY_OWNER, ParseUser.getCurrentUser());
                postObj.put(ParseConstants.KEY_TITLE, model.title);
                postObj.put(ParseConstants.KEY_DESCRIPTION, model.description);
                if (model.photo != null) {
                    postObj.put(ParseConstants.KEY_PHOTO, model.photo);
                }
                postObj.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (listener != null) {
                            listener.done(postObj, ParseErrorHandler.handle(e));
                        }
                    }
                });
            }
            public void onTaskProgress(int taskId, Object... values) {}
            public void onTaskPrepare(int taskId, Object data) {}
            public void onTaskCancelled(int taskId) {}
        });
    }

    public static void Delete(final ParseObject postObj, final ExceptionListener listener) {
        postObj.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (listener != null) {
                    listener.done(ParseErrorHandler.handle(e));
                }
            }
        });
    }
}

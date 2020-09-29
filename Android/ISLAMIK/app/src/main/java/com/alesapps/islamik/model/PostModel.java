package com.alesapps.islamik.model;

import com.alesapps.islamik.listener.ObjectListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

    public static void GetPost(final ObjectListener listener) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TBL_POST);
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (listener != null) {
                    listener.done(object, ParseErrorHandler.handle(e));
                }
            }
        });
    }
}

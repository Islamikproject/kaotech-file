package com.alesapps.islamikplus.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.alesapps.islamikplus.IslamikPlusApp;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.listener.ObjectListListener;
import com.alesapps.islamikplus.push.PushNoti;
import com.alesapps.islamikplus.ui.activity.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import org.json.JSONObject;
import java.util.List;

public class NotificationModel {
	public static int TYPE_BOOK = 0;

	public static int STATE_PENDING = 0;
	public static int STATE_ACCEPT = 1;
	public static int STATE_REJECT = 2;

	public ParseUser owner;
	public ParseUser toUser;
	public int type = TYPE_BOOK;
	public String message;
	public int state = STATE_PENDING;
	public ParseObject bookObj;

	public void parse(ParseObject object) {
		if (object == null)
			return;
		owner = object.getParseUser(ParseConstants.KEY_OWNER);
		toUser = object.getParseUser(ParseConstants.KEY_TO_USER);
		type = object.getInt(ParseConstants.KEY_TYPE);
		message = object.getString(ParseConstants.KEY_MESSAGE);
		state = object.getInt(ParseConstants.KEY_STATE);
		bookObj = object.getParseObject(ParseConstants.KEY_BOOK_OBJ);
	}

	public static void GetList(final ObjectListListener listener) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TBL_NOTIFICATION);
		query.whereEqualTo(ParseConstants.KEY_TO_USER, ParseUser.getCurrentUser());
		query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);

		query.include(ParseConstants.KEY_OWNER);
		query.include(ParseConstants.KEY_TO_USER);
		query.include(ParseConstants.KEY_BOOK_OBJ);
		query.setLimit(ParseConstants.QUERY_FETCH_MAX_COUNT);

		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException pe) {
				// TODO Auto-generated method stub
				if (listener != null)
					listener.done(objects, ParseErrorHandler.handle(pe));
			}
		});
	}

	public static void Register(final NotificationModel model, final ExceptionListener listener) {
		ParseObject notiObj = new ParseObject(ParseConstants.TBL_NOTIFICATION);
		notiObj.put(ParseConstants.KEY_OWNER, ParseUser.getCurrentUser());
		notiObj.put(ParseConstants.KEY_TO_USER, model.toUser);
		notiObj.put(ParseConstants.KEY_TYPE, model.type);
		notiObj.put(ParseConstants.KEY_MESSAGE, model.message);
		notiObj.put(ParseConstants.KEY_STATE, model.state);
		if (model.bookObj != null)
			notiObj.put(ParseConstants.KEY_BOOK_OBJ, model.bookObj);
		notiObj.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException pe) {
				// TODO Auto-generated method stub
				if (pe == null) {
					JSONObject object = new JSONObject();
					PushNoti.sendPush(model.type, model.toUser, model.message, object.toString(), null);
					if (listener != null)
						listener.done(ParseErrorHandler.handle(pe));
				} else {
					if (listener != null)
						listener.done(ParseErrorHandler.handle(pe));
				}
			}
		});
	}

	public static void UpdateState(final ParseObject notificationObj, final int state, final ExceptionListener listener) {
		notificationObj.put(ParseConstants.KEY_STATE, state);
		String meg = notificationObj.getString(ParseConstants.KEY_MESSAGE) + "\n" + IslamikPlusApp.getContext().getString(R.string.you_accepted_request);
		if (state == STATE_REJECT)
			meg = notificationObj.getString(ParseConstants.KEY_MESSAGE) + "\n" + IslamikPlusApp.getContext().getString(R.string.you_rejected_request);
		notificationObj.put(ParseConstants.KEY_MESSAGE, meg);
		notificationObj.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (ParseErrorHandler.handle(e) == null) {
					String message = String.format(IslamikPlusApp.getContext().getString(R.string.msg_notification_accepted), UserModel.GetFullName(ParseUser.getCurrentUser()));
					if (state == NotificationModel.STATE_REJECT)
						message = String.format(IslamikPlusApp.getContext().getString(R.string.msg_notification_declined), UserModel.GetFullName(ParseUser.getCurrentUser()), UserModel.GetFullName(notificationObj.getParseUser(ParseConstants.KEY_OWNER)));
					NotificationModel model = new NotificationModel();
					model.owner = ParseUser.getCurrentUser();
					model.toUser = notificationObj.getParseUser(ParseConstants.KEY_OWNER);
					model.type = NotificationModel.TYPE_BOOK;
					model.state = state;
					model.message = message;
					model.bookObj = notificationObj.getParseObject(ParseConstants.KEY_BOOK_OBJ);
					Register(model, listener);
				} else {
					if (listener != null)
						listener.done(ParseErrorHandler.handle(e));
				}
			}
		});
	}

	public static void showNotification(final int type, final String message) {
		if (TextUtils.isEmpty(message))
			return;

		Context context = IslamikPlusApp.getContext();

		NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.app_icon)
				.setContentTitle(IslamikPlusApp.getContext().getString(R.string.app_name))
				.setContentText(message)
				.setAutoCancel(true);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			notiBuilder.setSmallIcon(R.drawable.app_icon);
		} else {
			notiBuilder.setSmallIcon(R.drawable.app_icon);
		}
		Uri alarmSound = null;
		RingtoneManager ringtoneMgr = new RingtoneManager(context);
		ringtoneMgr.getCursor();

		if (alarmSound == null) {
			alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		}
		notiBuilder.setSound(alarmSound);

		boolean isVibrate = true;
		if (isVibrate) {
			long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
			notiBuilder.setVibrate(pattern);
		}

		Intent resultIntent = new Intent(context, MainActivity.class);;
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		if (resultIntent != null) {
			PendingIntent resultPendingIntent =	PendingIntent.getActivity(
					context,
					0,
					resultIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			notiBuilder.setContentIntent(resultPendingIntent);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			int notifyID = 1;
			String CHANNEL_ID = "islamik_channel_id";
			CharSequence name = "islamik_channel_name";
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
			Notification notification = new Notification.Builder(context)
					.setContentTitle(context.getString(R.string.app_name))
					.setContentText(message)
					.setSmallIcon(R.drawable.app_icon)
					.setChannelId(CHANNEL_ID)
					.build();
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.createNotificationChannel(mChannel);
			mNotificationManager.notify(notifyID , notification);
		} else {
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(0, notiBuilder.build());
		}
	}
}


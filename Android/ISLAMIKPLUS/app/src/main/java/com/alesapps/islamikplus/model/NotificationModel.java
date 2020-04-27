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
import com.alesapps.islamikplus.ui.activity.MainActivity;

public class NotificationModel {

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


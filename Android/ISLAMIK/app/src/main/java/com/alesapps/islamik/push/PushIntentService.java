package com.alesapps.islamik.push;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.alesapps.islamik.model.NotificationModel;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.ui.activity.ChatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class PushIntentService extends IntentService {

	public PushIntentService() {
		super("PushIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		String data = extras != null ? extras.getString("com.parse.Data") : "";
		try {
			if (!TextUtils.isEmpty(data)) {
				JSONObject jObject = new JSONObject(data);

				int type = jObject.getInt(ParseConstants.NOTI_TYPE);
				if (type == NotificationModel.TYPE_CHAT && ChatActivity.instance != null)
					ChatActivity.instance.refreshData();
				String message = jObject.getString(ParseConstants.NOTI_ALERT);
				NotificationModel.showNotification(type, message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PushReceiver.completeWakefulIntent(intent);
	}
}
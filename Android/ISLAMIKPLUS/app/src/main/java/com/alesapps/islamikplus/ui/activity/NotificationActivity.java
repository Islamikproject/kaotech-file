package com.alesapps.islamikplus.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.listener.ObjectListListener;
import com.alesapps.islamikplus.model.NotificationModel;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.ui.view.DragListView;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.DateTimeUtils;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActionBarActivity implements DragListView.OnRefreshLoadingMoreListener {
	public static NotificationActivity instance;
	DragListView list_notification;
	ListAdapter adapter;
	ArrayList<ParseObject> mDataList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.notification, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_notification);
		list_notification = findViewById(R.id.listView);
		adapter = new ListAdapter();
		list_notification.setAdapter(adapter);
		list_notification.setOnRefreshListener(this);
		list_notification.refresh();
	}

	@Override
	public void onDragRefresh() {
		getServerData();
	}

	@Override
	public void onDragLoadMore() {}

	private void getServerData() {
		NotificationModel.GetList(new ObjectListListener() {
			@Override
			public void done(List<ParseObject> objects, String error) {
				mDataList.clear();
				if (error == null && objects.size() > 0)
					mDataList.addAll(objects);
				showData();
			}
		});
	}

	private void showData() {
		adapter.notifyDataSetChanged();
		list_notification.onRefreshComplete();
	}


	class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			View layout_container;
			ImageView img_avatar;
			TextView txt_message;
			TextView txt_date;
			LinearLayout layout_accept;
			TextView txt_accept;
			TextView txt_reject;
			ImageView btn_call;
			ImageView btn_message;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(instance, R.layout.item_notification, null);

				holder = new ViewHolder();
				holder.layout_container = convertView.findViewById(R.id.layout_container);
				holder.img_avatar = convertView.findViewById(R.id.img_avatar);
				holder.txt_message = convertView.findViewById(R.id.txt_message);
				holder.txt_date = convertView.findViewById(R.id.txt_date);
				holder.layout_accept = convertView.findViewById(R.id.layout_accept);
				holder.txt_accept = convertView.findViewById(R.id.txt_accept);
				holder.txt_reject = convertView.findViewById(R.id.txt_reject);
				holder.btn_call = convertView.findViewById(R.id.btn_call);
				holder.btn_message = convertView.findViewById(R.id.btn_message);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.layout_accept.setVisibility(View.GONE);
			holder.btn_call.setVisibility(View.GONE);
			holder.btn_message.setVisibility(View.GONE);
			final NotificationModel model = new NotificationModel();
			model.parse(mDataList.get(position));
			holder.txt_message.setText(model.message);
			holder.txt_date.setText(DateTimeUtils.dateToString(mDataList.get(position).getCreatedAt(), DateTimeUtils.DATE_TIME_STRING_FORMAT));
			ParseFile avatar = model.owner.getParseFile(ParseConstants.KEY_AVATAR);
			if (avatar == null)
				holder.img_avatar.setBackgroundResource(R.drawable.default_profile);
			else
				Picasso.get().load(CommonUtil.getImagePath(avatar.getUrl())).into(holder.img_avatar);
			if (model.bookObj != null && model.state == NotificationModel.STATE_PENDING)
				holder.layout_accept.setVisibility(View.VISIBLE);
			if (model.type == NotificationModel.TYPE_BOOK && model.state == NotificationModel.STATE_ACCEPT) {
				holder.btn_call.setVisibility(View.VISIBLE);
				holder.btn_message.setVisibility(View.VISIBLE);
			}

			holder.txt_accept.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					updateState(mDataList.get(position), NotificationModel.STATE_ACCEPT);
				}
			});
			holder.txt_reject.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					updateState(mDataList.get(position), NotificationModel.STATE_REJECT);
				}
			});
			holder.btn_call.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ParseUser friendUser = model.owner;
					if (friendUser.getObjectId().equals(ParseUser.getCurrentUser().getObjectId()))
						friendUser = model.toUser;
					call(friendUser.getString(ParseConstants.KEY_PHONE_NUMBER));
				}
			});
			holder.btn_message.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ParseUser friendUser = model.owner;
					if (friendUser.getObjectId().equals(ParseUser.getCurrentUser().getObjectId()))
						friendUser = model.toUser;
					ChatActivity.mFriendObj = friendUser;
					ChatActivity.mBookObj = model.bookObj;
					startActivity(new Intent(instance, ChatActivity.class));
				}
			});
			return convertView;
		}
	}

	private void call(String phone) {
		if (!TextUtils.isEmpty(phone)) {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
			startActivity(intent);
		}
	}

	private void updateState(ParseObject notificationObj, int state) {
		dlg_progress.show();
		NotificationModel.UpdateState(notificationObj, state, new ExceptionListener() {
			@Override
			public void done(String error) {
				dlg_progress.cancel();
				if (error == null) {
					MessageUtil.showToast(instance, R.string.success);
					list_notification.refresh();
				} else {
					MessageUtil.showToast(instance, error);
				}
			}
		});
	}
}
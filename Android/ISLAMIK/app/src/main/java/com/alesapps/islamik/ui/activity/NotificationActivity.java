package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.ObjectListListener;
import com.alesapps.islamik.model.NotificationModel;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.ui.view.DragListView;
import com.alesapps.islamik.utils.CommonUtil;
import com.alesapps.islamik.utils.DateTimeUtils;
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
			ImageView btn_call;
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
				holder.btn_call = convertView.findViewById(R.id.btn_call);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.btn_call.setVisibility(View.GONE);
			final NotificationModel model = new NotificationModel();
			model.parse(mDataList.get(position));
			holder.txt_message.setText(model.message);
			holder.txt_date.setText(DateTimeUtils.dateToString(mDataList.get(position).getCreatedAt(), DateTimeUtils.DATE_TIME_STRING_FORMAT));
			ParseFile avatar = model.owner.getParseFile(ParseConstants.KEY_AVATAR);
			if (avatar == null)
				holder.img_avatar.setBackgroundResource(R.drawable.default_profile);
			else
				Picasso.get().load(CommonUtil.getImagePath(avatar.getUrl())).into(holder.img_avatar);
			if (model.type == NotificationModel.TYPE_BOOK && model.state == NotificationModel.STATE_ACCEPT)
				holder.btn_call.setVisibility(View.VISIBLE);

			holder.btn_call.setOnClickListener(new View.OnClickListener() {
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
}
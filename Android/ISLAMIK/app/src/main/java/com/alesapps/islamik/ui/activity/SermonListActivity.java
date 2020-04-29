package com.alesapps.islamik.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.ObjectListListener;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.SermonModel;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.ui.view.DragListView;
import com.alesapps.islamik.utils.DateTimeUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class SermonListActivity extends BaseActionBarActivity implements View.OnClickListener, DragListView.OnRefreshLoadingMoreListener {
	public static SermonListActivity instance;
	DragListView list_sermon;
	ListAdapter adapter;
	ArrayList<ParseObject> mDataList = new ArrayList<>();
	public static ParseUser mUserObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.app_name, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_sermon);
		list_sermon = findViewById(R.id.listView);
		adapter = new ListAdapter();
		list_sermon.setAdapter(adapter);
		list_sermon.setOnRefreshListener(this);
		list_sermon.refresh();
	}

	@Override
	public void onDragRefresh() {
		getServerData();
	}

	@Override
	public void onDragLoadMore() {}

	private void getServerData() {
		SermonModel.GetSermonList(mUserObj, new ObjectListListener() {
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
		list_sermon.onRefreshComplete();
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
			TextView txt_mosque;
			TextView txt_date;
			TextView txt_name;
			TextView txt_topic;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ListAdapter.ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(instance, R.layout.item_sermon, null);

				holder = new ListAdapter.ViewHolder();
				holder.layout_container = convertView.findViewById(R.id.layout_container);
				holder.txt_mosque = convertView.findViewById(R.id.txt_mosque);
				holder.txt_date = convertView.findViewById(R.id.txt_date);
				holder.txt_name = convertView.findViewById(R.id.txt_name);
				holder.txt_topic = convertView.findViewById(R.id.txt_topic);
				convertView.setTag(holder);

			} else {
				holder = (ListAdapter.ViewHolder) convertView.getTag();
			}
			final SermonModel model = new SermonModel();
			model.parse(mDataList.get(position));

			String mosque = model.mosque;
			String raiser = model.raiser;
			if (model.type < SermonModel.TYPE_RAISE) {
				mosque = model.owner.getString(ParseConstants.KEY_MOSQUE);
				raiser = UserModel.GetFullName(model.owner);
			}
			holder.txt_mosque.setText(mosque + " (" + raiser + ")");
			holder.txt_date.setText(DateTimeUtils.dateToString(mDataList.get(position).getCreatedAt(), DateTimeUtils.DATE_STRING_FORMAT));
			holder.txt_topic.setText(model.topic);

			holder.layout_container.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (model.type < SermonModel.TYPE_RAISE) {
						VideoActivity.mUser = mUserObj;
						VideoActivity.mSermonObj = mDataList.get(position);
						startActivity(new Intent(instance, VideoActivity.class));
					} else {
						showConfirmDialog(mDataList.get(position));
					}
				}
			});
			return convertView;
		}
	}

	private void showConfirmDialog(final ParseObject sermonObj) {
		String amount = String.valueOf(sermonObj.getDouble(ParseConstants.KEY_AMOUNT));
		if (amount.equals("0") || amount.equals("0.0"))
			amount = "";
		else
			amount = "$" + amount;
		String message = String.format(getString(R.string.confirm_mosque_virtual_basket), amount);
		new AlertDialog.Builder(instance)
				.setTitle(R.string.app_name)
				.setMessage(message)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						DonationActivity.mSermonObj = sermonObj;
						startActivity(new Intent(instance, DonationActivity.class));
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
	}
}
package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.UserListListener;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.SermonModel;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.ui.view.DragListView;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class SermonActivity extends BaseActionBarActivity implements DragListView.OnRefreshLoadingMoreListener {
	public static SermonActivity instance;
	DragListView list_sermon;
	ListAdapter adapter;
	ArrayList<ParseUser> mDataList = new ArrayList<>();
	public static int type = SermonModel.TYPE_JUMAH;
	public static int userType = UserModel.TYPE_USER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle("", -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_sermon);
		list_sermon = findViewById(R.id.listView);
		adapter = new ListAdapter();
		list_sermon.setAdapter(adapter);
		list_sermon.setOnRefreshListener(this);
		findViewById(R.id.btn_near).setOnClickListener(this);
		list_sermon.refresh();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub]
		super.onClick(view);
		switch (view.getId()) {
			case R.id.btn_near:
				MapActivity.mDataList.clear();
				MapActivity.mDataList.addAll(mDataList);
				MapActivity.type = type;
				startActivity(new Intent(instance, MapActivity.class));
				break;
		}
	}

	@Override
	public void onDragRefresh() {
		getServerData();
	}

	@Override
	public void onDragLoadMore() {}

	private void getServerData() {
		UserModel.GetUsersList(userType, new UserListListener() {
			@Override
			public void done(List<ParseUser> users, String error) {
				mDataList.clear();
				if (error == null && users.size() > 0) {
					if (userType == UserModel.TYPE_MOSQUE) {
						for (int i = 0; i < users.size(); i ++) {
							int _type = users.get(i).getInt(ParseConstants.KEY_TYPE);
							if (_type == UserModel.TYPE_MOSQUE || _type == UserModel.TYPE_ADMIN)
								mDataList.add(users.get(i));
						}
					} else {
						mDataList.addAll(users);
					}
				}
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
			TextView txt_name;
			TextView txt_address;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ListAdapter.ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(instance, R.layout.item_mosque, null);

				holder = new ListAdapter.ViewHolder();
				holder.layout_container = convertView.findViewById(R.id.layout_container);
				holder.txt_name = convertView.findViewById(R.id.txt_name);
				holder.txt_address = convertView.findViewById(R.id.txt_address);
				convertView.setTag(holder);

			} else {
				holder = (ListAdapter.ViewHolder) convertView.getTag();
			}
			UserModel model = new UserModel();
			model.parse(mDataList.get(position));
			holder.txt_name.setText(model.mosque);
			holder.txt_address.setText(model.address);

			holder.layout_container.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SermonListActivity.type = type;
					SermonListActivity.mUserObj = mDataList.get(position);
					startActivity(new Intent(instance, SermonListActivity.class));
				}
			});
			return convertView;
		}
	}
}
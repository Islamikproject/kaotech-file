package com.alesapps.islamikplus.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ObjectListListener;
import com.alesapps.islamikplus.model.PostModel;
import com.alesapps.islamikplus.ui.view.DragListView;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.DateTimeUtils;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class PostListActivity extends BaseActionBarActivity implements View.OnClickListener, DragListView.OnRefreshLoadingMoreListener {
	public static PostListActivity instance;
	public DragListView list_post;
	ListAdapter adapter;
	ArrayList<ParseObject> mDataList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.daily_post, -1);
		ShowActionBarIcons(true, R.id.action_back, R.id.action_add);
		setContentView(R.layout.activity_post_list);
		list_post = findViewById(R.id.listView);
		adapter = new ListAdapter();
		list_post.setAdapter(adapter);
		list_post.setOnRefreshListener(this);
		list_post.refresh();
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.action_add:
				PostActivity.mPostObj = null;
				startActivity(new Intent(instance, PostActivity.class));
				return;
		}
	}

	@Override
	public void onDragRefresh() {
		getServerData();
	}

	@Override
	public void onDragLoadMore() {}

	private void getServerData() {
		PostModel.GetPostList(new ObjectListListener() {
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
		list_post.onRefreshComplete();
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
			ImageView img_photo;
			TextView txt_title;
			TextView txt_date;
			TextView txt_description;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ListAdapter.ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(instance, R.layout.item_post, null);

				holder = new ListAdapter.ViewHolder();
				holder.layout_container = convertView.findViewById(R.id.layout_container);
				holder.img_photo = convertView.findViewById(R.id.img_photo);
				holder.txt_title = convertView.findViewById(R.id.txt_title);
				holder.txt_date = convertView.findViewById(R.id.txt_date);
				holder.txt_description = convertView.findViewById(R.id.txt_description);
				convertView.setTag(holder);

			} else {
				holder = (ListAdapter.ViewHolder) convertView.getTag();
			}
			PostModel model = new PostModel();
			model.parse(mDataList.get(position));
			holder.txt_title.setText(model.title);
			holder.txt_date.setText(DateTimeUtils.dateToString(mDataList.get(position).getCreatedAt(), DateTimeUtils.DATE_TIME_STRING_FORMAT));
			holder.txt_description.setText(model.description);
			if (model.photo != null)
				Picasso.get().load(CommonUtil.getImagePath(model.photo.getUrl())).into(holder.img_photo);
			holder.layout_container.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PostActivity.mPostObj = mDataList.get(position);
					startActivity(new Intent(instance, PostActivity.class));
				}
			});
			return convertView;
		}
	}
}
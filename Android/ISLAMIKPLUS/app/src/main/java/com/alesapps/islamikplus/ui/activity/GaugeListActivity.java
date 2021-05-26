package com.alesapps.islamikplus.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ObjectListListener;
import com.alesapps.islamikplus.model.GaugeModel;
import com.alesapps.islamikplus.ui.view.DragListView;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.DateTimeUtils;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class GaugeListActivity extends BaseActionBarActivity implements View.OnClickListener, DragListView.OnRefreshLoadingMoreListener {
	public static GaugeListActivity instance;
	public DragListView list_gauge;
	ListAdapter adapter;
	ArrayList<ParseObject> mDataList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.islamophobia_gauge, -1);
		ShowActionBarIcons(true, R.id.action_back, R.id.action_add);
		setContentView(R.layout.activity_gauge_list);
		list_gauge = findViewById(R.id.listView);
		adapter = new ListAdapter();
		list_gauge.setAdapter(adapter);
		list_gauge.setOnRefreshListener(this);
		list_gauge.refresh();
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.action_add:
				GaugeActivity.mGaugeObj = null;
				startActivity(new Intent(instance, GaugeActivity.class));
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
		GaugeModel.GetGaugeList(new ObjectListListener() {
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
		list_gauge.onRefreshComplete();
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
			TextView txt_text;
			TextView txt_date;
			TextView txt_link;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ListAdapter.ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(instance, R.layout.item_gauge, null);

				holder = new ListAdapter.ViewHolder();
				holder.layout_container = convertView.findViewById(R.id.layout_container);
				holder.img_photo = convertView.findViewById(R.id.img_photo);
				holder.txt_link = convertView.findViewById(R.id.txt_link);
				holder.txt_date = convertView.findViewById(R.id.txt_date);
				holder.txt_text = convertView.findViewById(R.id.txt_text);
				convertView.setTag(holder);

			} else {
				holder = (ListAdapter.ViewHolder) convertView.getTag();
			}
			GaugeModel model = new GaugeModel();
			model.parse(mDataList.get(position));
			if (!TextUtils.isEmpty(model.description)) {
				holder.txt_text.setText(model.description);
				holder.txt_text.setTextColor(getResources().getColor(AppConstant.ARRAY_COLOR[model.textColor]));
				holder.txt_text.setBackgroundColor(getResources().getColor(AppConstant.ARRAY_COLOR[model.bgColor]));
				holder.txt_text.setTextSize(Float.valueOf(AppConstant.ARRAY_STRING_SIZE[model.textSize]));
				Typeface face = ResourcesCompat.getFont(instance, AppConstant.ARRAY_FONT_VALUE[model.textFont]);
				holder.txt_text.setTypeface(face);
			}
			holder.txt_date.setText(DateTimeUtils.dateToString(mDataList.get(position).getCreatedAt(), DateTimeUtils.DATE_TIME_STRING_FORMAT));
			holder.txt_link.setText(model.webLink);
			if (!TextUtils.isEmpty(model.video))
				holder.img_photo.setImageResource(R.drawable.img_video);
			else
				holder.img_photo.setImageResource(R.drawable.default_image_bg);
			if (model.photo != null)
				Picasso.get().load(CommonUtil.getImagePath(model.photo.getUrl())).into(holder.img_photo);
			holder.layout_container.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					GaugeActivity.mGaugeObj = mDataList.get(position);
					startActivity(new Intent(instance, GaugeActivity.class));
				}
			});
			return convertView;
		}
	}
}
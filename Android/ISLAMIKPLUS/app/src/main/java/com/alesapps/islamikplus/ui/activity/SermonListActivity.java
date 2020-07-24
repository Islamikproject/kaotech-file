package com.alesapps.islamikplus.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ObjectListListener;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.SermonModel;
import com.alesapps.islamikplus.model.UserModel;
import com.alesapps.islamikplus.ui.view.DragListView;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.DateTimeUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class SermonListActivity extends BaseActionBarActivity implements View.OnClickListener, DragListView.OnRefreshLoadingMoreListener {
	public static SermonListActivity instance;
	Spinner sp_language;
	public DragListView list_sermon;
	ListAdapter adapter;
	ArrayList<ParseObject> mDataList = new ArrayList<>();
	public static int type = SermonModel.TYPE_JUMAH;
	String[] languageCode;
	String[] languageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		if (type == SermonModel.TYPE_JUMAH)
			SetTitle(R.string.jumah_sermon, -1);
		else
			SetTitle(R.string.regular_sermon, -1);
		ShowActionBarIcons(true, R.id.action_back, R.id.action_add);
		setContentView(R.layout.activity_sermon_list);
		sp_language = findViewById(R.id.sp_language);
		list_sermon = findViewById(R.id.listView);
		adapter = new ListAdapter();
		list_sermon.setAdapter(adapter);
		list_sermon.setOnRefreshListener(this);
		sp_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				list_sermon.refresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		initialize();
	}

	private void initialize() {
		String[] strLanguageCodes = CommonUtil.getAllLanguageCode();
		String[] strLanguageNames = CommonUtil.getAllLanguageName();
		languageCode = new String[strLanguageCodes.length + 1];
		languageName = new String[strLanguageNames.length + 1];
		languageCode[0] = "";
		languageName[0] = "";
		for (int i = 0; i < strLanguageCodes.length; i ++) {
			languageCode[i + 1] = strLanguageCodes[i];
			languageName[i + 1] = strLanguageNames[i];
		}
		ArrayAdapter<String> adapterLanguage = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, languageName);
		sp_language.setAdapter(adapterLanguage);
		sp_language.setSelection(0);
		list_sermon.refresh();
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.action_add:
				SermonActivity.type = type;
				startActivity(new Intent(instance, SermonActivity.class));
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
		SermonModel.GetSermonList(ParseUser.getCurrentUser(), type, languageCode[sp_language.getSelectedItemPosition()], new ObjectListListener() {
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
			SermonModel model = new SermonModel();
			model.parse(mDataList.get(position));

			String mosque = model.mosque;
			String raiser = model.raiser;
			if (model.type < SermonModel.TYPE_RAISE) {
				mosque = model.owner.getString(ParseConstants.KEY_MOSQUE);
				raiser = UserModel.GetFullName(model.owner);
			}
			holder.txt_mosque.setText(mosque + " (" + raiser + ")");
			holder.txt_date.setText(DateTimeUtils.dateToString(mDataList.get(position).getCreatedAt(), DateTimeUtils.DATE_STRING_FORMAT));
			holder.txt_topic.setText(CommonUtil.getLanguageName(model.language) + ": " + model.topic);

			holder.layout_container.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					VideoActivity.mSermonObj = mDataList.get(position);
					startActivity(new Intent(instance, VideoActivity.class));
				}
			});
			return convertView;
		}
	}
}
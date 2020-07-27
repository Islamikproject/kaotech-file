package com.alesapps.islamik.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.ObjectListListener;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.SermonModel;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.ui.view.DragListView;
import com.alesapps.islamik.utils.CommonUtil;
import com.alesapps.islamik.utils.DateTimeUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class SermonListActivity extends BaseActionBarActivity implements View.OnClickListener, DragListView.OnRefreshLoadingMoreListener {
	public static SermonListActivity instance;
	TextView txt_jumah;
	TextView txt_regular;
	Spinner sp_language;
	DragListView list_sermon;
	ListAdapter adapter;
	ArrayList<ParseObject> mDataList = new ArrayList<>();
	public static ParseUser mUserObj;
	int type = SermonModel.TYPE_JUMAH;
	String[] languageCode;
	String[] languageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(mUserObj.getString(ParseConstants.KEY_MOSQUE), -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_sermon_list);
		txt_jumah = findViewById(R.id.txt_jumah);
		txt_regular = findViewById(R.id.txt_regular);
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
		txt_regular.setOnClickListener(this);
		txt_jumah.setOnClickListener(this);
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
		setType(SermonModel.TYPE_JUMAH);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.txt_jumah:
				setType(SermonModel.TYPE_JUMAH);
				return;
			case R.id.txt_regular:
				setType(SermonModel.TYPE_REGULAR);
				break;
		}
	}

	private void setType(int index) {
		type = index;
		txt_jumah.setBackgroundResource(R.drawable.bg_rectangle_white_line);
		txt_jumah.setTextColor(getResources().getColor(R.color.white));
		txt_regular.setBackgroundResource(R.drawable.bg_rectangle_white_line);
		txt_regular.setTextColor(getResources().getColor(R.color.white));
		if (type == SermonModel.TYPE_JUMAH) {
			txt_jumah.setBackgroundColor(getResources().getColor(R.color.white));
			txt_jumah.setTextColor(getResources().getColor(R.color.green));
		} else if (type == SermonModel.TYPE_REGULAR) {
			txt_regular.setBackgroundColor(getResources().getColor(R.color.white));
			txt_regular.setTextColor(getResources().getColor(R.color.green));
		}
		list_sermon.refresh();
	}

	@Override
	public void onDragRefresh() {
		getServerData();
	}

	@Override
	public void onDragLoadMore() {}

	private void getServerData() {
		SermonModel.GetSermonList(mUserObj, type, languageCode[sp_language.getSelectedItemPosition()], new ObjectListListener() {
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
						String url = AppConstant.STRIPE_CONNECT_URL + sermonObj.getObjectId();
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						startActivity(browserIntent);
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
	}
}
package com.alesapps.islamikplus.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ObjectListListener;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.PaymentModel;
import com.alesapps.islamikplus.ui.activity.DonationActivity;
import com.alesapps.islamikplus.ui.view.DragListView;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.alesapps.islamikplus.utils.DateTimeUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class AmountFragment extends BaseFragment implements DragListView.OnRefreshLoadingMoreListener {
	public static AmountFragment instance;
	DragListView list_donation;
	LinearLayout layout_nodata;
	TextView txt_total;
	ListAdapter adapter;
	ArrayList<ParseObject> mDataList = new ArrayList<>();
	DonationActivity mActivity;

	public static AmountFragment newInstance() {
		AmountFragment fragment = new AmountFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		mActivity = DonationActivity.instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_amount, container, false);
		txt_total = mView.findViewById(R.id.txt_total);
		layout_nodata = mView.findViewById(R.id.layout_nodata);
		list_donation = mView.findViewById(R.id.listView);
		adapter = new ListAdapter();
		list_donation.setAdapter(adapter);
		layout_nodata.setOnClickListener(this);
		list_donation.setOnRefreshListener(this);
		txt_total.setText("$0.00");
		list_donation.refresh();
		return mView;
	}

	@Override
	public void onDragRefresh() {
		layout_nodata.setVisibility(View.GONE);
		getServerData();
	}

	@Override
	public void onDragLoadMore() {}

	private void getServerData() {
		PaymentModel.GetPaymentList(ParseUser.getCurrentUser(), new ObjectListListener() {
			@Override
			public void done(List<ParseObject> objects, String error) {
				mDataList.clear();
				if (error == null && objects.size() > 0) {
					Double total = 0.0;
					for (int i = 0; i < objects.size(); i ++) {
						total = total + objects.get(i).getDouble(ParseConstants.KEY_AMOUNT);
						mDataList.add(objects.get(i));
					}
					txt_total.setText("$" + CommonUtil.getStrDouble(total));
				}
				showData();
			}
		});
	}

	private void showData() {
		if (mDataList.size() > 0)
			layout_nodata.setVisibility(View.GONE);
		else
			layout_nodata.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();
		list_donation.onRefreshComplete();
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
			TextView txt_amount;
			TextView txt_date;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ListAdapter.ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_amount, null);

				holder = new ListAdapter.ViewHolder();
				holder.layout_container = convertView.findViewById(R.id.layout_container);
				holder.txt_name = convertView.findViewById(R.id.txt_name);
				holder.txt_amount = convertView.findViewById(R.id.txt_amount);
				holder.txt_date = convertView.findViewById(R.id.txt_date);
				convertView.setTag(holder);

			} else {
				holder = (ListAdapter.ViewHolder) convertView.getTag();
			}
			PaymentModel model = new PaymentModel();
			model.parse(mDataList.get(position));
			holder.txt_name.setText(model.name);
			holder.txt_amount.setText("$" + CommonUtil.getStrDouble(model.amount));
			holder.txt_date.setText(DateTimeUtils.dateToString(mDataList.get(position).getCreatedAt(), DateTimeUtils.DATE_STRING_FORMAT));
			return convertView;
		}
	}
}
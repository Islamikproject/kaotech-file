package com.alesapps.islamik.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.ObjectListener;
import com.alesapps.islamik.listener.UserListListener;
import com.alesapps.islamik.model.OrderModel;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.ui.view.DragListView;
import com.alesapps.islamik.utils.CommonUtil;
import com.alesapps.islamik.utils.MessageUtil;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class DonationActivity extends BaseActionBarActivity implements View.OnClickListener, DragListView.OnRefreshLoadingMoreListener {
	public static DonationActivity instance;
	RadioButton rb_mosque;
	RadioButton rb_scholars;
	RadioButton rb_influencers;
	EditText edt_amount;
	DragListView list_user;
	ListAdapter adapter;

	ArrayList<ParseUser> mServerDataList = new ArrayList<>();
	ArrayList<ParseUser> mDataList = new ArrayList<>();
	ParseUser mSelectedUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.charity_donation, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_donation);
		rb_mosque = findViewById(R.id.rb_mosque);
		rb_scholars = findViewById(R.id.rb_scholars);
		rb_influencers = findViewById(R.id.rb_influencers);
		edt_amount = findViewById(R.id.edt_amount);
		list_user = findViewById(R.id.listView);
		adapter = new ListAdapter();
		list_user.setAdapter(adapter);
		list_user.setOnRefreshListener(this);
		findViewById(R.id.rb_mosque).setOnClickListener(this);
		findViewById(R.id.rb_scholars).setOnClickListener(this);
		findViewById(R.id.rb_influencers).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		list_user.refresh();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.rb_mosque:
			case R.id.rb_scholars:
			case R.id.rb_influencers:
				showData();
				break;
			case R.id.btn_submit:
				if (isValid())
					submit();
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
		UserModel.GetUsersList(UserModel.TYPE_MOSQUE, new UserListListener() {
			@Override
			public void done(List<ParseUser> users, String error) {
				mServerDataList.clear();
				if (error == null && users.size() > 0)
					mServerDataList.addAll(users);
				showData();
			}
		});
	}

	private void showData() {
		mDataList.clear();
		mSelectedUser = null;
		int type = UserModel.TYPE_MOSQUE;
		if (rb_scholars.isChecked())
			type = UserModel.TYPE_USTHADH;
		else if (rb_mosque.isChecked())
			type = UserModel.TYPE_INFLUENCER_WOMEN;
		for (int i = 0; i < mServerDataList.size(); i++) {
			int _type = mServerDataList.get(i).getInt(ParseConstants.KEY_TYPE);
			if (_type == UserModel.TYPE_ADMIN || _type == type || (type == UserModel.TYPE_INFLUENCER_WOMEN && _type >= UserModel.TYPE_INFLUENCER_WOMEN))
				mDataList.add(mServerDataList.get(i));
		}
		adapter.notifyDataSetChanged();
		list_user.onRefreshComplete();
	}

	private boolean isValid() {
		String amount = edt_amount.getText().toString().trim();
		if (mSelectedUser == null) {
			MessageUtil.showError(instance, R.string.valid_No_mosque);
			return false;
		}
		if (TextUtils.isEmpty(amount)) {
			MessageUtil.showError(instance, R.string.valid_No_amount);
			return false;
		}
		return true;
	}

	private void submit() {
		OrderModel model = new OrderModel();
		model.owner = ParseUser.getCurrentUser();
		model.language = "";
		model.type = OrderModel.TYPE_DONATION;
		model.toUser = mSelectedUser;
		model.name = UserModel.GetFullName(ParseUser.getCurrentUser());
		model.subject = getString(R.string.charity_donation);
		model.message = "";
		model.amount = Double.valueOf(edt_amount.getText().toString().trim());
		dlg_progress.show();
		OrderModel.Register(model, new ObjectListener() {
			@Override
			public void done(ParseObject object, String error) {
				dlg_progress.cancel();
				if (error == null && object != null) {
					MessageUtil.showToast(instance, R.string.success);
					gotoDonation(object.getObjectId());
				} else {
					MessageUtil.showToast(instance, error);
				}
			}
		});
	}

	private void gotoDonation(String orderId) {
		new AlertDialog.Builder(instance)
				.setTitle(R.string.app_name)
				.setMessage(R.string.choose_payment_method)
				.setPositiveButton(R.string.stripe, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String url = AppConstant.STRIPE_CONNECT_URL + "order?order=" + orderId + "&amount=" + String.format("%.2f", Double.valueOf(edt_amount.getText().toString().trim()));
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						startActivity(browserIntent);
					}
				})
				.setNegativeButton(R.string.google_pay, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
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
			TextView txt_name;
			TextView txt_address;
			CheckBox check_select;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(instance, R.layout.item_mosque, null);

				holder = new ViewHolder();
				holder.layout_container = convertView.findViewById(R.id.layout_container);
				holder.img_avatar = convertView.findViewById(R.id.img_avatar);
				holder.txt_name = convertView.findViewById(R.id.txt_name);
				holder.txt_address = convertView.findViewById(R.id.txt_address);
				holder.check_select = convertView.findViewById(R.id.check_select);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			UserModel model = new UserModel();
			model.parse(mDataList.get(position));
			holder.txt_name.setText(model.mosque);
			holder.txt_address.setText(model.address);
			holder.check_select.setChecked(false);
			if (model.avatar == null)
				holder.img_avatar.setBackgroundResource(R.drawable.default_profile);
			else
				Picasso.get().load(CommonUtil.getImagePath(model.avatar.getUrl())).into(holder.img_avatar);
			if (mSelectedUser != null && mSelectedUser.getObjectId().equals(mDataList.get(position).getObjectId()))
				holder.check_select.setChecked(true);

			holder.layout_container.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mSelectedUser = mDataList.get(position);
					adapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}
	}
}
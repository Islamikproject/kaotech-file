package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
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

public class OrderActivity extends BaseActionBarActivity implements View.OnClickListener, DragListView.OnRefreshLoadingMoreListener {
	public static OrderActivity instance;
	Spinner sp_language;
	RadioButton rb_mosque;
	RadioButton rb_usthadh;
	EditText edt_name;
	EditText edt_subject;
	EditText edt_message;
	EditText edt_amount;
	DragListView list_user;
	ListAdapter adapter;
	ArrayList<ParseUser> mServerDataList = new ArrayList<>();
	ArrayList<ParseUser> mDataList = new ArrayList<>();
	int type = UserModel.TYPE_MOSQUE;
	ParseUser mSelectedUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.order_prayer, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_order);
		sp_language = findViewById(R.id.sp_language);
		rb_mosque = findViewById(R.id.rb_mosque);
		rb_usthadh = findViewById(R.id.rb_usthadh);
		edt_name = findViewById(R.id.edt_name);
		edt_subject = findViewById(R.id.edt_subject);
		edt_message = findViewById(R.id.edt_message);
		edt_amount = findViewById(R.id.edt_amount);
		list_user = findViewById(R.id.listView);
		adapter = new ListAdapter();
		list_user.setAdapter(adapter);
		list_user.setOnRefreshListener(this);
		findViewById(R.id.rb_mosque).setOnClickListener(this);
		findViewById(R.id.rb_usthadh).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		ArrayAdapter<String> adapterLanguage = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.LANGUAGE_ARRAY);
		sp_language.setAdapter(adapterLanguage);
		sp_language.setSelection(0);
		rb_mosque.setChecked(true);
		edt_name.setText("");
		edt_subject.setText("");
		edt_message.setText("");
		edt_amount.setText("");
		list_user.refresh();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.rb_mosque:
				type = UserModel.TYPE_MOSQUE;
				showData();
				break;
			case R.id.rb_usthadh:
				type = UserModel.TYPE_USTHADH;
				showData();
				break;
			case R.id.btn_submit:
				gotoDonation("Otyx88TL3C");
//				if (isValid())
//					submit();
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
		for (int i = 0; i < mServerDataList.size(); i++) {
			int _type = mServerDataList.get(i).getInt(ParseConstants.KEY_TYPE);
			if (_type == type)
				mDataList.add(mServerDataList.get(i));
			else if (_type == UserModel.TYPE_ADMIN && type == UserModel.TYPE_MOSQUE)
				mDataList.add(mServerDataList.get(i));
		}
		adapter.notifyDataSetChanged();
		list_user.onRefreshComplete();
	}

	private boolean isValid() {
		String name = edt_name.getText().toString().trim();
		String subject = edt_subject.getText().toString().trim();
		String message = edt_message.getText().toString().trim();
		String amount = edt_amount.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			MessageUtil.showError(instance, R.string.valid_No_name);
			edt_name.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(subject)) {
			MessageUtil.showError(instance, R.string.valid_No_subject);
			edt_subject.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(message)) {
			MessageUtil.showError(instance, R.string.valid_No_message);
			edt_message.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(amount)) {
			MessageUtil.showError(instance, R.string.valid_No_amount);
			edt_amount.requestFocus();
			return false;
		}
		if (mSelectedUser == null && rb_mosque.isChecked()) {
			MessageUtil.showError(instance, R.string.valid_No_mosque);
			return false;
		}
		if (mSelectedUser == null && rb_usthadh.isChecked()) {
			MessageUtil.showError(instance, R.string.valid_No_usthadh);
			return false;
		}
		return true;
	}

	private void submit() {
		OrderModel model = new OrderModel();
		model.owner = ParseUser.getCurrentUser();
		model.language = AppConstant.LANGUAGE_SYMBOL[sp_language.getSelectedItemPosition()];
		model.type = type;
		model.toUser = mSelectedUser;
		model.name = edt_name.getText().toString().trim();
		model.subject = edt_subject.getText().toString().trim();
		model.message = edt_message.getText().toString().trim();
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
		String url = AppConstant.STRIPE_CONNECT_URL + "order?order=" + orderId + "&amount=" + String.format("%.2f", Double.valueOf(edt_amount.getText().toString().trim()));
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
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
			final ListAdapter.ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(instance, R.layout.item_mosque, null);

				holder = new ListAdapter.ViewHolder();
				holder.layout_container = convertView.findViewById(R.id.layout_container);
				holder.img_avatar = convertView.findViewById(R.id.img_avatar);
				holder.txt_name = convertView.findViewById(R.id.txt_name);
				holder.txt_address = convertView.findViewById(R.id.txt_address);
				holder.check_select = convertView.findViewById(R.id.check_select);
				convertView.setTag(holder);

			} else {
				holder = (ListAdapter.ViewHolder) convertView.getTag();
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
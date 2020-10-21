package com.alesapps.islamik.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.ExceptionListener;
import com.alesapps.islamik.listener.ObjectListener;
import com.alesapps.islamik.listener.UserListListener;
import com.alesapps.islamik.model.BookModel;
import com.alesapps.islamik.model.NotificationModel;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.ui.view.DragListView;
import com.alesapps.islamik.utils.CommonUtil;
import com.alesapps.islamik.utils.DateTimeUtils;
import com.alesapps.islamik.utils.MessageUtil;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookActivity extends BaseActionBarActivity implements View.OnClickListener, DragListView.OnRefreshLoadingMoreListener {
	public static BookActivity instance;
	TextView txt_date;
	TextView txt_time;
	RadioButton rb_one;
	RadioButton rb_group;
	EditText edt_children;
	LinearLayout layout_children;
	EditText edt_children_a;
	EditText edt_children_b;
	EditText edt_children_c;
	EditText edt_children_d;
	EditText edt_amount;
	DragListView list_user;
	ListAdapter adapter;

	ArrayList<ParseUser> mServerDataList = new ArrayList<>();
	ArrayList<ParseUser> mDataList = new ArrayList<>();
	ParseUser mSelectedUser;
	Date mDate = Calendar.getInstance().getTime();
	int mHour = 0;
	int mMinute = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.book_session, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_book);
		txt_date = findViewById(R.id.txt_date);
		txt_time = findViewById(R.id.txt_time);
		rb_one = findViewById(R.id.rb_one);
		rb_group = findViewById(R.id.rb_group);
		edt_children = findViewById(R.id.edt_children);
		layout_children = findViewById(R.id.layout_children);
		edt_children_a = findViewById(R.id.edt_children_a);
		edt_children_b = findViewById(R.id.edt_children_b);
		edt_children_c = findViewById(R.id.edt_children_c);
		edt_children_d = findViewById(R.id.edt_children_d);
		edt_amount = findViewById(R.id.edt_amount);
		list_user = findViewById(R.id.listView);
		adapter = new ListAdapter();
		list_user.setAdapter(adapter);
		list_user.setOnRefreshListener(this);
		findViewById(R.id.layout_date).setOnClickListener(this);
		findViewById(R.id.layout_time).setOnClickListener(this);
		findViewById(R.id.rb_one).setOnClickListener(this);
		findViewById(R.id.rb_group).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		rb_one.setChecked(true);
		txt_date.setText("");
		txt_time.setText("");
		edt_amount.setText("");
		edt_amount.setText("");
		setPrefer(true);
		list_user.refresh();
	}

	private void setPrefer(boolean isOne) {
		edt_children.setText("");
		edt_children_a.setText("");
		edt_children_b.setText("");
		edt_children_c.setText("");
		edt_children_d.setText("");
		if (isOne)
			layout_children.setVisibility(View.GONE);
		else
			layout_children.setVisibility(View.VISIBLE);
		if (mSelectedUser != null) {
			if (isOne)
				edt_amount.setText(String.valueOf(mSelectedUser.getDouble(ParseConstants.KEY_PRICE)));
			else
				edt_amount.setText(String.valueOf(mSelectedUser.getDouble(ParseConstants.KEY_GROUP_PRICE)));
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.layout_date:
				showDatePicker();
				break;
			case R.id.layout_time:
				showTimePicker();
				break;
			case R.id.rb_one:
				setPrefer(true);
				break;
			case R.id.rb_group:
				setPrefer(false);
				break;
			case R.id.btn_submit:
				if (isValid())
					submit();
				break;
		}
	}

	private void showDatePicker() {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(mDate);
		int year = cal.get(Calendar.YEAR);
		int monthOfYear = cal.get(Calendar.MONTH);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		new DatePickerDialog(instance, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.MONTH, monthOfYear);
				cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				mDate = cal.getTime();
				txt_date.setText(DateTimeUtils.dateToString(mDate, DateTimeUtils.DATE_STRING_FORMAT));
			}
		}, year, monthOfYear, dayOfMonth).show();
	}

	private void showTimePicker() {
		new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mHour = hourOfDay;
				mMinute = minute;
				txt_time.setText(DateTimeUtils.convertTime(hourOfDay)+ ":" + DateTimeUtils.convertTime(minute));
			}
		},mHour, mMinute,false).show();
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
			mDataList.add(mServerDataList.get(i));
		}
		adapter.notifyDataSetChanged();
		list_user.onRefreshComplete();
	}

	private boolean isValid() {
		String date = txt_date.getText().toString().trim();
		String time = txt_time.getText().toString().trim();
		String children = edt_children.getText().toString().trim();
		if (TextUtils.isEmpty(date)) {
			MessageUtil.showError(instance, R.string.valid_No_date);
			return false;
		}
		if (TextUtils.isEmpty(time)) {
			MessageUtil.showError(instance, R.string.valid_No_time);
			return false;
		}
		if (mSelectedUser == null) {
			MessageUtil.showError(instance, R.string.valid_No_mosque);
			return false;
		}
		if (TextUtils.isEmpty(children)) {
			MessageUtil.showError(instance, R.string.valid_No_children);
			edt_children.requestFocus();
			return false;
		}
		return true;
	}

	private void submit() {
		final BookModel model = new BookModel();
		model.owner = ParseUser.getCurrentUser();
		model.toUser = mSelectedUser;
		model.bookDate = DateTimeUtils.getDateTime(mDate, mHour, mMinute);
		model.type = BookModel.TYPE_ONE;
		if (rb_group.isSelected())
			model.type = BookModel.TYPE_GROUP;
		model.price = Double.parseDouble(edt_amount.getText().toString().trim());
		ArrayList<String> children = new ArrayList<>();
		children.add(edt_children.getText().toString().trim());
		if (!TextUtils.isEmpty(edt_children_a.getText().toString().trim()))
			children.add(edt_children_a.getText().toString().trim());
		if (!TextUtils.isEmpty(edt_children_b.getText().toString().trim()))
			children.add(edt_children_b.getText().toString().trim());
		if (!TextUtils.isEmpty(edt_children_c.getText().toString().trim()))
			children.add(edt_children_c.getText().toString().trim());
		if (!TextUtils.isEmpty(edt_children_d.getText().toString().trim()))
			children.add(edt_children_d.getText().toString().trim());
		model.childName = children;

		dlg_progress.show();
		BookModel.Register(model, new ObjectListener() {
			@Override
			public void done(ParseObject object, String error) {
				if (error == null) {
					NotificationModel notificationModel = new NotificationModel();
					notificationModel.owner = ParseUser.getCurrentUser();
					notificationModel.toUser = mSelectedUser;
					notificationModel.type = NotificationModel.TYPE_BOOK;
					notificationModel.state = NotificationModel.STATE_PENDING;
					notificationModel.bookObj = object;
					notificationModel.message = String.format(getString(R.string.msg_notification_book),
							UserModel.GetFullName(ParseUser.getCurrentUser()), DateTimeUtils.dateToString(mDate, DateTimeUtils.DATE_STRING_FORMAT), edt_amount.getText().toString());
					NotificationModel.Register(notificationModel, new ExceptionListener() {
						@Override
						public void done(String error) {
							dlg_progress.cancel();
							if (error == null) {
								MessageUtil.showToast(instance, R.string.success);
								myBack();
							} else {
								MessageUtil.showToast(instance, error);
							}
						}
					});
				} else {
					dlg_progress.cancel();
					MessageUtil.showToast(instance, error);
				}
			}
		});
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
					if (rb_one.isSelected())
						edt_amount.setText(String.valueOf(mSelectedUser.getDouble(ParseConstants.KEY_PRICE)));
					else
						edt_amount.setText(String.valueOf(mSelectedUser.getDouble(ParseConstants.KEY_GROUP_PRICE)));
					adapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}
	}
}
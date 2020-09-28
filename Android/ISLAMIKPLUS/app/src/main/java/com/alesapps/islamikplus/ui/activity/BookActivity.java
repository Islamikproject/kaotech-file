package com.alesapps.islamikplus.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.ParseErrorHandler;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class BookActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static BookActivity instance = null;
	Spinner sp_price;
	Spinner sp_group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle("", -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_book);
		sp_price = findViewById(R.id.sp_price);
		sp_group = findViewById(R.id.sp_group);
		findViewById(R.id.btn_save).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		ArrayAdapter<String> adapterPrice = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.STRING_SESSION_PRICE);
		sp_price.setAdapter(adapterPrice);
		sp_price.setSelection(ParseUser.getCurrentUser().getInt(ParseConstants.KEY_PRICE));
		ArrayAdapter<String> adapterGroup = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.STRING_SESSION_GROUP);
		sp_group.setAdapter(adapterGroup);
		sp_group.setSelection(ParseUser.getCurrentUser().getInt(ParseConstants.KEY_GROUP_PRICE));
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.btn_save:
				saveData();
				return;
		}
	}

	private void saveData() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		currentUser.put(ParseConstants.KEY_PRICE, sp_price.getSelectedItemPosition());
		currentUser.put(ParseConstants.KEY_GROUP_PRICE, sp_group.getSelectedItemPosition());
		dlg_progress.show();
		currentUser.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				dlg_progress.hide();
				if (e == null) {
					MessageUtil.showToast(instance, R.string.success);
					myBack();
				} else {
					MessageUtil.showToast(instance, ParseErrorHandler.handle(e));
				}
			}
		});
	}
}


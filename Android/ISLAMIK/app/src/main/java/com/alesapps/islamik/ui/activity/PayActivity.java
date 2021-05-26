package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.alesapps.islamik.Constants;
import com.alesapps.islamik.R;
import com.alesapps.islamik.model.ParseConstants;
import com.parse.ParseObject;

public class PayActivity extends BaseActionBarActivity {
	public static PayActivity instance = null;
	LinearLayout layout_pay;
	LinearLayout layout_success;
	public static ParseObject mSermonObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;;
		SetTitle("", -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_pay);
		layout_pay = findViewById(R.id.layout_pay);
		layout_success = findViewById(R.id.layout_success);
		findViewById(R.id.btn_pay).setOnClickListener(this);
		layout_pay.setVisibility(View.VISIBLE);
		layout_success.setVisibility(View.GONE);
	}

	public void setSuccess() {
		layout_pay.setVisibility(View.GONE);
		layout_success.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.btn_pay:
				Double amount = mSermonObj.getDouble(ParseConstants.KEY_AMOUNT);
				Intent payIntent = new Intent(instance, GooglePayActivity.class);
				payIntent.setAction(Constants.ACTION_PAY_GOOGLE_PAY);
				payIntent.putExtra(Constants.OPTION_PRICE_EXTRA, amount * 100);
				startActivity(payIntent);
				break;
		}
	}
}
package com.alesapps.islamikplus.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.model.SermonModel;
import com.alesapps.islamikplus.ui.activity.DonationActivity;
import com.alesapps.islamikplus.utils.MessageUtil;

public class RaiseFragment extends BaseFragment{
	public static RaiseFragment instance;
	EditText edt_raiser;
	EditText edt_mosque;
	EditText edt_reason;
	EditText edt_amount;
	DonationActivity mActivity;

	public static RaiseFragment newInstance() {
		RaiseFragment fragment = new RaiseFragment();
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
		mView = inflater.inflate(R.layout.fragment_raise, container, false);
		edt_raiser = mView.findViewById(R.id.edt_raiser);
		edt_mosque = mView.findViewById(R.id.edt_mosque);
		edt_reason = mView.findViewById(R.id.edt_reason);
		edt_amount = mView.findViewById(R.id.edt_amount);
		mView.findViewById(R.id.btn_next).setOnClickListener(this);
		initialize();
		return mView;
	}

	private void initialize() {
		edt_raiser.setText("");
		edt_mosque.setText("");
		edt_reason.setText("");
		edt_amount.setText("");
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
			case R.id.btn_next:
				if (isValid())
					showConfirmDialog();
				break;
		}
	}

	private boolean isValid() {
		String raiser = edt_raiser.getText().toString().trim();
		String mosque = edt_mosque.getText().toString().trim();
		String reason = edt_reason.getText().toString().trim();
		String amount = edt_amount.getText().toString().trim();
		if (TextUtils.isEmpty(raiser)) {
			MessageUtil.showError(mActivity, R.string.valid_No_fundraiser);
			edt_raiser.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(mosque)) {
			MessageUtil.showError(mActivity, R.string.valid_No_mosque);
			edt_mosque.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(reason)) {
			MessageUtil.showError(mActivity, R.string.valid_No_reason);
			edt_reason.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(amount)) {
			MessageUtil.showError(mActivity, R.string.valid_No_amount);
			edt_amount.requestFocus();
			return false;
		}
		return true;
	}

	private void showConfirmDialog() {
		String mosque = edt_mosque.getText().toString().trim();
		String reason = edt_reason.getText().toString().trim();
		String amount = "$" + edt_amount.getText().toString().trim();
		String message = String.format(getString(R.string.raising_message), mosque, reason, amount);
		new AlertDialog.Builder(mActivity)
				.setTitle(R.string.app_name)
				.setMessage(message)
				.setPositiveButton(R.string.send_, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						send();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
	}

	private void send() {
		String raiser = edt_raiser.getText().toString().trim();
		String mosque = edt_mosque.getText().toString().trim();
		String reason = edt_reason.getText().toString().trim();
		String amount = edt_amount.getText().toString().trim();
		SermonModel model = new SermonModel();
		model.type = SermonModel.TYPE_RAISE;
		model.raiser = raiser;
		model.mosque = mosque;
		model.topic = reason;
		model.amount = Double.valueOf(amount);
		mActivity.dlg_progress.show();
		SermonModel.Register(model, new ExceptionListener() {
			@Override
			public void done(String error) {
				if (error == null) {
					MessageUtil.showToast(mActivity, R.string.success);
					initialize();
				} else {
					MessageUtil.showToast(mActivity, error);
				}
			}
		});
	}
}



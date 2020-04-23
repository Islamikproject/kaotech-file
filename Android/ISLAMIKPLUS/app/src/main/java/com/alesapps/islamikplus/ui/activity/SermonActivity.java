package com.alesapps.islamikplus.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.model.FileModel;
import com.alesapps.islamikplus.model.SermonModel;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.parse.ParseUser;

public class SermonActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static SermonActivity instance = null;
	EditText edt_topic;
	Spinner sp_amount;
	Button btn_next;
	Button btn_save;

	public static int type = SermonModel.TYPE_JUMAH;
	String file_path = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		if (type == SermonModel.TYPE_JUMAH)
			SetTitle(R.string.jumah_sermon, -1);
		else
			SetTitle(R.string.regular_sermon, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_sermon);
		edt_topic = findViewById(R.id.edt_topic);
		sp_amount = findViewById(R.id.sp_amount);
		btn_next = findViewById(R.id.btn_next);
		btn_save = findViewById(R.id.btn_save);

		findViewById(R.id.btn_next).setOnClickListener(this);
		findViewById(R.id.btn_save).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		btn_save.setVisibility(View.GONE);
		btn_next.setVisibility(View.VISIBLE);
		ArrayAdapter<String> adapterAmount = new ArrayAdapter<>(instance, android.R.layout.simple_spinner_dropdown_item, AppConstant.STRING_AMOUNT);
		sp_amount.setAdapter(adapterAmount);
		sp_amount.setSelection(0);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.btn_next:
				if (isValid())
					startActivity(new Intent(instance, ReadyActivity.class));
				return;
		}
	}

	private boolean isValid() {
		String topic = edt_topic.getText().toString().trim();
		if (TextUtils.isEmpty(topic)) {
			MessageUtil.showError(instance, R.string.valid_No_topic);
			edt_topic.requestFocus();
			return false;
		}
		return true;
	}

	public void showConfirmDialog(String path) {
		file_path = path;
		btn_save.setVisibility(View.VISIBLE);
		btn_next.setVisibility(View.GONE);
		String amount = AppConstant.STRING_AMOUNT[sp_amount.getSelectedItemPosition()];
		if (TextUtils.isEmpty(amount))
			amount = "";
		else
			amount = "$" + amount;
		String message = String.format(getString(R.string.confirm_mosque_virtual_basket), amount);
		new AlertDialog.Builder(instance)
				.setTitle(R.string.app_name)
				.setMessage(message)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						save();
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
	}

	private void save() {
		SermonModel model = new SermonModel();
		model.owner = ParseUser.getCurrentUser();
		model.type = type;
		model.topic = edt_topic.getText().toString().trim();
		model.amount = AppConstant.ARRAY_AMOUNT[sp_amount.getSelectedItemPosition()];
		model.video = FileModel.createVideoParseFile("video.mp4", file_path);
		dlg_progress.show();
		SermonModel.Register(model, new ExceptionListener() {
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
	}
}


package com.alesapps.islamikplus.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.BooleanListener;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.model.FileModel;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.SermonModel;
import com.alesapps.islamikplus.push.PushNoti;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.parse.ParseUser;

public class SermonActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static SermonActivity instance = null;
	EditText edt_topic;
	Spinner sp_amount;
	Button btn_next;
	Button btn_save;
	public static int type = SermonModel.TYPE_JUMAH;

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

	public void uploadVideo(String path){
		Uri video_uri = Uri.parse("file://" + path);
		dlg_progress.show();
		FileModel.UploadVideo(video_uri, new BooleanListener() {
			@Override
			public void done(boolean flag, String error) {
				if (flag) {
					save(error);
				} else {
					dlg_progress.cancel();
					MessageUtil.showToast(instance, error);
				}
			}
		});
	}

	private void save(String video_path) {
		final SermonModel model = new SermonModel();
		model.owner = ParseUser.getCurrentUser();
		model.type = type;
		model.topic = edt_topic.getText().toString().trim();
		model.amount = AppConstant.ARRAY_AMOUNT[sp_amount.getSelectedItemPosition()];
		model.video = video_path;
		SermonModel.Register(model, new ExceptionListener() {
			@Override
			public void done(String error) {
				dlg_progress.cancel();
				if (error == null) {
					String message = String.format(getString(R.string.success_jumah_message), model.owner.getString(ParseConstants.KEY_MOSQUE), model.topic);
					if (type == SermonModel.TYPE_REGULAR)
						message = String.format(getString(R.string.success_regular_message), model.owner.getString(ParseConstants.KEY_MOSQUE), model.topic);
					PushNoti.sendPushAll(type, message, "", null);
					MessageUtil.showToast(instance, R.string.success);
					if (SermonListActivity.instance != null)
						SermonListActivity.instance.list_sermon.refresh();
					myBack();
				} else {
					MessageUtil.showToast(instance, error);
				}
			}
		});
	}
}


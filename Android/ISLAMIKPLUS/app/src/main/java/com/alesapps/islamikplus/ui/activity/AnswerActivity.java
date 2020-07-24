package com.alesapps.islamikplus.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ExceptionListener;
import com.alesapps.islamikplus.model.MessageModel;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.utils.MessageUtil;
import com.parse.ParseObject;

public class AnswerActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static AnswerActivity instance = null;
	TextView txt_topic;
	TextView txt_question;
	EditText edt_answer;
	public static ParseObject mMessagesObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle("", -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_answer);
		txt_topic = findViewById(R.id.txt_topic);
		txt_question = findViewById(R.id.txt_question);
		edt_answer = findViewById(R.id.edt_answer);

		findViewById(R.id.btn_submit).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		txt_topic.setText(mMessagesObj.getParseObject(ParseConstants.KEY_SERMON).getString(ParseConstants.KEY_TOPIC));
		txt_question.setText(mMessagesObj.getString(ParseConstants.KEY_QUESTION));
		edt_answer.setText(mMessagesObj.getString(ParseConstants.KEY_ANSWER));
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.btn_submit:
				if (isValid())
					submit();
				return;
		}
	}

	private boolean isValid() {
		String answer = edt_answer.getText().toString().trim();
		if (TextUtils.isEmpty(answer)) {
			MessageUtil.showError(instance, R.string.valid_No_answer);
			edt_answer.requestFocus();
			return false;
		}
		return true;
	}

	private void submit() {
		String answer = edt_answer.getText().toString().trim();
		dlg_progress.show();
		MessageModel.RegisterAnswer(mMessagesObj, answer, new ExceptionListener() {
			@Override
			public void done(String error) {
				dlg_progress.hide();
				if (error == null) {
					MessageUtil.showToast(instance, R.string.success);
					if (MessagesActivity.instance != null)
						MessagesActivity.instance.list_message.refresh();
					myBack();
				} else {
					MessageUtil.showToast(instance, error);
				}
			}
		});
	}
}


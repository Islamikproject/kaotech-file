package com.alesapps.islamik.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.ExceptionListener;
import com.alesapps.islamik.model.MessageModel;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.utils.MessageUtil;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class QuestionActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static QuestionActivity instance = null;
	TextView txt_topic;
	TextView txt_answer;
	EditText edt_question;
	Button btn_submit;
	public static ParseObject mSermonObj;
	public static ParseObject mMessagesObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle("", -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_question);
		txt_topic = findViewById(R.id.txt_topic);
		txt_answer = findViewById(R.id.txt_answer);
		edt_question = findViewById(R.id.edt_question);
		btn_submit = findViewById(R.id.btn_submit);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		initialize();
	}

	private void initialize() {
		txt_topic.setText(mSermonObj.getString(ParseConstants.KEY_TOPIC));
		if (mMessagesObj != null) {
			txt_answer.setText(mMessagesObj.getString(ParseConstants.KEY_ANSWER));
			edt_question.setText(mMessagesObj.getString(ParseConstants.KEY_QUESTION));
			edt_question.setEnabled(false);
			btn_submit.setVisibility(View.GONE);
		} else {
			edt_question.setEnabled(true);
			btn_submit.setVisibility(View.VISIBLE);
		}
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
		String question = edt_question.getText().toString().trim();
		if (TextUtils.isEmpty(question)) {
			MessageUtil.showError(instance, R.string.valid_No_question);
			edt_question.requestFocus();
			return false;
		}
		return true;
	}

	private void submit() {
		String question = edt_question.getText().toString().trim();
		dlg_progress.show();
		MessageModel model = new MessageModel();
		model.sermon = mSermonObj;
		model.owner = ParseUser.getCurrentUser();
		model.mosque = mSermonObj.getParseUser(ParseConstants.KEY_OWNER);
		model.question = question;
		MessageModel.Register(model, new ExceptionListener() {
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


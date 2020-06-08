package com.alesapps.islamik.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.R;

public class VerseActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static VerseActivity instance = null;
	EditText edt_index;
	TextView txt_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(null, 0);
		ShowActionBarIcons(true, R.id.action_done);
		setContentView(R.layout.activity_verse);
		edt_index = findViewById(R.id.edt_index);
		txt_content = findViewById(R.id.txt_content);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
			case R.id.action_done:
				Integer index = Integer.parseInt(edt_index.getText().toString().trim());
				txt_content.setText(getValue(index));
				break;
		}
	}

	private String getValue(int index) {
		String[] verseArray = getResources().getStringArray(AppConstant.VERSE_ARRAY[index]);
		String value = "";
		for (int i = 0; i < verseArray.length; i ++) {
			if (i == 0)
				value = "@\"" + verseArray[i] + "\"";
			else
				value = value + ", " + "@\"" + verseArray[i] + "\"";
		}
		return value;
	}
}


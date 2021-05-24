package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.alesapps.islamik.R;
import com.alesapps.islamik.model.SermonModel;
import com.alesapps.islamik.model.UserModel;

public class JumahSermonActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static JumahSermonActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.jumah_and_sermon, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_jumah_sermons);
		findViewById(R.id.layout_africa).setOnClickListener(this);
		findViewById(R.id.layout_asia).setOnClickListener(this);
		findViewById(R.id.layout_america).setOnClickListener(this);
		findViewById(R.id.layout_australia).setOnClickListener(this);
		findViewById(R.id.layout_europ).setOnClickListener(this);
		findViewById(R.id.layout_scholars).setOnClickListener(this);
		findViewById(R.id.layout_women).setOnClickListener(this);
		findViewById(R.id.layout_kids).setOnClickListener(this);
		findViewById(R.id.layout_other).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.layout_africa:
				gotoNextActivity(SermonModel.TYPE_JUMAH, UserModel.TYPE_MOSQUE, UserModel.CONTINENT_AFRICA);
				break;
			case R.id.layout_asia:
				gotoNextActivity(SermonModel.TYPE_JUMAH, UserModel.TYPE_MOSQUE, UserModel.CONTINENT_ASIA);
				break;
			case R.id.layout_america:
				gotoNextActivity(SermonModel.TYPE_JUMAH, UserModel.TYPE_MOSQUE, UserModel.CONTINENT_AMERICA);
				break;
			case R.id.layout_australia:
				gotoNextActivity(SermonModel.TYPE_JUMAH, UserModel.TYPE_MOSQUE, UserModel.CONTINENT_AUSTRALIA);
				break;
			case R.id.layout_europ:
				gotoNextActivity(SermonModel.TYPE_JUMAH, UserModel.TYPE_MOSQUE, UserModel.CONTINENT_EUROPA);
				break;
			case R.id.layout_scholars:
				gotoNextActivity(SermonModel.TYPE_REGULAR, UserModel.TYPE_USTHADH, -1);
				break;
			case R.id.layout_women:
				gotoNextActivity(SermonModel.TYPE_REGULAR, UserModel.TYPE_INFLUENCER_WOMEN, -1);
				break;
			case R.id.layout_kids:
				gotoNextActivity(SermonModel.TYPE_REGULAR, UserModel.TYPE_INFLUENCER_KID, -1);
				break;
			case R.id.layout_other:
				gotoNextActivity(SermonModel.TYPE_REGULAR, UserModel.TYPE_INFLUENCER_OTHER, -1);
				break;
		}
	}

	private void gotoNextActivity(int _type, int _user, int _continent) {
		SermonActivity.continent = _continent;
		SermonActivity.type = _type;
		SermonActivity.userType = _user;
		startActivity(new Intent(instance, SermonActivity.class));
	}
}
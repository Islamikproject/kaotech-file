package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alesapps.islamik.R;
import com.alesapps.islamik.model.SermonModel;
import com.alesapps.islamik.model.UserModel;

public class JumahSermonActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static JumahSermonActivity instance;
	TextView txt_jumah;
	TextView txt_regular;
	LinearLayout layout_jumah;
	LinearLayout layout_regular;
	LinearLayout layout_infuencers_view;
	ImageView img_view;
	int type = SermonModel.TYPE_JUMAH;
	boolean isShowInfuencers = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.jumah_and_sermon, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_jumah_sermons);
		txt_jumah = findViewById(R.id.txt_jumah);
		txt_regular = findViewById(R.id.txt_regular);
		layout_jumah = findViewById(R.id.layout_jumah);
		layout_regular = findViewById(R.id.layout_regular);
		layout_infuencers_view = findViewById(R.id.layout_influencers_view);
		img_view = findViewById(R.id.img_view);
		txt_regular.setOnClickListener(this);
		txt_jumah.setOnClickListener(this);
		findViewById(R.id.layout_africa).setOnClickListener(this);
		findViewById(R.id.layout_asia).setOnClickListener(this);
		findViewById(R.id.layout_america).setOnClickListener(this);
		findViewById(R.id.layout_australia).setOnClickListener(this);
		findViewById(R.id.layout_europ).setOnClickListener(this);
		findViewById(R.id.layout_scholars).setOnClickListener(this);
		findViewById(R.id.layout_influencers).setOnClickListener(this);
		findViewById(R.id.layout_women).setOnClickListener(this);
		findViewById(R.id.layout_kids).setOnClickListener(this);
		findViewById(R.id.layout_other).setOnClickListener(this);
		setType(SermonModel.TYPE_JUMAH);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
			case R.id.txt_jumah:
				setType(SermonModel.TYPE_JUMAH);
				return;
			case R.id.txt_regular:
				setType(SermonModel.TYPE_REGULAR);
				break;
			case R.id.layout_africa:
				gotoNextActivity(SermonModel.TYPE_JUMAH, UserModel.TYPE_MOSQUE);
				break;
			case R.id.layout_asia:
				gotoNextActivity(SermonModel.TYPE_JUMAH, UserModel.TYPE_MOSQUE);
				break;
			case R.id.layout_america:
				gotoNextActivity(SermonModel.TYPE_JUMAH, UserModel.TYPE_MOSQUE);
				break;
			case R.id.layout_australia:
				gotoNextActivity(SermonModel.TYPE_JUMAH, UserModel.TYPE_MOSQUE);
				break;
			case R.id.layout_europ:
				gotoNextActivity(SermonModel.TYPE_JUMAH, UserModel.TYPE_MOSQUE);
				break;
			case R.id.layout_scholars:
				gotoNextActivity(SermonModel.TYPE_REGULAR, UserModel.TYPE_USTHADH);
				break;
			case R.id.layout_influencers:
				setInfluencer(!isShowInfuencers);
				break;
			case R.id.layout_women:
				gotoNextActivity(SermonModel.TYPE_REGULAR, UserModel.TYPE_INFLUENCER_WOMEN);
				break;
			case R.id.layout_kids:
				gotoNextActivity(SermonModel.TYPE_REGULAR, UserModel.TYPE_INFLUENCER_KID);
				break;
			case R.id.layout_other:
				gotoNextActivity(SermonModel.TYPE_REGULAR, UserModel.TYPE_INFLUENCER_OTHER);
				break;
		}
	}

	private void setType(int index) {
		type = index;
		txt_jumah.setBackgroundResource(R.drawable.bg_rectangle_white_line);
		txt_jumah.setTextColor(getResources().getColor(R.color.white));
		txt_regular.setBackgroundResource(R.drawable.bg_rectangle_white_line);
		txt_regular.setTextColor(getResources().getColor(R.color.white));
		layout_jumah.setVisibility(View.GONE);
		layout_regular.setVisibility(View.GONE);
		if (type == SermonModel.TYPE_JUMAH) {
			txt_jumah.setBackgroundColor(getResources().getColor(R.color.white));
			txt_jumah.setTextColor(getResources().getColor(R.color.green));
			layout_jumah.setVisibility(View.VISIBLE);
		} else if (type == SermonModel.TYPE_REGULAR) {
			txt_regular.setBackgroundColor(getResources().getColor(R.color.white));
			txt_regular.setTextColor(getResources().getColor(R.color.green));
			layout_regular.setVisibility(View.VISIBLE);
			setInfluencer(false);
		}
	}

	private void setInfluencer(boolean isShow) {
		isShowInfuencers = isShow;
		if (isShowInfuencers) {
			layout_infuencers_view.setVisibility(View.VISIBLE);
			img_view.setBackgroundResource(R.drawable.ic_up);
		} else {
			layout_infuencers_view.setVisibility(View.GONE);
			img_view.setBackgroundResource(R.drawable.ic_down);
		}
	}

	private void gotoNextActivity(int _type, int _user) {
		SermonActivity.type = _type;
		SermonActivity.userType = _user;
		startActivity(new Intent(instance, SermonActivity.class));
	}
}
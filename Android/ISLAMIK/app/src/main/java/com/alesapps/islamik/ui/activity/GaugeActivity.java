package com.alesapps.islamik.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.core.content.res.ResourcesCompat;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.R;
import com.alesapps.islamik.model.GaugeModel;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.utils.CommonUtil;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

public class GaugeActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static GaugeActivity instance = null;
	TextView txt_text;
	TextView txt_weblink;
	ImageView img_photo;
	VideoView video_view;
	public static ParseObject mGaugeObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.islamophobia_gauge, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_gauge);
		txt_text = findViewById(R.id.txt_text);
		txt_weblink = findViewById(R.id.txt_weblink);
		img_photo = findViewById(R.id.img_photo);
		video_view = findViewById(R.id.video_view);
		findViewById(R.id.txt_weblink).setOnClickListener(this);
		initialize();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub]
		super.onClick(view);
		switch (view.getId()) {
			case R.id.txt_weblink:
				String web_url = mGaugeObj.getString(ParseConstants.KEY_WEB_LINK);
				if (!TextUtils.isEmpty(web_url)) {
					if (!web_url.startsWith("http://") && !web_url.startsWith("https://"))
						web_url = "http://" + web_url;
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web_url));
					startActivity(browserIntent);
				}
				break;
		}
	}

	private void initialize() {
		if (mGaugeObj != null) {
			GaugeModel model = new GaugeModel();
			model.parse(mGaugeObj);
			if (!TextUtils.isEmpty(model.description)) {
				txt_text.setText(model.description);
				txt_text.setTextColor(getResources().getColor(AppConstant.ARRAY_COLOR[model.textColor]));
				txt_text.setBackgroundColor(getResources().getColor(AppConstant.ARRAY_COLOR[model.bgColor]));
				txt_text.setTextSize(Float.valueOf(AppConstant.ARRAY_STRING_SIZE[model.textSize]));
				Typeface face = ResourcesCompat.getFont(instance, AppConstant.ARRAY_FONT_VALUE[model.textFont]);
				txt_text.setTypeface(face);
			}
			txt_weblink.setText(model.webLink);
			if (model.photo != null)
				Picasso.get().load(CommonUtil.getImagePath(model.photo.getUrl())).into(img_photo);
			if (!TextUtils.isEmpty(model.video)) {
				video_view.setVideoPath(model.video);
				video_view.setMediaController(new MediaController(instance));
				video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mediaPlayer) {
						video_view.start();
					}
				});
			}
		}
	}
}


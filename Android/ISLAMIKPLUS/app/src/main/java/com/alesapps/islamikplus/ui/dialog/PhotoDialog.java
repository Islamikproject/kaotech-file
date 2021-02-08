package com.alesapps.islamikplus.ui.dialog;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

public class PhotoDialog extends Activity implements View.OnClickListener {
	public static PhotoDialog instance = null;
	ImageView img_view;
	public static ParseFile photoFile;
	String mImageUrl = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.dialog_photo);
		Window window = getWindow();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(getResources().getColor(R.color.black));
		}
		img_view = findViewById(R.id.img_view);
		img_view.setImageResource(R.drawable.default_image_bg);
		showImage(photoFile);
		findViewById(R.id.btn_close).setOnClickListener(this);
	}

	private void showImage(ParseFile file) {
		if (file != null) {
			showImage(file.getUrl());
		} else {
			showImage("");
		}
	}

	private void showImage(String path) {
		if (path == null || path.equals(mImageUrl))
			return;

		mImageUrl = path;

		if (!TextUtils.isEmpty(mImageUrl)) {
			Picasso.get().load(CommonUtil.getImagePath(mImageUrl)).into(img_view);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_close:
				finish();
				break;
		}
	}
}



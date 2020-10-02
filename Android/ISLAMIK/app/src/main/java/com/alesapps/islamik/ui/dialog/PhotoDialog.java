package com.alesapps.islamik.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.alesapps.islamik.R;
import com.alesapps.islamik.ui.activity.BaseActivity;
import com.alesapps.islamik.utils.CommonUtil;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

public class PhotoDialog extends BaseActivity {
	public static PhotoDialog instance = null;
	ImageView img_view;

	public static ParseFile photoFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.dialog_photo);
		img_view = findViewById(R.id.img_view);
		findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Picasso.get().load(CommonUtil.getImagePath(photoFile.getUrl())).into(img_view);
	}

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.in_right, R.anim.out_right);
		super.onPause();
	}
}



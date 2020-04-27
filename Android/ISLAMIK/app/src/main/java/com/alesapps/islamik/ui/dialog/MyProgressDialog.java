package com.alesapps.islamik.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import com.alesapps.islamik.R;

public class MyProgressDialog extends Dialog {

	public MyProgressDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MyProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.dialog_progress);

		getWindow().setBackgroundDrawable(new ColorDrawable(0));

		WindowManager.LayoutParams lp = getWindow().getAttributes();  
		lp.dimAmount = 0f; 
		getWindow().setAttributes(lp);

		setCancelable(false);
	}
	
	@Override
	public void show() {
		try {
			super.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}

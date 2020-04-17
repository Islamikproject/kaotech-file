package com.alesapps.kaotech.ui.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseFragment extends Fragment implements OnClickListener {
	protected View mView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onClick(View v) {}
}
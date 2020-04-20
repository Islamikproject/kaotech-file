package com.alesapps.islamikplus.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.ui.activity.DonationActivity;

public class RaiseFragment extends BaseFragment{
	public static RaiseFragment instance;
	DonationActivity mActivity;

	public static RaiseFragment newInstance() {
		RaiseFragment fragment = new RaiseFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		mActivity = DonationActivity.instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_raise, container, false);
		return mView;
	}
}



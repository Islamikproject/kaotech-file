package com.alesapps.islamikplus.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.ui.fragment.AmountFragment;
import com.alesapps.islamikplus.ui.fragment.BaseFragment;
import com.alesapps.islamikplus.ui.fragment.BasketFragment;
import com.alesapps.islamikplus.ui.fragment.RaiseFragment;

public class DonationActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static DonationActivity instance;
	TextView txt_amount;
	TextView txt_raise;
	TextView txt_basket;
	BaseFragment mCurrentFragment;
	int mCurrentFragmentIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.manage_donation, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_donation);
		txt_amount = findViewById(R.id.txt_amount);
		txt_raise = findViewById(R.id.txt_raise);
		txt_basket = findViewById(R.id.txt_basket);
		txt_amount.setOnClickListener(this);
		txt_raise.setOnClickListener(this);
		txt_basket.setOnClickListener(this);
		SwitchContent(AppConstant.SW_FRAGMENT_AMOUNT, null);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.txt_amount:
				SwitchContent(AppConstant.SW_FRAGMENT_AMOUNT, null);
				break;
			case R.id.txt_raise:
				SwitchContent(AppConstant.SW_FRAGMENT_RAISE, null);
				break;
			case R.id.txt_basket:
				SwitchContent(AppConstant.SW_FRAGMENT_BASKET, null);
				break;
		}
	}

	private void SwitchContent(int fragment_index, Bundle bundle) {
		if (mCurrentFragmentIndex != fragment_index) {
			mCurrentFragmentIndex = fragment_index;
			txt_amount.setTextColor(getResources().getColor(R.color.white));
			txt_raise.setTextColor(getResources().getColor(R.color.white));
			txt_basket.setTextColor(getResources().getColor(R.color.white));
			if (mCurrentFragmentIndex == AppConstant.SW_FRAGMENT_AMOUNT) {
				txt_amount.setTextColor(getResources().getColor(R.color.text_normal));
				mCurrentFragment = AmountFragment.newInstance();
			} else if (mCurrentFragmentIndex == AppConstant.SW_FRAGMENT_RAISE) {
				txt_raise.setTextColor(getResources().getColor(R.color.text_normal));
				mCurrentFragment = RaiseFragment.newInstance();
			} else if (mCurrentFragmentIndex == AppConstant.SW_FRAGMENT_BASKET) {
				txt_basket.setTextColor(getResources().getColor(R.color.text_normal));
				mCurrentFragment = BasketFragment.newInstance();
			}

			if (mCurrentFragment != null) {
				try {
					if (bundle != null)
						mCurrentFragment.setArguments(bundle);
					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.main_content, mCurrentFragment).commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}

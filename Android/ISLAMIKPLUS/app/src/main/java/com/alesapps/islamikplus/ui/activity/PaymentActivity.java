package com.alesapps.islamikplus.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import com.alesapps.islamikplus.AppConstant;
import com.alesapps.islamikplus.AppPreference;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.ui.view.MyWebView;
import com.alesapps.islamikplus.utils.CommonUtil;
import com.parse.ParseUser;

public class PaymentActivity extends BaseActionBarActivity {
    public static PaymentActivity instance = null;

    SeekBar seekbar_progress;
    MyWebView webView;
    public static boolean isSignUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        SetTitle(R.string.payment_information, -1);
        if (isSignUp)
            ShowActionBarIcons(true, R.id.action_done);
        else
            ShowActionBarIcons(true, R.id.action_back);
        setContentView(R.layout.activity_payment);

        seekbar_progress = findViewById(R.id.seekbar_progress);
        webView = findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        showUrl();

        webView.setOnWebViewListner(new MyWebView.OnWebViewListner() {

            @Override
            public void onScrollChanged(boolean isHitBottom) {}

            @Override
            public void onPageLoadStarted(String url) {
                // TODO Auto-generated method stub
                seekbar_progress.setMax(100);
                seekbar_progress.setProgress(0);
                seekbar_progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageLoadFinished() {
                // TODO Auto-generated method stub
                seekbar_progress.setVisibility(View.GONE);
            }

            @Override
            public void onPageLoadProgressChanged(int progress) {
                // TODO Auto-generated method stub
                seekbar_progress.setProgress(progress);
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.action_done:
                startActivity(new Intent(instance, MainActivity.class));
                finish();
                return;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        try {
            ParseUser.getCurrentUser().fetch();
        } catch (Exception ex) {}
        CommonUtil.hideKeyboard(instance, webView);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        showUrl();
    }

    private void showUrl() {
        String url = AppConstant.STRIPE_CONNECT_URL + "email=" + AppPreference.getStr(AppPreference.KEY.PHONE_NUMBER, "")
                + "&password=" + AppPreference.getStr(AppPreference.KEY.PASSWORD, "");
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }
}

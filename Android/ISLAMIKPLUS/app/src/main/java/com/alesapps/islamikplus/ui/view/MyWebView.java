package com.alesapps.islamikplus.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
@SuppressWarnings("deprecation")
public class MyWebView extends WebView {

	public MyWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
		super(context, attrs, defStyleAttr, privateBrowsing);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		WebSettings ws = getSettings();
		ws.getPluginState();
		ws.setPluginState(PluginState.ON);
		ws.setJavaScriptEnabled(true);
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		setBackgroundColor(Color.TRANSPARENT);

		setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				if (mOnWebViewListner != null)
					mOnWebViewListner.onPageLoadStarted(url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				final String mimeType = "text/html";
				final String encoding = "UTF-8";
				String html = "<html></html>";

				view.loadDataWithBaseURL("", html, mimeType, encoding, "");
			}
		});

		setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				if (mOnWebViewListner != null)
					mOnWebViewListner.onPageLoadProgressChanged(newProgress);
				
				if (newProgress == 100) {
					if (mOnWebViewListner != null) {
						mOnWebViewListner.onPageLoadFinished();
					}
				}
			}
		});
		setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		if (mOnWebViewListner != null) {
			int contentHeight = (int) Math.floor(getContentHeight() * getScale());
			if (contentHeight - getScrollY() <= getHeight()) {
				mOnWebViewListner.onScrollChanged(true);
			} else {
				mOnWebViewListner.onScrollChanged(false);
			}
		}

		super.onScrollChanged(l, t, oldl, oldt);
	}

	private OnWebViewListner mOnWebViewListner;
	public void setOnWebViewListner(OnWebViewListner listener) {
		mOnWebViewListner = listener;
	}

	public interface OnWebViewListner {
		public void onPageLoadStarted(String url);
		public void onPageLoadFinished();
		public void onPageLoadProgressChanged(int progress);
		public void onScrollChanged(boolean isHitBottom);
	}
}

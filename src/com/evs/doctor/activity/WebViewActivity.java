package com.evs.doctor.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.evs.doctor.R;
import com.evs.doctor.view.NavigationBarView;

public class WebViewActivity extends GenericActivity {

    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static String EXTRA_TITLE = "EXTRA_TITLE";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        title = (title != null ? title : getString(R.string.web_page));
        ((NavigationBarView) findViewById(R.id.view_navbar)).updateBar(title, null, null);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        String link = getIntent().getStringExtra(EXTRA_URL);

        if (link == null) {
            String content = getIntent().getStringExtra(EXTRA_DATA);
            mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        } else {
            mWebView.loadUrl(link);
        }
    }

}

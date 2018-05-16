package com.st.pizzame.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.st.pizzame.R;

/**
 * Created by sumit.thakur on 5/14/18.
 */

public class WebViewActivity extends Activity {
    private static final String TAG = WebViewActivity.class.getSimpleName();
    public static final String EXTRA_MAP_URL = "extra_map_url";

    private WebView mWebview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        if (getIntent().hasExtra(EXTRA_MAP_URL)) {
            String mapUrl = getIntent().getExtras().getString(EXTRA_MAP_URL);
            mWebview = (WebView) findViewById(R.id.location_map_view);
            WebSettings webSettings = mWebview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebview.setWebViewClient(new WebViewController());
            mWebview.loadUrl(mapUrl);
        } else {
            Toast.makeText(this, "Map not available.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Load Url :" + url);
            view.loadUrl(url);
            return true;
        }
    }
}

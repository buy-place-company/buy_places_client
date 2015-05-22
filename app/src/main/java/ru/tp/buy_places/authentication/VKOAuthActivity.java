package ru.tp.buy_places.authentication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.tp.buy_places.R;

/**
 * Created by Ivan on 22.05.2015.
 */
public class VKOAuthActivity extends Activity {
    public static final int RESULT_FAILED = 2;
    private WebView mWebView;
    final String REDIRECT_URI = "http://127.0.0.1/auth/vk";
    final String APP_ID = "4927495";
    final String PERMISSIONS = "email,friends";
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            setResult(RESULT_FAILED);
            finish();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (uri.getScheme().equals("http") && uri.getHost().equals("127.0.0.1") && uri.getPath().equals("/auth/vk")) {
                String code = uri.getQueryParameter("code");
                onVKCodeReceived(code);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

    private void onVKCodeReceived(String code) {
        Intent intent = new Intent();
        intent.putExtra("EXTRA_CODE", code);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vk_oauth_activity);
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient(mWebViewClient);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url = "https://oauth.vk.com/authorize?" +
                "client_id=" + APP_ID + "&" +
                "scope=" + PERMISSIONS + "&" +
                "redirect_uri=" + REDIRECT_URI + "&" +
                "response_type=code&" +
                "v=5.33";

        mWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}

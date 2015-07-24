package ru.tp.buy_places.authentication;

import android.app.Activity;
import android.app.Fragment;
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
    private String REDIRECT_URI;
    private String APP_ID;
    private String PERMISSIONS;
    private String PROTOCOL_VERSION;


    public static void startForResult(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), VKOAuthActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

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
            String authority = uri.getAuthority();
            String protocol = uri.getScheme();
            if ((protocol + "://" + authority).equals(REDIRECT_URI)) {
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


        APP_ID = getString(R.string.vk_app_id);
        REDIRECT_URI = getString(R.string.vk_redirect_uri);
        PERMISSIONS = getString(R.string.vk_permissions);
        PROTOCOL_VERSION = getString(R.string.vk_protocol_version);
        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setSaveFormData(false);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        String url = "https://oauth.vk.com/authorize?" +
                "client_id=" + APP_ID + "&" +
                "scope=" + PERMISSIONS + "&" +
                "redirect_uri=" + REDIRECT_URI + "&" +
                "response_type=code&" +
                "v=" + PROTOCOL_VERSION;

        mWebView.loadUrl(url);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}

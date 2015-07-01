package ru.tp.buy_places.authentication;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import ru.tp.buy_places.R;

/**
 * Created by Ivan on 20.05.2015.
 */
public class LoginActivity extends AccountAuthenticatorActivity implements View.OnClickListener {
    public static final String EXTRA_TOKEN_TYPE = "EXTRA_TOKEN_TYPE";
    private Button mLoginViaVKButton;

    private String SERVER_ROOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        mLoginViaVKButton = (Button)findViewById(R.id.button_login_via_vk);
        mLoginViaVKButton.setOnClickListener(this);
        SERVER_ROOT = getString(R.string.url_root);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login_via_vk:
                Intent intent = new Intent(this, VKOAuthActivity.class);
                startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    String code = data.getStringExtra("EXTRA_CODE");
                    if (code != null) {
                        new LoginViaVKAsyncTask(this, code).execute();
                    }
                } else if (resultCode == VKOAuthActivity.RESULT_FAILED) {
                    new LoginViaVKAsyncTask(this, "code").execute();
                }
        }
    }

    private class LoginViaVKAsyncTask extends AsyncTask<Void, Void, JSONObject> {
        private final Context mContext;
        private final String mCode;
        private ProgressDialog mProgressDialog;

        public LoginViaVKAsyncTask(Context context, String code) {
            mContext = context;
            mCode = code;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressDialog = ProgressDialog.show(mContext, mContext.getString(R.string.wait), mContext.getString(R.string.request_executes));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            StringBuilder paramsQuery = new StringBuilder();
            JSONObject response;
            try {
                paramsQuery.append("code="+mCode);
                URL url = new URL(SERVER_ROOT + "auth/vk" + "?" + paramsQuery.toString());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                String responseString = "";
                Scanner scanner = new Scanner(httpURLConnection.getInputStream());
                while (scanner.hasNextLine())
                    responseString += scanner.nextLine();
                response = new JSONObject(responseString);
                httpURLConnection.disconnect();
            } catch (JSONException | IOException e) {
                response = null;
                e.printStackTrace();
            } catch (Exception e) {
                response = null;
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //if (mProgressDialog != null && mProgressDialog.isShowing())
            //    mProgressDialog.hide();
            String username;

            if (jsonObject == null) {
                username = "USERNAME";
            } else {
                username = jsonObject.optString("user_name");
            }
            AccountManager accountManager = AccountManager.get(mContext);
            Bundle result = new Bundle();
            Account account = new BuyItAccount(username);
            String token = "token";
            if (accountManager.addAccountExplicitly(account, null, null)) {
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                result.putString(AccountManager.KEY_AUTHTOKEN, token);
                accountManager.setAuthToken(account, account.type, token);
            } else {
                result.putString(AccountManager.KEY_ERROR_MESSAGE, "Failed to add user");
            }
            setAccountAuthenticatorResult(result);
            setResult(RESULT_OK);
            finish();
        }
    }
}

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
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import ru.tp.buy_places.R;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.network.UnknownErrorResponse;
import ru.tp.buy_places.service.resourses.Player;

/**
 * Created by Ivan on 20.05.2015.
 */
public class LoginActivity extends AccountAuthenticatorActivity implements View.OnClickListener {
    public static final String EXTRA_TOKEN_TYPE = "EXTRA_TOKEN_TYPE";
    private Button mLoginViaVKButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        mLoginViaVKButton = (Button)findViewById(R.id.button_login_via_vk);
        mLoginViaVKButton.setOnClickListener(this);
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

    private class LoginViaVKAsyncTask extends AsyncTask<Void, Void, Bundle> {
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
        protected Bundle doInBackground(Void... params) {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("code", mCode);
            Iterator<Map.Entry<String, String>> iterator = requestParams.entrySet().iterator();
            StringBuilder paramsQuery = new StringBuilder();
            JSONObject responseJSONObject;
            try {
                while (iterator.hasNext()) {
                    Map.Entry entry = iterator.next();
                    paramsQuery.append(entry.getKey()).append("=").append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
                    if (iterator.hasNext()) {
                        paramsQuery.append("&");
                    }
                }

                URL url = new URL(mContext.getString(R.string.url_root) + "/auth/vk" + "?" + paramsQuery.toString());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                String responseString = "";
                Scanner scanner = new Scanner(httpURLConnection.getInputStream());
                while (scanner.hasNextLine())
                    responseString += scanner.nextLine();
                responseJSONObject = new JSONObject(responseString);
                httpURLConnection.disconnect();

            } catch (JSONException | IOException e) {
                responseJSONObject = null;
                e.printStackTrace();
            }
            // Start
            if (responseJSONObject == null) {
                Response resp = new UnknownErrorResponse();
            }
            int status = responseJSONObject.optInt("status");
            String message = responseJSONObject.optString("message", null);
            JSONObject dataJSONArray = responseJSONObject.optJSONObject("user");
            Player player = Player.fromJSONObject(dataJSONArray);
            Response resp = new Response(status, message, player);
            player = (Player)resp.getData();
            player.writeToDatabase(mContext);

            Bundle result = new Bundle();
            try {
//                String username = response.getString("name");
//                long id = response.getLong("id");
//                long vkId = response.getLong("id_vk");
                AccountManager accountManager = AccountManager.get(mContext);
                Account account = new BuyItAccount(player.getUsername());
                String token = "token";
                if (accountManager.addAccountExplicitly(account, null, null)) {
                    accountManager.setUserData(account, BuyItAccount.KEY_ID, Long.toString(player.getId()));
                    //accountManager.setUserData(account, BuyItAccount.KEY_VK_ID, Long.toString(vkId));
                    result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                    result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                    result.putString(AccountManager.KEY_AUTHTOKEN, token);
                    accountManager.setAuthToken(account, account.type, token);
                    //ServiceHelper.get(mContext).getProfile();


            //End
//            try {
//                String username = response.getString("name");
//                long id = response.getLong("id");
//                long vkId = response.getLong("id_vk");
//                AccountManager accountManager = AccountManager.get(mContext);
//                Account account = new BuyItAccount(username);
//                String token = "token";
//                if (accountManager.addAccountExplicitly(account, null, null)) {
//                    accountManager.setUserData(account, BuyItAccount.KEY_ID, Long.toString(id));
//                    //accountManager.setUserData(account, BuyItAccount.KEY_VK_ID, Long.toString(vkId));
//                    result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
//                    result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
//                    result.putString(AccountManager.KEY_AUTHTOKEN, token);
//                    accountManager.setAuthToken(account, account.type, token);
//                    ServiceHelper.get(mContext).getProfile();
                } else {
                    result.putString(AccountManager.KEY_ERROR_MESSAGE, "Failed to add user");
                }
//            } catch (JSONException e) {
//                result.putString(AccountManager.KEY_ERROR_MESSAGE, "Invalid response");
            } catch (NullPointerException e) {
                result.putString(AccountManager.KEY_ERROR_MESSAGE, "Service Unavailiable");
            }
            return result;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            setAccountAuthenticatorResult(result);
            setResult(RESULT_OK);
            finish();
        }
    }
}

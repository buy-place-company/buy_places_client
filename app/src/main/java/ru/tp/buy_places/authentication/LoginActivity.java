package ru.tp.buy_places.authentication;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import ru.tp.buy_places.R;
import ru.tp.buy_places.service.BuyItService;
import ru.tp.buy_places.service.ServiceHelper;

/**
 * Created by Ivan on 20.05.2015.
 */
public class LoginActivity extends AccountAuthenticatorActivity implements View.OnClickListener {
    public static final String EXTRA_TOKEN_TYPE = "EXTRA_TOKEN_TYPE";
    private Button mLoginViaVKButton;
    private BroadcastReceiver mAuthenticationCompletedReceiver = new AuthenticationCompletedReceiver();
    private long mAuthenticationRequestId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        LocalBroadcastManager.getInstance(this).registerReceiver(mAuthenticationCompletedReceiver, new IntentFilter(ServiceHelper.ACTION_REQUEST_RESULT));
        mLoginViaVKButton = (Button) findViewById(R.id.button_login_via_vk);
        mLoginViaVKButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mAuthenticationCompletedReceiver);
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
                        mAuthenticationRequestId = ServiceHelper.get(this).authenticate(code);
                    }
                }
        }
    }

    private class AuthenticationCompletedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final long requestId = intent.getLongExtra(BuyItService.EXTRA_REQUEST_ID, 0);
            if (mAuthenticationRequestId == requestId) {
                long id = intent.getLongExtra(BuyItService.EXTRA_ID, 0);
                String username = intent.getStringExtra(BuyItService.EXTRA_USERNAME);

                AccountManager accountManager = AccountManager.get(context);
                Account account = new BuyItAccount(username);

                Bundle result = new Bundle();
                if (accountManager.addAccountExplicitly(account, null, null)) {
                    accountManager.setUserData(account, BuyItAccount.KEY_ID, Long.toString(id));
                    result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                    result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                } else {
                    result.putString(AccountManager.KEY_ERROR_MESSAGE, "Failed to add user");
                }
                LoginActivity.this.setAccountAuthenticatorResult(result);
                LoginActivity.this.setResult(RESULT_OK);
                LoginActivity.this.finish();
            }
        }
    }
}

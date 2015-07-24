package ru.tp.buy_places.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import ru.tp.buy_places.R;
import ru.tp.buy_places.authentication.BuyItAccount;
import ru.tp.buy_places.service.ServiceHelper;


public class SplashScreenActivity extends AppCompatActivity {
    private ProgressBar mProgressbar;

    private long mProfileRequestId;
    private long mMyVenuesRequestId;
    private long mMyDealsRequestId;


    private boolean mProfileLoaded = false;
    private boolean mMyVenuesLoaded = false;
    private boolean mMyDealsLoaded = false;
    private BroadcastReceiver mSplashScreenBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mProgressbar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressbar.setMax(3);
        //final int SPLASH_SCREEN_DURATION = getResources().getInteger(R.integer.splash_screen_duration);
        mSplashScreenBroadcastReceiver = new SplashScreenBrodcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ServiceHelper.ACTION_REQUEST_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mSplashScreenBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(BuyItAccount.TYPE);
        if (accounts.length == 0) {
            addNewAccount(accountManager);
        } else {
            mProfileRequestId = ServiceHelper.get(this).getProfile();
            mMyVenuesRequestId = ServiceHelper.get(this).getMyVenues();
            mMyDealsRequestId = ServiceHelper.get(this).getDeals();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSplashScreenBroadcastReceiver);
    }

    private void addNewAccount(AccountManager accountManager) {
        accountManager.addAccount(BuyItAccount.TYPE, BuyItAccount.TOKEN_FULL_ACCESS, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle result = future.getResult();
                    //ServiceHelper.get(SplashScreenActivity.this).getProfile();
                    //MainActivity.start(SplashScreenActivity.this);
                } catch (Exception e) {
                    SplashScreenActivity.this.finish();
                }
            }
        }, null);
    }


    @Override
    public void onBackPressed() {

    }

    private class SplashScreenBrodcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long requestId = intent.getLongExtra(ServiceHelper.EXTRA_REQUEST_ID, 0);
            int status = intent.getIntExtra(ServiceHelper.EXTRA_RESULT_CODE, 0);
            if (requestId == mProfileRequestId) {
                mProfileLoaded = true;
                mProgressbar.incrementProgressBy(1);
            }
            if (requestId == mMyVenuesRequestId) {
                mMyVenuesLoaded = true;
                mProgressbar.incrementProgressBy(1);
            }
            if (requestId == mMyDealsRequestId) {
                mMyDealsLoaded = true;
                mProgressbar.incrementProgressBy(1);
            }
            if (mProfileLoaded && mMyVenuesLoaded && mMyDealsLoaded) {
                MainActivity.start(SplashScreenActivity.this);
                finish();
            }
        }
    }
}

package ru.tp.buy_places.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import ru.tp.buy_places.R;
import ru.tp.buy_places.authentication.BuyItAccount;
import ru.tp.buy_places.gcm.RegistrationIntentService;
import ru.tp.buy_places.service.ServiceHelper;
import ru.tp.buy_places.utils.AccountManagerHelper;


public class SplashScreenActivity extends AppCompatActivity {
    private String LOG_TAG = SplashScreenActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private ProgressBar mProgressbar;

    private long mProfileRequestId;
    private long mMyVenuesRequestId;
    private long mMyDealsRequestId;


    private boolean mProfileLoaded = false;
    private boolean mMyVenuesLoaded = false;
    private boolean mMyDealsLoaded = false;
    private boolean mGCMRegistrationCompleted = false;
    private BroadcastReceiver mSplashScreenBroadcastReceiver;
    private long mFavouriteVenuesRequestId;
    private boolean mFavouriteVenuesLoaded = false;

    public static void startClearTask(Context context) {
        Intent intent = new Intent(context, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mProgressbar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressbar.setMax(4);
        mSplashScreenBroadcastReceiver = new SplashScreenBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ServiceHelper.ACTION_REQUEST_RESULT);
        IntentFilter gcmIntentFilter = new IntentFilter(RegistrationIntentService.GCM_REGISTRATION_COMPLETED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mSplashScreenBroadcastReceiver, intentFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(mSplashScreenBroadcastReceiver, gcmIntentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(BuyItAccount.TYPE);
        if (accounts.length == 0) {
            AccountManagerHelper.addNewAccount(this, accountManager);
        } else {
            mProfileLoaded = false;
            mMyVenuesLoaded = false;
            mMyDealsLoaded = false;
            mGCMRegistrationCompleted = false;
            mFavouriteVenuesLoaded = false;
            mProfileRequestId = ServiceHelper.get(this).getProfile();
            mMyVenuesRequestId = ServiceHelper.get(this).getMyVenues();
            mMyDealsRequestId = ServiceHelper.get(this).getDeals();
            mFavouriteVenuesRequestId = ServiceHelper.get(this).getFavouriteVenues();
            if (checkPlayServices()) {
                RegistrationIntentService.startGCMRegistrationService(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSplashScreenBroadcastReceiver);
    }


    @Override
    public void onBackPressed() {

    }

    private class SplashScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ServiceHelper.ACTION_REQUEST_RESULT:
                    long requestId = intent.getLongExtra(ServiceHelper.EXTRA_REQUEST_ID, 0);
                    int status = intent.getIntExtra(ServiceHelper.EXTRA_RESULT_CODE, 0);
                    if (requestId == mProfileRequestId) {
                        mProfileLoaded = true;
                        mProgressbar.incrementProgressBy(1);
                    } else if (requestId == mMyVenuesRequestId) {
                        mMyVenuesLoaded = true;
                        mProgressbar.incrementProgressBy(1);
                    } else if (requestId == mMyDealsRequestId) {
                        mMyDealsLoaded = true;
                        mProgressbar.incrementProgressBy(1);
                    } else if (requestId == mFavouriteVenuesRequestId) {
                        mFavouriteVenuesLoaded = true;
                        mProgressbar.incrementProgressBy(1);
                    }
                    break;
                case RegistrationIntentService.GCM_REGISTRATION_COMPLETED:
                    mGCMRegistrationCompleted = true;
            }
            if (mProfileLoaded && mMyVenuesLoaded && mMyDealsLoaded && mFavouriteVenuesLoaded && mGCMRegistrationCompleted) {
                MainActivity.start(SplashScreenActivity.this);
                mProfileLoaded = false;
                mMyVenuesLoaded = false;
                mMyDealsLoaded = false;
                mGCMRegistrationCompleted = false;
                mFavouriteVenuesLoaded = false;
                Log.e(LOG_TAG, "MainActivity.start()");
            }

        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}

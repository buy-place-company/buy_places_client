package ru.tp.buy_places.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import ru.tp.buy_places.R;
import ru.tp.buy_places.authentication.BuyItAccount;


public class SplashScreenActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final ImageView splashImageView =
                (ImageView) findViewById(R.id.SplashImageView);
        splashImageView.setBackgroundResource(R.drawable.splash);
        final AnimationDrawable frameAnimation =
                (AnimationDrawable)splashImageView.getBackground();
        splashImageView.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });
        final int SPLASH_SCREEN_DURATION = getResources().getInteger(R.integer.splash_screen_duration);

        final AccountManager accountManager = AccountManager.get(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Account[] accounts = accountManager.getAccountsByType(BuyItAccount.TYPE);
                if (accounts.length == 0) {
                    //addNewAccount(accountManager);
                    MainActivity.start(SplashScreenActivity.this);
                    finish();
                } else {
                    MainActivity.start(SplashScreenActivity.this);
                    finish();
                }
            }
        }, SPLASH_SCREEN_DURATION);
    }

    private void addNewAccount(AccountManager accountManager) {
        accountManager.addAccount(BuyItAccount.TYPE, BuyItAccount.TOKEN_FULL_ACCESS, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    future.getResult();
                    MainActivity.start(SplashScreenActivity.this);
                } catch (Exception e) {
                    SplashScreenActivity.this.finish();
                }
            }
        }, null);
    }


    @Override
    public void onBackPressed() {

    }
}

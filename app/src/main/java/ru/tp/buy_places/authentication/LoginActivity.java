package ru.tp.buy_places.authentication;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.andraskindler.parallaxviewpager.ParallaxViewPager;

import ru.tp.buy_places.R;
import ru.tp.buy_places.service.BuyItService;
import ru.tp.buy_places.service.ServiceHelper;
import ru.tp.buy_places.service.network.Response;

/**
 * Created by Ivan on 20.05.2015.
 */
public class LoginActivity extends AccountAuthenticatorActivity implements LoginFragment.OnLoginListener, RegistrationFragment.OnRegistrationListener {
    public static final String EXTRA_TOKEN_TYPE = "EXTRA_TOKEN_TYPE";
    private BroadcastReceiver mAuthenticationCompletedReceiver = new AuthenticationCompletedReceiver();
    private long mAuthenticationRequestId;
    private LoginPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    private ParallaxViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout)findViewById(R.id.tab_layout);
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.primary));
        mPagerAdapter = new LoginPagerAdapter(this, getFragmentManager());
        mViewPager = (ParallaxViewPager) findViewById(R.id.view_pager);
        mViewPager.setScaleType(ParallaxViewPager.FIT_WIDTH);
        mViewPager.setBackgroundResource(R.drawable.login_background);
        mViewPager.setAdapter(mPagerAdapter);
        setSupportActionBar(toolbar);
        mTabLayout.setupWithViewPager(mViewPager);
        LocalBroadcastManager.getInstance(this).registerReceiver(mAuthenticationCompletedReceiver, new IntentFilter(ServiceHelper.ACTION_REQUEST_RESULT));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mAuthenticationCompletedReceiver);
    }


    @Override
    public void onLoginViaVkButtonClickListener(String code) {
        mAuthenticationRequestId = ServiceHelper.get(this).authenticate(code);
    }

    @Override
    public void onLoginClickListener(String username, String password) {
        mAuthenticationRequestId = ServiceHelper.get(this).baseLogin(username, password);
    }

    @Override
    public void onRegisterButtonClick(String email, String username, String password) {
        mAuthenticationRequestId = ServiceHelper.get(this).register(email, username, password);
    }


    private class AuthenticationCompletedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final long requestId = intent.getLongExtra(ServiceHelper.EXTRA_REQUEST_ID, 0);
            final int status = intent.getIntExtra(ServiceHelper.EXTRA_RESULT_CODE, 0);
            if (mAuthenticationRequestId == requestId) {
                switch (status) {
                    case Response.RESULT_OK:
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
                        break;
                    case Response.RESULT_LOGIN_FAILED:
                        Toast.makeText(LoginActivity.this, "В процессе логина произошла ошибка", Toast.LENGTH_LONG).show();
                        break;
                    case Response.RESULT_UNAVAILABLE:
                        Toast.makeText(LoginActivity.this, "Сервис недоступен, проверьте подключение к сети", Toast.LENGTH_LONG).show();
                        break;
                    case Response.RESULT_USER_ALREADY_EXISTS:
                        Toast.makeText(LoginActivity.this, "Пользователь с таким Email уже существует", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    }


    private class LoginPagerAdapter extends FragmentPagerAdapter {

        private final Context mContext;

        public LoginPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LoginFragment.newInstance();
                case 1:
                    return RegistrationFragment.newInstance();
                default:
                    return LoginFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.login_page_name);
                case 1:
                    return mContext.getString(R.string.registration_page_name);
                default:
                    return mContext.getString(R.string.login_page_name);
            }
        }
    }


}

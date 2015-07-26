package ru.tp.buy_places.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import java.io.IOException;

import ru.tp.buy_places.activities.SplashScreenActivity;
import ru.tp.buy_places.authentication.BuyItAccount;

/**
 * Created by Ivan on 09.07.2015.
 */
public final class AccountManagerHelper {
    private AccountManagerHelper(){}

    public static long getPlayerId(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        String userdata = accountManager.getUserData(account, BuyItAccount.KEY_ID);
        return Long.parseLong(userdata);
    }


    public static void logout(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        accountManager.removeAccount(account, null, null, null);
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType(BuyItAccount.TYPE);
        if (accounts.length == 1) {
            return accounts[0];
        } else
            throw new IllegalStateException("There is only one account must exists");
    }

    public static void addNewAccount(final Activity activity, AccountManager am) {
        am.addAccount(BuyItAccount.TYPE, BuyItAccount.TOKEN_FULL_ACCESS, null, null, activity,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            future.getResult();
                        } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                            activity.finish();
                        }
                    }
                }, null
        );
    }

    public static void removeAccount(final Activity activity) {
        final AccountManager accountManager = AccountManager.get(activity);
        Account account = getAccount(accountManager);
        accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
            @Override
            public void run(AccountManagerFuture<Boolean> future) {
                try {
                    if (future.getResult()) {
                        SplashScreenActivity.startClearTask(activity);
                    }
                } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                    e.printStackTrace();
                }

            }
        }, null);
    }
}

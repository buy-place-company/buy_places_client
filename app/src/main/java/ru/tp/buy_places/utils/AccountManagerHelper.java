package ru.tp.buy_places.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

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


}

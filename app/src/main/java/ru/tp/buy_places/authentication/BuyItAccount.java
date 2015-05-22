package ru.tp.buy_places.authentication;

import android.accounts.Account;

/**
 * Created by Ivan on 22.05.2015.
 */
public class BuyItAccount extends Account{
    public static final String TYPE = "ru.tp.buy_it.account";
    public static final String TOKEN_FULL_ACCESS = "ru.tp.buy_it.account.FULL_ACCESS";


    public BuyItAccount(String name) {
        super(name, TYPE);
    }
}

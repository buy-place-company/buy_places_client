package ru.tp.buy_places.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BuyItAuthenticatorService extends Service {

    private BuyItAuthenticator mBuyItAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mBuyItAuthenticator = new BuyItAuthenticator(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBuyItAuthenticator.getIBinder();
    }
}

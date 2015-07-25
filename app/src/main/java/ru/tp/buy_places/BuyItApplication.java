package ru.tp.buy_places;

import android.app.Application;
import android.webkit.CookieSyncManager;

import com.google.android.gms.maps.MapsInitializer;

import java.net.CookieManager;
import java.net.CookieStore;

import ru.tp.buy_places.service.network.PersistentCookieStore;

/**
 * Created by Ivan on 20.05.2015.
 */
public class BuyItApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MapsInitializer.initialize(this);
        CookieSyncManager.createInstance(this);
        CookieStore cookieStore = new PersistentCookieStore(this);
        CookieManager cookieManager = new CookieManager(cookieStore, null);
        CookieManager.setDefault(cookieManager);
    }
}

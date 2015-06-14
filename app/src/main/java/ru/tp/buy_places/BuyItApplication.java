package ru.tp.buy_places;

import android.app.Application;

import com.google.android.gms.maps.MapsInitializer;

/**
 * Created by Ivan on 20.05.2015.
 */
public class BuyItApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MapsInitializer.initialize(this);
    }
}

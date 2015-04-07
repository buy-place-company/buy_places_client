package ru.tp.buy_places.content_provider;

import android.net.Uri;

/**
 * Created by Ivan on 07.04.2015.
 */
public final class BuyPlacesContract {
    public static final String AUTHORITY = "ru.tp.buy_places.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);


    private BuyPlacesContract () {

    }



}

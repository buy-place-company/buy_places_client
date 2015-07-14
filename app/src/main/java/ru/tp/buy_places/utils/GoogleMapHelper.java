package ru.tp.buy_places.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Ivan on 04.07.2015.
 */
public final class GoogleMapHelper {
    private GoogleMapHelper(){}

    public static double getDistance(LatLng point1, LatLng point2) {
        double R = 6372795;
        double latitude1 = point1.latitude;
        double longitude1 = point1.longitude;
        double latitude2 = point2.latitude;
        double longitude2 = point2.longitude;

        latitude1 *= Math.PI/180;
        longitude1 *= Math.PI/180;
        latitude2 *= Math.PI/180;
        longitude2 *= Math.PI/180;

        double cl1 = Math.cos(latitude1);
        double cl2 = Math.cos(latitude2);
        double sl1 = Math.sin(latitude1);
        double sl2 = Math.sin(latitude2);
        double delta = longitude2 - longitude1;
        double cdelta = Math.cos(delta);
        double sdelta = Math.sin(delta);

        double y = Math.sqrt(Math.pow(cl2 * sdelta, 2) + Math.pow(cl1 * sl2 - sl1 * cl2 * cdelta, 2));
        double x = sl1 * sl2 + cl1 * cl2 * cdelta;
        double ad = Math.atan2(y, x);
        double dist = ad * R;
        return dist;
    }
}
package ru.tp.buy_places.map;

import android.location.Location;

/**
 * Created by Krygin on 06.07.2015.
 */
public interface LocationProvider {
    void addLocationListener(LocationApiConnectionListener.OnLocationChangedListener listener);
    void removeLocationListener(LocationApiConnectionListener.OnLocationChangedListener listener);
    void notifyLocationChanged(Location location);
    void requestLastKnownLocation();
}

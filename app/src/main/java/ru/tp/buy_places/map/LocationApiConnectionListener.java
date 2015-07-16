package ru.tp.buy_places.map;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import ru.tp.buy_places.R;

/**
 * Created by Ivan on 05.07.2015.
 */
public class LocationApiConnectionListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private final OnLocationChangedListener mOnLocationChangedListener;
    private final GoogleApiClientHolder mGoogleApiClientHolder;
    private final Context mContext;

    public LocationApiConnectionListener(Context context, GoogleApiClientHolder googleApiClientHolder, OnLocationChangedListener onLocationChangedListener) {
        mContext = context;
        mOnLocationChangedListener = onLocationChangedListener;
        mGoogleApiClientHolder = googleApiClientHolder;
    }

    @Override
    public void onConnected(Bundle bundle) {
        final GoogleApiClient googleApiClient = mGoogleApiClientHolder.getGoogleApiClient();
        final Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastKnownLocation != null) {
            mOnLocationChangedListener.onLocationChanged(lastKnownLocation);
        }
        final LocationRequest locationRequest = new LocationRequest()
                .setInterval(mContext.getResources().getInteger(R.integer.location_request_interval))
                .setFastestInterval(R.integer.location_request_fastest_interval)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        Toast.makeText(mContext, "Connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(mContext, "Suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(mContext, "Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mOnLocationChangedListener.onLocationChanged(location);
    }

    public interface OnLocationChangedListener {
        void onLocationChanged(Location location);
    }

    public interface GoogleApiClientHolder {
        GoogleApiClient getGoogleApiClient();
    }
}

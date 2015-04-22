package ru.tp.buy_places.fragments.map;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.tp.buy_places.R;

import static ru.tp.buy_places.content_provider.BuyPlacesContract.Places;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener, LoaderManager.LoaderCallbacks<Cursor> {

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private Marker mMyPositionMarker;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map_view);
        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onStart();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*10, 10.f, this);
        mMyPositionMarker = mGoogleMap.addMarker(new MarkerOptions().title("You are here").position(new LatLng(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(), mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude())));
        getLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mGoogleMap != null) {
            if (mMyPositionMarker == null) {
                mGoogleMap.addMarker(new MarkerOptions().title("You are here").position(new LatLng(location.getLatitude(), location.getLongitude())));
            } else {
                mMyPositionMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Places.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        while (data.moveToNext()) {
            double latitude = data.getDouble(data.getColumnIndex(Places.COLUMN_LATITUDE));
            double longitude = data.getDouble(data.getColumnIndex(Places.COLUMN_LONGITUDE));
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

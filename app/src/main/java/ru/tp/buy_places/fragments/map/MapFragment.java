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
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashSet;
import java.util.Set;

import ru.tp.buy_places.R;

import static ru.tp.buy_places.content_provider.BuyPlacesContract.Places;


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ClusterManager<ClusterItem> mClusterManager;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private Marker mMyPositionMarker;


    private Set<Marker> markers = new HashSet<>();

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
        mMapView.onCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMapView != null)
            mMapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null)
            mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null)
            mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMapView != null)
            mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null)
            mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mClusterManager = new ClusterManager<ClusterItem>(getActivity(), mGoogleMap);
        if (mLocationManager != null) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10.f, this);
            Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                //mMyPositionMarker = mGoogleMap.addMarker(new MarkerOptions().title("You are here").position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));
                mClusterManager.addItem(new MyClusterItem(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
                mClusterManager.cluster();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mGoogleMap != null) {
            if (mMyPositionMarker == null) {
                //mGoogleMap.addMarker(new MarkerOptions().title("You are here").position(new LatLng(location.getLatitude(), location.getLongitude())));
                mClusterManager.addItem(new MyClusterItem(location.getLatitude(), location.getLongitude()));
                mClusterManager.cluster();
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
            //mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
            mClusterManager.addItem(new MyClusterItem(latitude, longitude));
            mClusterManager.cluster();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private class MyClusterItem implements ClusterItem {
        private final double latitude;
        private final double longitude;

        private MyClusterItem(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public LatLng getPosition() {
            return new LatLng(latitude, longitude);
        }
    }

}

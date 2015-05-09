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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import ru.tp.buy_places.R;
import ru.tp.buy_places.map.ObjectRenderer;
import ru.tp.buy_places.map.PlaceClusterItem;
import ru.tp.buy_places.service.ServiceHelper;

import static ru.tp.buy_places.content_provider.BuyPlacesContract.Places;


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ALL_PLACES_LOADER_ID = 0;
    private static final int CHANGED_PLACE_LOADER_ID = 1;


    private ClusterManager<PlaceClusterItem> mClusterManager;
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
        MapsInitializer.initialize(getActivity());
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        getLoaderManager().initLoader(ALL_PLACES_LOADER_ID, null, this);

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
        mClusterManager = new ClusterManager<>(getActivity(), mGoogleMap);
        mClusterManager.setRenderer(new ObjectRenderer(getActivity(), mGoogleMap, mClusterManager));
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("MapFragment", "OnCameraChanged");
                mClusterManager.onCameraChange(cameraPosition);
                ServiceHelper.get(getActivity()).getObjectsAroundThePoint(cameraPosition.target);
            }
        });
        if (mLocationManager != null) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10.f, this);
            Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation != null) {
                Log.d("MapFragment", "OnMapReady");
                ServiceHelper.get(getActivity()).getObjectsAroundThePlayer(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
                mMyPositionMarker = mGoogleMap.addMarker(new MarkerOptions().title("You are here").position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        ServiceHelper.get(getActivity()).getObjectsAroundThePlayer(new LatLng(location.getLatitude(), location.getLongitude()));
        Log.d("MapFragment", "OnLocationChanged");
        if (mGoogleMap != null) {
            if (mMyPositionMarker == null) {
                mMyPositionMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
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
        if (mClusterManager != null)
            mClusterManager.clearItems();
        while (data.moveToNext()) {
            long rowId = data.getLong(data.getColumnIndex(Places._ID));
            String id = data.getString(data.getColumnIndex(Places.COLUMN_ID));
            String name = data.getString(data.getColumnIndex(Places.COLUMN_NAME));
            double latitude = data.getDouble(data.getColumnIndex(Places.COLUMN_LATITUDE));
            double longitude = data.getDouble(data.getColumnIndex(Places.COLUMN_LONGITUDE));
            int isAroundThePoint = data.getInt(data.getColumnIndex(Places.COLUMN_IS_AROUND_THE_POINT));
            int isAroundThePlayer = data.getInt(data.getColumnIndex(Places.COLUMN_IS_AROUND_THE_PLAYER));
            int isVisitedInThePast = data.getInt(data.getColumnIndex(Places.COLUMN_IS_VISITED_IN_THE_PAST));
            Log.d("Places row", rowId + "\t" + id + "\t" + isAroundThePoint + "\t" + isAroundThePlayer + "\t" + isVisitedInThePast + "\n");
            PlaceClusterItem placeClusterItem = new PlaceClusterItem(rowId, id, name, latitude, longitude);
            mClusterManager.addItem(placeClusterItem);
        }
        Log.d("==========", "========================================================");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}

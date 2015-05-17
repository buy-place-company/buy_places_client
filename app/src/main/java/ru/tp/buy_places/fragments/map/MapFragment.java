package ru.tp.buy_places.fragments.map;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
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
import ru.tp.buy_places.activities.PlaceActivity;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.map.ObjectRenderer;
import ru.tp.buy_places.map.PlaceClusterItem;
import ru.tp.buy_places.service.ServiceHelper;
import ru.tp.buy_places.service.resourses.Place;
import ru.tp.buy_places.service.resourses.Places;


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final int ALL_PLACES_LOADER_ID = 0;
    private static final int CHANGED_PLACE_LOADER_ID = 1;


    private ClusterManager<PlaceClusterItem> mClusterManager;
    private CustomInfoWindowAdapter mInfoWindowAdapter;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private Marker mMyPositionMarker;
    private ImageButton mMyLocationImageButton;
    private ImageButton mZoomInImageButton;
    private ImageButton mZoomOutImageButton;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity());
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mInfoWindowAdapter = new CustomInfoWindowAdapter(getActivity());
        getLoaderManager().initLoader(ALL_PLACES_LOADER_ID, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMyLocationImageButton = (ImageButton)rootView.findViewById(R.id.image_button_my_location);
        mMyLocationImageButton.setOnClickListener(this);
        mZoomInImageButton = (ImageButton)rootView.findViewById(R.id.image_button_zoom_in);
        mZoomInImageButton.setOnClickListener(this);
        mZoomOutImageButton = (ImageButton)rootView.findViewById(R.id.image_button_zoom_out);
        mZoomOutImageButton.setOnClickListener(this);
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
        mInfoWindowAdapter = new CustomInfoWindowAdapter(getActivity());
        mGoogleMap = googleMap;
        mClusterManager = new ClusterManager<>(getActivity(), mGoogleMap);
        mClusterManager.setRenderer(new ObjectRenderer(getActivity(), mGoogleMap, mClusterManager, mInfoWindowAdapter));
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mClusterManager.onCameraChange(cameraPosition);
                ServiceHelper.get(getActivity()).getPlacesAroundThePoint(cameraPosition.target);
            }
        });
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.setInfoWindowAdapter(mInfoWindowAdapter);
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), PlaceActivity.class);
                PlaceClusterItem item = mInfoWindowAdapter.getItem(marker);
                if (item !=  null) {
                    Long id = (Long)item.getRowId();
                    intent.putExtra("EXTRA_PLACE_ID", id.toString());
                } else intent.putExtra("EXTRA_PLACE_ID", "ANONYMOUS");
                startActivity(intent);
            }
        });

        if (mLocationManager != null) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10.f, this);
            Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation != null) {
                ServiceHelper.get(getActivity()).getPlacesAroundThePlayer(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
                mMyPositionMarker = mGoogleMap.addMarker(new MarkerOptions().title("You are here").position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMyPositionMarker.getPosition(), 15f));

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        ServiceHelper.get(getActivity()).getPlacesAroundThePlayer(new LatLng(location.getLatitude(), location.getLongitude()));
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
        return new CursorLoader(getActivity(), BuyPlacesContract.Places.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mClusterManager != null)
            mClusterManager.clearItems();
        Places places = Places.fromCursor(data);
        for (Place place: places.getPlaces()) {
            PlaceClusterItem placeClusterItem = new PlaceClusterItem(place);
            mClusterManager.addItem(placeClusterItem);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_button_my_location:
                if (mGoogleMap != null && mMyPositionMarker != null)
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mMyPositionMarker.getPosition()));
                break;
            case R.id.image_button_zoom_in:
                if (mGoogleMap != null)
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;
            case R.id.image_button_zoom_out:
                if (mGoogleMap != null)
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }
}

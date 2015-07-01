package ru.tp.buy_places.fragments.map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashMap;
import java.util.Map;

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
    private static final String MAP_FILTER_SHARED_PREFERENCES = "MAP_FILTER";

    private ClusterManager<PlaceClusterItem> mClusterManager;
    private CustomInfoWindowAdapter mInfoWindowAdapter;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private Marker mMyPositionMarker;
    private ImageButton mMyLocationImageButton;
    private ImageButton mZoomInImageButton;
    private ImageButton mZoomOutImageButton;
    private SharedPreferences mSharedPreferences;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;


    private CoordinatorLayout.LayoutParams mOldLayoutParams;
    private CoordinatorLayout.LayoutParams mNewLayoutParams;
    private FrameLayout mFrameLayout;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mInfoWindowAdapter = new CustomInfoWindowAdapter(getActivity());
        mSharedPreferences = getActivity().getSharedPreferences(MAP_FILTER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(ALL_PLACES_LOADER_ID, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                final Map<Integer, Boolean> currentState = new HashMap<>();
                AlertDialog d = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.places_filter_dialog_title)
                        .setMultiChoiceItems(getResources().getStringArray(R.array.filter), null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                            }
                        })
                        .setPositiveButton(R.string.positive_button_title, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton(R.string.cancel_button_title, null)
                        .show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
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
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        mGoogleMap.setInfoWindowAdapter(mInfoWindowAdapter);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
//        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                Intent intent = new Intent(getActivity(), PlaceActivity.class);
//                PlaceClusterItem item = mInfoWindowAdapter.getItem(marker);
//                if (item != null) {
//                    Long id = item.getRowId();
//                    intent.putExtra("EXTRA_PLACE_ID", id);
//                    startActivity(intent);
//                }
//
//            }
//        });

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<PlaceClusterItem>() {
            @Override
            public boolean onClusterItemClick(PlaceClusterItem placeClusterItem) {
                Intent intent = new Intent(getActivity(), PlaceActivity.class);
                if (placeClusterItem != null) {
                    Long id = placeClusterItem.getRowId();
                    intent.putExtra("EXTRA_PLACE_ID", id);
                    startActivity(intent);
                    return true;
                }
                return false;

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

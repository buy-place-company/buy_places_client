package ru.tp.buy_places.fragments.map;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.tp.buy_places.R;
import ru.tp.buy_places.activities.VenueActivity;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.map.CustomInfoWindowAdapter;
import ru.tp.buy_places.map.LocationApiConnectionListener;
import ru.tp.buy_places.map.LocationProvider;
import ru.tp.buy_places.map.VenueClusterItem;
import ru.tp.buy_places.map.VenuesRenderer;
import ru.tp.buy_places.map.marker.PlayerLocationMarkerOptionsCreator;
import ru.tp.buy_places.service.ServiceHelper;
import ru.tp.buy_places.service.resourses.Place;
import ru.tp.buy_places.service.resourses.Places;
import ru.tp.buy_places.service.resourses.Player;
import ru.tp.buy_places.utils.AccountManagerHelper;


public class MapFragment extends Fragment implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, LocationApiConnectionListener.OnLocationChangedListener, DialogInterface.OnClickListener {

    private static final int ALL_PLACES_LOADER_ID = 0;
    private static final int PLAYER_LOADER_ID = 1;

    private static final String EXTRA_PLAYER_ID = "EXTRA_PLAYER_ROW_ID";
    private static final String ARG_FILTER = "ARG_FILTER";

    private ClusterManager<VenueClusterItem> mClusterManager;
    private CustomInfoWindowAdapter mInfoWindowAdapter;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private Marker mMyPositionMarker;
    private ImageButton mMyLocationImageButton;
    private ImageButton mZoomInImageButton;
    private ImageButton mZoomOutImageButton;

    private LocationProvider mLocationProvider;
    private PlayerLocationMarkerOptionsCreator mPlayerLocationMarkerOptionsCreator;
    private Player mPlayer;
    private ArrayList<Integer> mSelectedFilterItems = new ArrayList<>();


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
            try {
                mLocationProvider = (LocationProvider)activity;
                mLocationProvider.addLocationListener(this);
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement LocationProvider");
            }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLocationProvider.removeLocationListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayerLocationMarkerOptionsCreator = new PlayerLocationMarkerOptionsCreator(getActivity());
        mInfoWindowAdapter = new CustomInfoWindowAdapter(getActivity());
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(ALL_PLACES_LOADER_ID, null, this);
        final Bundle arguments = new Bundle();
        arguments.putLong(EXTRA_PLAYER_ID, AccountManagerHelper.getPlayerId(getActivity()));
        getLoaderManager().initLoader(PLAYER_LOADER_ID, arguments, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                final String [] filterItems = getResources().getStringArray(R.array.filter);
                boolean[] selected = new boolean[filterItems.length];
                for (Integer i : mSelectedFilterItems) {
                    selected[i] = true;
                }

                final ArrayList<Integer> items = mSelectedFilterItems;
                AlertDialog d = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.places_filter_dialog_title)
                        .setMultiChoiceItems(filterItems, selected, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    items.add(which);
                                } else if (items.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    items.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        .setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!items.isEmpty()) {
                                    Bundle args = new Bundle();
                                    args.putIntegerArrayList(ARG_FILTER, mSelectedFilterItems);
                                    getLoaderManager().restartLoader(ALL_PLACES_LOADER_ID, args, MapFragment.this);
                                } else {
                                    getLoaderManager().restartLoader(ALL_PLACES_LOADER_ID, null, MapFragment.this);
                                }
                                mSelectedFilterItems = items;
                            }
                        })
                        .setNeutralButton(R.string.dialog_cancel_button_title, null)
                        .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mMyLocationImageButton = (ImageButton) rootView.findViewById(R.id.image_button_my_location);
        mMyLocationImageButton.setOnClickListener(this);
        mZoomInImageButton = (ImageButton) rootView.findViewById(R.id.image_button_zoom_in);
        mZoomInImageButton.setOnClickListener(this);
        mZoomOutImageButton = (ImageButton) rootView.findViewById(R.id.image_button_zoom_out);
        mZoomOutImageButton.setOnClickListener(this);

        return rootView;
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
        mClusterManager.setRenderer(new VenuesRenderer(getActivity(), mGoogleMap, mClusterManager, mInfoWindowAdapter));
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mClusterManager.onCameraChange(cameraPosition);
                ServiceHelper.get(getActivity()).getVenuesAroundThePoint(cameraPosition.target);
            }
        });
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        mGoogleMap.setInfoWindowAdapter(mInfoWindowAdapter);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
        mGoogleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<VenueClusterItem>() {
            @Override
            public void onClusterItemInfoWindowClick(VenueClusterItem venueClusterItem) {
                if (venueClusterItem != null) {
                    VenueActivity.VenueType venueType = VenueActivity.VenueType.fromVenue(venueClusterItem.getPlace());
                    VenueActivity.start(MapFragment.this, venueClusterItem.getRowId(), venueClusterItem.getPosition(), venueType);
                }
            }
        });
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<VenueClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<VenueClusterItem> cluster) {
                List<VenueClusterItem> venues = new ArrayList<>(cluster.getItems());
                final ListAdapter adapter = new ClusterPopupListAdapter(getActivity(), venues);
                new AlertDialog.Builder(getActivity()).setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final VenueClusterItem venueClusterItem = ((VenueClusterItem)adapter.getItem(which));
                        VenueActivity.VenueType venueType = VenueActivity.VenueType.fromVenue(venueClusterItem.getPlace());
                        VenueActivity.start(MapFragment.this, venueClusterItem.getRowId(), venueClusterItem.getPosition(), venueType);
                    }
                }).show();
                return true;
            }
        });
        mLocationProvider.requestLastKnownLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mGoogleMap != null && location != null) {
            if (mMyPositionMarker == null) {
                mPlayerLocationMarkerOptionsCreator.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                mMyPositionMarker = mGoogleMap.addMarker(mPlayerLocationMarkerOptionsCreator.create(new MarkerOptions()));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMyPositionMarker.getPosition(), getResources().getInteger(R.integer.google_map_default_zoom_level)));
            } else {
                mMyPositionMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ALL_PLACES_LOADER_ID:
                if (args != null) {
                    ArrayList<Integer> selected  = args.getIntegerArrayList(ARG_FILTER);
                    StringBuilder selectionBuilder = new StringBuilder();
                    ArrayList<String> selectionArgs = new ArrayList<>();
                    Iterator<Integer> iterator = selected.iterator();
                    while (iterator.hasNext()) {
                        Integer i = iterator.next();
                        switch (i) {
                            case 0:
                                selectionBuilder.append(BuyPlacesContract.Places.WITH_HIGH_BUY_PRICE_LIMIT);
                                selectionArgs.add(String.valueOf(mPlayer.getCash()));
                                break;
                            case 1:
                                selectionBuilder.append(BuyPlacesContract.Places.IS_IN_OWNERSHIP_SELECTION);
                                break;
                            case 2:
                                selectionBuilder.append(BuyPlacesContract.Places.FAVOURITE_SELECTION);
                                break;
                        }
                        if (iterator.hasNext())
                            selectionBuilder.append(" OR ");
                    }
                    String[] selectionArgsArray = selectionArgs.toArray(new String[selectionArgs.size()]);
                    return new CursorLoader(getActivity(), BuyPlacesContract.Places.CONTENT_URI, null, "(" + BuyPlacesContract.Places.IMPORTANT_SELECTION + ")" + " AND " + "(" + selectionBuilder.toString() + ")", selectionArgsArray, null);
                } else {
                    return new CursorLoader(getActivity(), BuyPlacesContract.Places.CONTENT_URI, null, BuyPlacesContract.Places.IMPORTANT_SELECTION, null, null);
                }
            case PLAYER_LOADER_ID:
                final long playerId = args.getLong(EXTRA_PLAYER_ID);
                return new CursorLoader(getActivity(), BuyPlacesContract.Players.CONTENT_URI, null, BuyPlacesContract.Players.WITH_SPECIFIED_ID_SELECTION, new String[]{Long.toString(playerId)}, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case ALL_PLACES_LOADER_ID:
                if (mClusterManager != null) {
                    mClusterManager.clearItems();
                    mInfoWindowAdapter.clearItems();
                    Places places = Places.fromCursor(data);
                    for (Place place : places.getPlaces()) {
                        VenueClusterItem venueClusterItem = new VenueClusterItem(place);
                        mClusterManager.addItem(venueClusterItem);
                    }
                    mClusterManager.cluster();
                }
                break;
            case PLAYER_LOADER_ID:
                if (data.moveToFirst()) {
                    mPlayer = Player.fromCursor(data);
                }
                // Todo Update UI interface
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

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}

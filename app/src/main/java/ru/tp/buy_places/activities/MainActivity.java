package ru.tp.buy_places.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Set;

import ru.tp.buy_places.R;
import ru.tp.buy_places.fragments.deals.DealsFragment;
import ru.tp.buy_places.fragments.map.MapFragment;
import ru.tp.buy_places.fragments.objects.PlaceListFragment;
import ru.tp.buy_places.fragments.raiting.RaitingFragment;
import ru.tp.buy_places.fragments.settings.SettingFragment;
import ru.tp.buy_places.map.LocationApiConnectionListener;
import ru.tp.buy_places.map.LocationProvider;
import ru.tp.buy_places.service.ServiceHelper;

public class MainActivity extends AppCompatActivity implements
        LocationApiConnectionListener.GoogleApiClientHolder,
        LocationApiConnectionListener.OnLocationChangedListener,
        LocationProvider,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private GoogleApiClient mGoogleApiClient;

    private Set<LocationApiConnectionListener.OnLocationChangedListener> mOnLocationChangedListeners = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationView = (NavigationView)findViewById(R.id.navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        if (mToolbar != null)
            setSupportActionBar(mToolbar);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mNavigationView.setNavigationItemSelectedListener(this);
        showPage(Page.MAP);

        final LocationApiConnectionListener mLocationApiConnectionListener = new LocationApiConnectionListener(this, this, this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(mLocationApiConnectionListener)
                .addOnConnectionFailedListener(mLocationApiConnectionListener)
                .build();
        ServiceHelper.get(this).getMyPlaces();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private void showPage(Page page) {
        Fragment fragment = getFragmentManager().findFragmentByTag(page.name());
        if (fragment == null) {
            switch (page) {
                case MAP:
                    fragment = new MapFragment();
                    break;
                case MY_OBJECTS:
                    fragment = new PlaceListFragment();
                    break;
                case DEALS:
                    fragment = new DealsFragment();
                    break;
                case RATING:
                    fragment = new RaitingFragment();
                    break;
                case SETTINGS:
                    fragment = new SettingFragment();
                    break;
                default:
                    fragment = new MapFragment();
                    break;
            }
            getFragmentManager().beginTransaction().replace(R.id.content, fragment, page.name()).commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.navigation_map:
                showPage(Page.MAP);
                break;
            case R.id.navigation_places:
                showPage(Page.MY_OBJECTS);
                break;
            case R.id.navigation_deals:
                showPage(Page.DEALS);
                break;
            case R.id.navigation_rating:
                showPage(Page.RATING);
                break;
            case R.id.navigation_settings:
                showPage(Page.SETTINGS);
                break;
            default:
                showPage(Page.MAP);
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    public void onLocationChanged(Location location) {
        ServiceHelper.get(this).getPlacesAroundThePlayer(new LatLng(location.getLatitude(), location.getLongitude()));
        notifyLocationChanged(location);
    }

    @Override
    public void addLocationListener(LocationApiConnectionListener.OnLocationChangedListener listener) {
        mOnLocationChangedListeners.add(listener);
    }

    @Override
    public void removeLocationListener(LocationApiConnectionListener.OnLocationChangedListener listener) {
        mOnLocationChangedListeners.remove(listener);
    }

    @Override
    public void notifyLocationChanged(Location location) {
        for (LocationApiConnectionListener.OnLocationChangedListener listener: mOnLocationChangedListeners)
            listener.onLocationChanged(location);
    }

    @Override
    public void requestLastKnownLocation() {
        if (mGoogleApiClient != null)
            notifyLocationChanged(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
    }

    public enum Page {
        MAP,
        MY_OBJECTS,
        DEALS,
        RATING,
        SETTINGS
    }
}

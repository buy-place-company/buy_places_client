package ru.tp.buy_places.activities;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.tp.buy_places.R;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.fragments.deals.DealsFragment;
import ru.tp.buy_places.fragments.map.MapFragment;
import ru.tp.buy_places.fragments.objects.PlaceListFragment;
import ru.tp.buy_places.fragments.raiting.RaitingFragment;
import ru.tp.buy_places.fragments.settings.SettingFragment;
import ru.tp.buy_places.map.LocationApiConnectionListener;
import ru.tp.buy_places.map.LocationProvider;
import ru.tp.buy_places.service.ServiceHelper;
import ru.tp.buy_places.service.resourses.Player;
import ru.tp.buy_places.utils.AccountManagerHelper;

public class MainActivity extends AppCompatActivity implements
        LocationApiConnectionListener.GoogleApiClientHolder,
        LocationApiConnectionListener.OnLocationChangedListener,
        LocationProvider,
        NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        SettingFragment.OnLogoutClickListener {

    private Location mLastLocation = null;

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();


    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int PLAYER_LOADER_ID = 0;
    private static final String KEY_PLAYER_ID = "KEY_PLAYER_ID";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private GoogleApiClient mGoogleApiClient;

    private Set<LocationApiConnectionListener.OnLocationChangedListener> mOnLocationChangedListeners = new HashSet<>();
    private View mHeaderView;
    private CircleImageView mHeaderAvatar;
    private TextView mHeaderUsername;
    private TextView mHeaderScore;
    private TextView mHeaderCash;
    private long mLogoutRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView)findViewById(R.id.navigation);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setDisplayShowHomeEnabled(true);
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.header, null);
        mHeaderAvatar = (CircleImageView) mHeaderView.findViewById(R.id.image);
        mHeaderCash = (TextView) mHeaderView.findViewById(R.id.text_view_cash);
        mHeaderScore = (TextView) mHeaderView.findViewById(R.id.text_view_score);
        mHeaderUsername = (TextView) mHeaderView.findViewById(R.id.name);
        mNavigationView.addHeaderView(mHeaderView);

        mNavigationView.setNavigationItemSelectedListener(this);
        
        showPage(Page.MAP);
        final LocationApiConnectionListener mLocationApiConnectionListener = new LocationApiConnectionListener(this, this, this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(mLocationApiConnectionListener)
                .addOnConnectionFailedListener(mLocationApiConnectionListener)
                .build();
        long playerId = AccountManagerHelper.getPlayerId(this);
        Bundle args = new Bundle();
        args.putLong(KEY_PLAYER_ID, playerId);
        getLoaderManager().initLoader(PLAYER_LOADER_ID, args, this);
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
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void showPage(Page page) {
        Fragment fragment = getFragmentManager().findFragmentByTag(page.name());
        if (fragment == null) {
            switch (page) {
                case USER:
                    Intent intent = new Intent(this, UserActivity.class);
                    startActivity(intent);
                case MAP:
                    fragment = new MapFragment();
                    getSupportActionBar().setTitle(R.string.navigation_map);
                    break;
                case MY_OBJECTS:
                    fragment = new PlaceListFragment();
                    getSupportActionBar().setTitle(R.string.navigation_venues);
                    break;
                case DEALS:
                    fragment = new DealsFragment();
                    getSupportActionBar().setTitle(R.string.navigation_deals);
                    break;
                case RATING:
                    getSupportActionBar().setTitle(R.string.navigation_rating);
                    fragment = new RaitingFragment();
                    break;
                case SETTINGS:
                    fragment = new SettingFragment();
                    getSupportActionBar().setTitle(R.string.navigation_settings);
                    break;
                default:
                    fragment = new MapFragment();
                    getSupportActionBar().setTitle(R.string.navigation_map);
                    break;
            }
            getFragmentManager().beginTransaction().replace(R.id.content, fragment, page.name()).commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.tv_userphoto:
                showPage(Page.USER);
                break;
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
        if (mLastLocation == null || location.distanceTo(mLastLocation) > 1000) {
            ServiceHelper.get(this).getVenuesAroundThePlayer(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        notifyLocationChanged(location);
        mLastLocation = location;
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
        if (mGoogleApiClient != null) {
            Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastKnownLocation != null)
                notifyLocationChanged(lastKnownLocation);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final long playerId = args.getLong(KEY_PLAYER_ID);
        return new CursorLoader(this, BuyPlacesContract.Players.CONTENT_URI, BuyPlacesContract.Players.ALL_COLUMNS_PROJECTION, BuyPlacesContract.Players.WITH_SPECIFIED_ID_SELECTION, new String[]{Long.toString(playerId)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            final Player player = Player.fromCursor(data);
            if (!TextUtils.isEmpty(player.getAvatar())) {
                Picasso.with(this).load(player.getAvatar()).into(mHeaderAvatar);
            }
            mHeaderUsername.setText(player.getUsername());
            mHeaderCash.setText(Long.toString(player.getCash()));
            mHeaderScore.setText(Long.toString(player.getScore()));
            mHeaderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserActivity.start(MainActivity.this, player);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onLogoutClick() {
        AccountManagerHelper.removeAccount(MainActivity.this);
    }

    public enum Page {
        MAP,
        USER,
        MY_OBJECTS,
        DEALS,
        RATING,
        SETTINGS
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPreferences(MODE_PRIVATE).edit()
                .remove(DealsFragment.KEY_DEALS_PAGE)
                .remove(PlaceListFragment.KEY_VENUES_PAGE)
                .apply();
    }
}

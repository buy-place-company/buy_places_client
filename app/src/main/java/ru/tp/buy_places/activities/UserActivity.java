package ru.tp.buy_places.activities;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import ru.tp.buy_places.R;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.resourses.Place;
import ru.tp.buy_places.service.resourses.Player;

public class UserActivity extends ActionBarActivity implements OnMapReadyCallback,  LoaderManager.LoaderCallbacks<Cursor> {
    private MapView mMapView;
    private Player mPlayer;

    private static final int USER_LOADER_ID = 0;
    private static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        final long userId = intent.getLongExtra("EXTRA_USER_ID", -1);
        Bundle args = new Bundle();
        args.putLong(EXTRA_USER_ID, userId);
        getSupportLoaderManager().initLoader(USER_LOADER_ID, args, this);
        MapsInitializer.initialize(this);

        if (mMapView != null)
            mMapView.getMapAsync(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final long userId = args.getLong(EXTRA_USER_ID);
        final Uri userUri = ContentUris.withAppendedId(BuyPlacesContract.Players.CONTENT_URI, userId);
        return new CursorLoader(this, userUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            mPlayer = Player.fromCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

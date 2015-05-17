package ru.tp.buy_places.activities;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import ru.tp.buy_places.R;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.resourses.Place;

public class PlaceActivity extends ActionBarActivity implements OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PLACE_LOADER_ID = 0;
    private static final String EXTRA_PLACES_ROW_ID = "EXTRA_PLACES_ROW_ID";
    private TextView placeName;
    private Button upgrade;
    private Button sell;

    public static final String DIALOG = "Сделка";
   private AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
        Intent intent = getIntent();
        long placeRowId = Long.parseLong(intent.getStringExtra("EXTRA_PLACE_ID"));
        placeName = (TextView)findViewById(R.id.text_view_object_name);
        if (intent.getStringExtra("EXTRA_PLACE_ID") != null) {
            placeName.setText(intent.getStringExtra("EXTRA_PLACE_ID"));
        }

        ad = new AlertDialog.Builder(this);
        ad.setTitle(DIALOG);

        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });

        upgrade = (Button)findViewById(R.id.button_upgrade);
        sell = (Button)findViewById(R.id.button_sell);
        OnClickListener upgradeListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.setMessage("Вы можете улучшить здание за 500р");
                ad.show();
            }
        };
        OnClickListener sellListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.setMessage("Вы можете продать здание за 50% стоимости");
                ad.show();
            }
        };

        upgrade.setOnClickListener(upgradeListener);
        sell.setOnClickListener(sellListener);
        Bundle args = new Bundle();
        args.putLong(EXTRA_PLACES_ROW_ID, placeRowId);
        getSupportLoaderManager().initLoader(PLACE_LOADER_ID, args, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_object, menu);

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
    public void onClick(View v) {

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final long placesRowId = args.getLong(EXTRA_PLACES_ROW_ID);
        final Uri placesUri = ContentUris.withAppendedId(BuyPlacesContract.Places.CONTENT_URI, placesRowId);
        return new CursorLoader(this, placesUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            Place place = Place.fromCursor(data);
            new Integer(3).toString();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

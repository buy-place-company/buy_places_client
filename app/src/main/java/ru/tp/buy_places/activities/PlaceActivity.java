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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import ru.tp.buy_places.R;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.resourses.Place;

public class PlaceActivity extends ActionBarActivity implements OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PLACE_LOADER_ID = 0;
    private static final String EXTRA_PLACES_ROW_ID = "EXTRA_PLACES_ROW_ID";
    private Place mPlace;
    private TextView placeName;
    private TextView owner;
    private TextView level;
    private TextView price;
    private TextView service;
    private TextView profit;
    private TextView income;
    private Button upgradePlace;
    private Button sellPlace;

    public static final String DIALOG = "Сделка";
    private AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);
        placeName = (TextView)findViewById(R.id.text_view_object_name);
        owner = (TextView)findViewById(R.id.text_view_owner);
        price = (TextView)findViewById(R.id.text_view_price_value);
        profit = (TextView)findViewById(R.id.text_view_profit_value);
        level = (TextView)findViewById(R.id.text_view_place_level);
        income = (TextView)findViewById(R.id.text_view_income_value);
        service = (TextView)findViewById(R.id.text_view_service_value);
        FrameLayout buttonContent = (FrameLayout)findViewById(R.id.button_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);

        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.buttons_my_place, buttonContent, true);

        Intent intent = getIntent();
        final long placeRowId = intent.getLongExtra("EXTRA_PLACE_ID", -1);
        Bundle args = new Bundle();
        args.putLong(EXTRA_PLACES_ROW_ID, placeRowId);
        getSupportLoaderManager().initLoader(PLACE_LOADER_ID, args, this);

        ad = new AlertDialog.Builder(this);
        ad.setTitle(DIALOG);
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {}
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {}
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {}
        });

        upgradePlace = (Button)findViewById(R.id.button_upgrade_place);
        sellPlace = (Button)findViewById(R.id.button_sell_place);
        owner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaceActivity.this, UserActivity.class);
                intent.putExtra("EXTRA_USER_ID", mPlace.getOwner().getId());
                startActivity(intent);
            }
        });

        upgradePlace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.setMessage("Вы можете улучшить здание за 500р");
                ad.show();
            }
        });
        sellPlace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.setMessage("Вы можете продать здание за 50% стоимости");
                ad.show();
            }
        });

        upgrade.setOnClickListener(upgradeListener);
        sell.setOnClickListener(sellListener);
        Bundle args = new Bundle();
        args.putLong(EXTRA_PLACES_ROW_ID, placeRowId);
        if (placeRowId > 0) {
            getSupportLoaderManager().initLoader(PLACE_LOADER_ID, args, this);
        }
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
            mPlace = Place.fromCursor(data);
            placeName.setText(mPlace.getName());
            owner.setText(mPlace.getOwner().getUsername());
            level.setText(Integer.toString(mPlace.getLevel()));
            price.setText(Long.toString(mPlace.getPrice()));
            service.setText(Long.toString(mPlace.getExpense()));
            income.setText(Long.toString(mPlace.getIncome()));
            profit.setText(Long.toString(mPlace.getIncome() - mPlace.getExpense()));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

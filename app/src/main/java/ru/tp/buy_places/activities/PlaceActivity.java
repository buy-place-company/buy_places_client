package ru.tp.buy_places.activities;

import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.tp.buy_places.R;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.ServiceHelper;
import ru.tp.buy_places.service.resourses.Place;

public class PlaceActivity extends AppCompatActivity implements OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback {
    public static final String EXTRA_VENUES_ROW_ID = "EXTRA_VENUES_ROW_ID";
    public static final String EXTRA_VENUES_LOCATION = "EXTRA_VENUES_LOCATION";
    public static final String EXTRA_VENUES_TYPE = "EXTRA_VENUES_TYPE";



    public static void start(Fragment fragment, long venuesRowId, LatLng venuesLocation, VenueType type) {
        Intent intent = new Intent(fragment.getActivity(), PlaceActivity.class);
        intent.putExtra(PlaceActivity.EXTRA_VENUES_ROW_ID, venuesRowId);
        intent.putExtra(PlaceActivity.EXTRA_VENUES_LOCATION, venuesLocation);
        intent.putExtra(PlaceActivity.EXTRA_VENUES_TYPE, type);
        fragment.startActivity(intent);
    }

    public static void start(Activity activity, long venuesRowId, LatLng venuesLocation, VenueType type) {
        Intent intent = new Intent(activity, PlaceActivity.class);
        intent.putExtra(PlaceActivity.EXTRA_VENUES_ROW_ID, venuesRowId);
        intent.putExtra(PlaceActivity.EXTRA_VENUES_LOCATION, venuesLocation);
        intent.putExtra(PlaceActivity.EXTRA_VENUES_TYPE, type);
        activity.startActivity(intent);
    }

    public static final String DIALOG = "Сделка";
    private static final int VENUE_LOADER_ID = 0;
    private Place mPlace;

    private VenueView mVenueView;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private FrameLayout mAppBarLayout;
    private VenueType mVenueType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);

        final long venuesRowId = getIntent().getLongExtra(EXTRA_VENUES_ROW_ID, 0);
        final LatLng venuesPosition = getIntent().getParcelableExtra(EXTRA_VENUES_LOCATION);
        final int zoomLevel = getResources().getInteger(R.integer.google_map_default_zoom_level);
        mVenueType = (VenueType) getIntent().getSerializableExtra(EXTRA_VENUES_TYPE);
        mAppBarLayout = (FrameLayout) findViewById(R.id.app_bar_layout);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions()
                .liteMode(true)
                .mapToolbarEnabled(false)
                .camera(new CameraPosition.Builder()
                        .target(venuesPosition)
                        .zoom(zoomLevel)
                        .build());
        mMapView = new MapView(this, googleMapOptions);
        mAppBarLayout.addView(mMapView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mVenueView = new VenueView(this);
        mVenueView.setNameTextView((TextView) findViewById(R.id.text_view_object_name));
        mVenueView.setOwnerTextView((TextView) findViewById(R.id.text_view_owner));
        mVenueView.setLevelTextView((TextView) findViewById(R.id.text_view_place_level));
        mVenueView.setButtonsContainerLayout((FrameLayout) findViewById(R.id.button_container));
        mVenueView.setStatisticsTableLayout((TableLayout) findViewById(R.id.table_layout_statistics));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        Bundle args = new Bundle();
        args.putLong(EXTRA_VENUES_ROW_ID, venuesRowId);
        if (venuesRowId != 0) {
            getLoaderManager().initLoader(VENUE_LOADER_ID, args, this);
        }

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
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
        final long placesRowId = args.getLong(EXTRA_VENUES_ROW_ID);
        final Uri placesUri = ContentUris.withAppendedId(BuyPlacesContract.Places.CONTENT_URI, placesRowId);
        return new CursorLoader(this, placesUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            mPlace = Place.fromCursor(data);
            mVenueView.getNameTextView().setText(mPlace.getName());
            if (mPlace.getOwner() != null)
                mVenueView.getOwnerTextView().setText(mPlace.getOwner().getUsername());
            mVenueView.getLevelTextView().setText(Integer.toString(mPlace.getLevel()));

            mVenueView.getStatisticsTableLayout().removeAllViews();
            mVenueView.getStatisticsTableLayout().addView(VenueView.createStatisticsTableRow(this, getString(R.string.statistics_price), Long.toString(mPlace.getPrice())));
            mVenueView.getStatisticsTableLayout().addView(VenueView.createStatisticsTableRow(this, getString(R.string.statistics_outcome), Long.toString(mPlace.getExpense())));
            mVenueView.getStatisticsTableLayout().addView(VenueView.createStatisticsTableRow(this, getString(R.string.statistics_income), Long.toString(mPlace.getIncome())));
            mVenueView.getStatisticsTableLayout().addView(VenueView.createStatisticsTableRow(this, getString(R.string.statistics_profit), Long.toString(mPlace.getIncome() - mPlace.getExpense())));
            LayoutInflater inflater = LayoutInflater.from(this);
            mVenueType = mPlace.isInOwnership() ? VenueType.MINE : mPlace.getOwner() == null ? VenueType.NOBODYS : VenueType.ANOTHERS;

            switch (mVenueType) {
                case MINE:
                    View mineVenueButtons = inflater.inflate(R.layout.buttons_my_place, mVenueView.getButtonsContainerLayout());
                    setMineVenueButtonsOnClickListeners(mineVenueButtons);
                    break;
                case ANOTHERS:
                    View anothersVenueButtons = inflater.inflate(R.layout.buttons_player_place, mVenueView.getButtonsContainerLayout());
                    setAnothersVenueButtonsOnClickListeners(anothersVenueButtons);
                    break;
                case NOBODYS:
                    final View nobodysVenueButtons = inflater.inflate(R.layout.buttons_nobody_place, mVenueView.getButtonsContainerLayout());
                    setNobodysVenueButtonsOnClickListeners(nobodysVenueButtons);
                    break;

            }
        }
    }

    private void setNobodysVenueButtonsOnClickListeners(View nobodysVenueButtons) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        Button buyVenueButton = (Button) nobodysVenueButtons.findViewById(R.id.button_buy_place);
        buyVenueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.setTitle(DIALOG);
                dialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        ServiceHelper.get(PlaceActivity.this).buyPlace(mPlace.getId());
                        Toast.makeText(PlaceActivity.this, "Запрос на покупку отправлен", Toast.LENGTH_LONG).show();
                    }
                });
                dialogBuilder.setNegativeButton(R.string.dialog_negative_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                    }
                });
                dialogBuilder.setCancelable(true);
                dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                    }
                });

                dialogBuilder.setMessage("Вы действительно хотите купить здание за " + mPlace.getPrice() + "?");
                dialogBuilder.show();
            }
        });
    }

    private void setAnothersVenueButtonsOnClickListeners(View anothersVenueButtons) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        Button repurchaseVenueButton = (Button) anothersVenueButtons.findViewById(R.id.button_rebuy_place);
        repurchaseVenueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.setTitle(DIALOG);
                dialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        ServiceHelper.get(PlaceActivity.this).suggestDeal(mPlace.getId());
                        Toast.makeText(PlaceActivity.this, "Запрос на заключение сделки отправлен", Toast.LENGTH_LONG).show();
                    }
                });
                dialogBuilder.setNegativeButton(R.string.dialog_negative_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                    }
                });
                dialogBuilder.setCancelable(true);
                dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                    }
                });

                dialogBuilder.setMessage("Вы действительно хотите выкупить это здание?");
                dialogBuilder.show();
            }
        });
    }

    private void setMineVenueButtonsOnClickListeners(View mineVenueButtons) {
        final AlertDialog.Builder sellDialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog.Builder upgradeDialogBuilder = new AlertDialog.Builder(this);
        Button upgradeVenueButton = (Button) mineVenueButtons.findViewById(R.id.button_upgrade_place);
        Button sellVenueButton = (Button) mineVenueButtons.findViewById(R.id.button_sell_place);
        upgradeVenueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                upgradeDialogBuilder.setTitle(DIALOG);
                upgradeDialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        ServiceHelper.get(PlaceActivity.this).upgradePlace(mPlace.getId());
                        Toast.makeText(PlaceActivity.this,
                                "Здание повысилось до " + Integer.toString(mPlace.getLevel() + 1) + " уровня",
                                Toast.LENGTH_LONG).show();
                    }
                });
                upgradeDialogBuilder.setNegativeButton(R.string.dialog_negative_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                    }
                });
                upgradeDialogBuilder.setCancelable(true);
                upgradeDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                upgradeDialogBuilder.setMessage("Вы можете улучшить здание за 500р");
                upgradeDialogBuilder.show();
            }
        });
        sellVenueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sellDialogBuilder.setTitle(DIALOG);
                sellDialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        ServiceHelper.get(PlaceActivity.this).sellPlace(mPlace.getId());
                        Toast.makeText(PlaceActivity.this, "Здание продано",
                                Toast.LENGTH_LONG).show();
                        //PlaceActivity.this.finish();
                    }
                });
                sellDialogBuilder.setNegativeButton(R.string.dialog_negative_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                    }
                });
                sellDialogBuilder.setCancelable(true);
                sellDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                    }
                });

                sellDialogBuilder.setMessage("Ваше здание будет продано за 50% от стоимости");
                sellDialogBuilder.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        final LatLng venuesLocation = getIntent().getParcelableExtra(EXTRA_VENUES_LOCATION);
        mGoogleMap.addMarker(new MarkerOptions().position(venuesLocation));
    }


    public enum VenueType {
        MINE,
        ANOTHERS,
        NOBODYS;

        public static VenueType fromVenue(Place venue) {
            return venue.isInOwnership() ? MINE : venue.getOwner() == null ? NOBODYS : ANOTHERS;
        }
    }
}

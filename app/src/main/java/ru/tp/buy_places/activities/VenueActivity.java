package ru.tp.buy_places.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ru.tp.buy_places.R;
import ru.tp.buy_places.SimpleSectionedRecyclerViewAdapter;
import ru.tp.buy_places.InfoListAdapter;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.ServiceHelper;
import ru.tp.buy_places.service.resourses.Place;

public class VenueActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback {
    public static final String EXTRA_VENUES_ROW_ID = "EXTRA_VENUES_ROW_ID";
    public static final String EXTRA_VENUES_LOCATION = "EXTRA_VENUES_LOCATION";
    public static final String EXTRA_VENUES_TYPE = "EXTRA_VENUES_TYPE";
    private static final String LOG_TAG = VenueActivity.class.getSimpleName();
    private RecyclerView mInfoList;
    private InfoListAdapter mInfoListAdapter;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FrameLayout mButtonsContainerLayout;


    private long mSellVenueRequestId = 0;
    private long mUpgradeVenueRequestId = 0;
    private long mBuyVenueRequestId = 0;
    private long mCollectLootRequestId = 0;
    private long mSuggestDealRequestId = 0;
    private CoordinatorLayout mCoordinatorLayout;
    private BroadcastReceiver mServiceResultReceiver;
    private Menu mMenu;


    public static void start(Fragment fragment, long venuesRowId, LatLng venuesLocation, VenueType type) {
        Intent intent = new Intent(fragment.getActivity(), VenueActivity.class);
        intent.putExtra(VenueActivity.EXTRA_VENUES_ROW_ID, venuesRowId);
        intent.putExtra(VenueActivity.EXTRA_VENUES_LOCATION, venuesLocation);
        intent.putExtra(VenueActivity.EXTRA_VENUES_TYPE, type);
        fragment.startActivity(intent);
    }

    public static void start(Activity activity, long venuesRowId, LatLng venuesLocation, VenueType type) {
        Intent intent = new Intent(activity, VenueActivity.class);
        intent.putExtra(VenueActivity.EXTRA_VENUES_ROW_ID, venuesRowId);
        intent.putExtra(VenueActivity.EXTRA_VENUES_LOCATION, venuesLocation);
        intent.putExtra(VenueActivity.EXTRA_VENUES_TYPE, type);
        activity.startActivity(intent);
    }

    public static final String DIALOG = "Сделка";
    private static final int VENUE_LOADER_ID = 0;
    private Place mPlace;

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private FrameLayout mMapContainer;
    private VenueType mVenueType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);
        final long venuesRowId = getIntent().getLongExtra(EXTRA_VENUES_ROW_ID, 0);
        final LatLng venuesPosition = getIntent().getParcelableExtra(EXTRA_VENUES_LOCATION);
        final int zoomLevel = getResources().getInteger(R.integer.google_map_default_zoom_level);
        mVenueType = (VenueType) getIntent().getSerializableExtra(EXTRA_VENUES_TYPE);
        mInfoList = (RecyclerView) findViewById(R.id.recycler_view_info);
        mInfoList.setLayoutManager(new LinearLayoutManager(this));
        mInfoListAdapter = new InfoListAdapter(this);
        mInfoList.setAdapter(mInfoListAdapter);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.app_bar_layout);
        mMapContainer = (FrameLayout) findViewById(R.id.map_container);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions()
                .liteMode(true)
                .mapToolbarEnabled(false)
                .camera(new CameraPosition.Builder()
                        .target(venuesPosition)
                        .zoom(zoomLevel)
                        .build());
        mMapView = new MapView(this, googleMapOptions);
        mMapContainer.addView(mMapView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mButtonsContainerLayout = (FrameLayout) findViewById(R.id.button_container);
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


        IntentFilter intentFilter = new IntentFilter(ServiceHelper.ACTION_REQUEST_RESULT);
        mServiceResultReceiver = new VenueBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mServiceResultReceiver, intentFilter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.venue_favourite:
                if (mPlace.isFavourite())
                    ServiceHelper.get(this).removeVenueFromFavourites(mPlace.getId());
                else
                    ServiceHelper.get(this).addVenuesToFavourite(mPlace.getId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final long placesRowId = args.getLong(EXTRA_VENUES_ROW_ID);
        final Uri placesUri = ContentUris.withAppendedId(BuyPlacesContract.Places.CONTENT_URI, placesRowId);
        return new CursorLoader(this, placesUri, BuyPlacesContract.Places.WITH_OWNERS_COLUMNS_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            mPlace = Place.fromCursor(data);
            if (mMenu != null) {
                MenuItem favouriteMenuItem = mMenu.findItem(R.id.venue_favourite);
                if (mPlace.isFavourite())
                    favouriteMenuItem.setIcon(R.drawable.ic_star_white_18dp);
                else {
                    favouriteMenuItem.setIcon(R.drawable.ic_star_outline_white_18dp);
                }
            }
            mCollapsingToolbarLayout.setTitle(mPlace.getName());
            int commonHeaderPositionBasePosition = 0;
            int statisticsHeaderPositionBasePosition = commonHeaderPositionBasePosition;
            List<InfoListAdapter.InfoItem> infoItems = new ArrayList<>();
            if (mPlace.getOwner() != null) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_owner), InfoListAdapter.InfoType.PLAYER, mPlace.getOwner().getUsername(), mPlace.getOwner()));
                statisticsHeaderPositionBasePosition++;
            }
            if (mPlace.getBuyPrice() != null) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_buy_price), InfoListAdapter.InfoType.PRICE, Long.toString(mPlace.getBuyPrice())));
                statisticsHeaderPositionBasePosition++;
            }
            if (mPlace.getSellPrice() != null) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_sell_price), InfoListAdapter.InfoType.PRICE, Long.toString(mPlace.getSellPrice())));
                statisticsHeaderPositionBasePosition++;
            }
            if (mPlace.getUpgradePrice() != null) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_upgrade_price), InfoListAdapter.InfoType.PRICE, Long.toString(mPlace.getUpgradePrice())));
                statisticsHeaderPositionBasePosition++;
            }
            if (mPlace.getLoot() != null && mPlace.getMaxLoot() != null) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_loot), InfoListAdapter.InfoType.PRICE, Long.toString(mPlace.getLoot()) + " / " + Long.toString(mPlace.getMaxLoot())));
                statisticsHeaderPositionBasePosition++;
            }
            if (mPlace.getCheckinsCount() > 0) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_checkins), InfoListAdapter.InfoType.CHECKINS, Long.toString(mPlace.getCheckinsCount())));
                statisticsHeaderPositionBasePosition++;
            }
            if (mPlace.getLevel() >= 0) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_level), InfoListAdapter.InfoType.LEVEL, String.valueOf(mPlace.getLevel())));
                statisticsHeaderPositionBasePosition++;
            }
            if (mPlace.getIncome() != null) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_income), InfoListAdapter.InfoType.PRICE, Long.toString(mPlace.getIncome())));
            }
            if (mPlace.getExpense() != null) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_outcome), InfoListAdapter.InfoType.PRICE, Long.toString(mPlace.getExpense())));
            }
            if (mPlace.getIncome() != null && mPlace.getExpense() != null) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_profit), InfoListAdapter.InfoType.PRICE, Long.toString(mPlace.getIncome() - mPlace.getExpense())));
            }

            mInfoListAdapter.setInfoItems(infoItems);

            //This is the code to provide a sectioned list
            List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
            //Sections
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(commonHeaderPositionBasePosition, getString(R.string.venue_info_common)));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(statisticsHeaderPositionBasePosition, getString(R.string.venue_info_stats)));
            //Add your adapter to the sectionAdapter
            SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
            SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                    SimpleSectionedRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mInfoListAdapter);
            mSectionedAdapter.setSections(sections.toArray(dummy));

            //Apply this adapter to the RecyclerView
            mInfoList.setAdapter(mSectionedAdapter);

            LayoutInflater inflater = LayoutInflater.from(this);
            mVenueType = mPlace.isInOwnership() ? VenueType.MINE : mPlace.getOwner() == null ? VenueType.NOBODYS : VenueType.ANOTHERS;
            mButtonsContainerLayout.removeAllViews();
            switch (mVenueType) {
                case MINE:
                    View mineVenueButtons = inflater.inflate(R.layout.buttons_my_place, mButtonsContainerLayout);
                    setMineVenueButtonsOnClickListeners(mineVenueButtons);
                    break;
                case ANOTHERS:
                    View anothersVenueButtons = inflater.inflate(R.layout.buttons_player_place, mButtonsContainerLayout);
                    setAnothersVenueButtonsOnClickListeners(anothersVenueButtons);
                    break;
                case NOBODYS:
                    final View nobodysVenueButtons = inflater.inflate(R.layout.buttons_nobody_place, mButtonsContainerLayout);
                    setNobodysVenueButtonsOnClickListeners(nobodysVenueButtons);
                    break;

            }
        }
    }

    private void setNobodysVenueButtonsOnClickListeners(View nobodysVenueButtons) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        FloatingActionButton buyVenueButton = (FloatingActionButton) nobodysVenueButtons.findViewById(R.id.button_buy_place);
        buyVenueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.setTitle("Покупка");
                dialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        mBuyVenueRequestId = ServiceHelper.get(VenueActivity.this).buyVenue(mPlace.getId());
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

                dialogBuilder.setMessage("Вы действительно хотите купить здание за " + mPlace.getBuyPrice() + "?");
                dialogBuilder.show();
            }
        });
    }

    private void setAnothersVenueButtonsOnClickListeners(View anothersVenueButtons) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        FloatingActionButton repurchaseVenueButton = (FloatingActionButton) anothersVenueButtons.findViewById(R.id.button_rebuy_place);
        repurchaseVenueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.setTitle("Сделка");
                dialogBuilder.setMessage("Выкупить за:");
                TextInputLayout textInputLayout = (TextInputLayout) LayoutInflater.from(VenueActivity.this).inflate(R.layout.suggest_deal_dialog_view, null);
                dialogBuilder.setView(textInputLayout);
                dialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        TextInputLayout input = (TextInputLayout) ((Dialog) dialog).findViewById(R.id.text_input_layout_amount);
                        if (!TextUtils.isEmpty(input.getEditText().getText().toString())) {
                            long amount = Long.parseLong(input.getEditText().getText().toString());
                            ServiceHelper.get(VenueActivity.this).suggestDeal(mPlace.getId(), amount);
                        } else
                            Toast.makeText(VenueActivity.this, "Вы не ввели сумму", Toast.LENGTH_LONG).show();
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

                dialogBuilder.show();
            }
        });
    }

    private void setMineVenueButtonsOnClickListeners(View mineVenueButtons) {
        final AlertDialog.Builder sellDialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog.Builder upgradeDialogBuilder = new AlertDialog.Builder(this);
        //final AlertDialog.Builder suggestDealDialogBuilder = new AlertDialog.Builder(this);
        FloatingActionButton upgradeVenueButton = (FloatingActionButton) mineVenueButtons.findViewById(R.id.button_upgrade_place);
        FloatingActionButton sellVenueButton = (FloatingActionButton) mineVenueButtons.findViewById(R.id.button_sell_place);
        FloatingActionButton collectLootButton = (FloatingActionButton) mineVenueButtons.findViewById(R.id.button_collect_loot);
        //FloatingActionButton suggestDealButton = (FloatingActionButton) mineVenueButtons.findViewById(R.id.button_suggest_deal);
        upgradeVenueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                upgradeDialogBuilder.setTitle("Улучшение");
                upgradeDialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        mUpgradeVenueRequestId = ServiceHelper.get(VenueActivity.this).upgradeVenue(mPlace.getId());
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
                upgradeDialogBuilder.setMessage("Вы можете улучшить здание");
                upgradeDialogBuilder.show();
            }
        });
        sellVenueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sellDialogBuilder.setTitle("Продажа");
                sellDialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        mSellVenueRequestId = ServiceHelper.get(VenueActivity.this).sellVenue(mPlace.getId());
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

                sellDialogBuilder.setMessage("Ваше здание будет продано");
                sellDialogBuilder.show();
            }
        });
        collectLootButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollectLootRequestId = ServiceHelper.get(VenueActivity.this).collectLootFromPlace(mPlace.getId());
            }
        });
//        suggestDealButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                suggestDealDialogBuilder.setTitle("Сделка");
//                suggestDealDialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int arg1) {
//                        TextInputLayout input = (TextInputLayout) ((Dialog) dialog).findViewById(R.id.text_input_layout_amount);
//                        if (!TextUtils.isEmpty(input.getEditText().getText().toString())) {
//                            long amount = Long.parseLong(input.getEditText().getText().toString());
//                            mSuggestDealRequestId = ServiceHelper.get(VenueActivity.this).suggestDeal(mPlace.getId(), amount);
//                        } else
//                            Toast.makeText(VenueActivity.this, "Вы не ввели сумму", Toast.LENGTH_LONG).show();
//
//                    }
//                });
//                suggestDealDialogBuilder.setNegativeButton(R.string.dialog_negative_button_title, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int arg1) {
//                    }
//                });
//                suggestDealDialogBuilder.setCancelable(true);
//                suggestDealDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    public void onCancel(DialogInterface dialog) {
//                    }
//                });
//
//                suggestDealDialogBuilder.setMessage("Выставить на продажу за:");
//                TextInputLayout textInputLayout = (TextInputLayout) LayoutInflater.from(VenueActivity.this).inflate(R.layout.suggest_deal_dialog_view, null);
//                suggestDealDialogBuilder.setView(textInputLayout);
//                suggestDealDialogBuilder.show();
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mServiceResultReceiver);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        final LatLng venuesLocation = getIntent().getParcelableExtra(EXTRA_VENUES_LOCATION);
        mGoogleMap.addMarker(new MarkerOptions().position(venuesLocation));
        mGoogleMap.setOnMapClickListener(null);
        mGoogleMap.setOnMapLongClickListener(null);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
    }


    public enum VenueType {
        MINE,
        ANOTHERS,
        NOBODYS;

        public static VenueType fromVenue(Place venue) {
            return venue.isInOwnership() ? MINE : venue.getOwner() == null ? NOBODYS : ANOTHERS;
        }
    }

    private class VenueBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long requestId = intent.getLongExtra(ServiceHelper.EXTRA_REQUEST_ID, 0);
            int status = intent.getIntExtra(ServiceHelper.EXTRA_RESULT_CODE, 0);
            if (requestId == mCollectLootRequestId) {
                String collectLootMessage = getCollectLootMessage(VenueActivity.this, status);
                Snackbar.make(mCoordinatorLayout, collectLootMessage, Snackbar.LENGTH_LONG).setAction("OK", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
            } else if (requestId == mBuyVenueRequestId) {
                String buyVenueMessage = getBuyVenueMessage(VenueActivity.this, status);
                Snackbar.make(mCoordinatorLayout, buyVenueMessage, Snackbar.LENGTH_LONG).setAction("OK", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
            } else if (requestId == mUpgradeVenueRequestId) {
                String upgradeVenueMessage = getUpgradeVenueMessage(VenueActivity.this, status);
                Snackbar.make(mCoordinatorLayout, upgradeVenueMessage, Snackbar.LENGTH_LONG).setAction("OK", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
            } else if (requestId == mSellVenueRequestId) {
                String sellVenueMessage = getSellVenueMessage(VenueActivity.this, status);
                Snackbar.make(mCoordinatorLayout, sellVenueMessage, Snackbar.LENGTH_LONG).setAction("OK", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
            }
        }

        private String getCollectLootMessage(Context context, int status) {
            switch (status) {
                case 200:
                    return context.getString(R.string.message_collect_loot_success);
                default:
                    return context.getString(R.string.message_collect_loot_failure);
            }
        }

        private String getBuyVenueMessage(Context context, int status) {
            switch (status) {
                case 200:
                    return context.getString(R.string.message_buy_venue_success);
                default:
                    return context.getString(R.string.message_buy_venue_failure);
            }
        }

        private String getUpgradeVenueMessage(Context context, int status) {
            switch (status) {
                case 200:
                    return context.getString(R.string.message_upgrade_venue_success);
                default:
                    return context.getString(R.string.message_upgrade_venue_failure);
            }
        }

        private String getSellVenueMessage(Context context, int status) {
            switch (status) {
                case 200:
                    return context.getString(R.string.message_sell_venue_success);
                default:
                    return context.getString(R.string.message_sell_venue_failure);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_venue, menu);
        mMenu = menu;
        if (mPlace != null) {
            MenuItem favouriteMenuItem = mMenu.findItem(R.id.venue_favourite);
            if (mPlace.isFavourite())
                favouriteMenuItem.setIcon(R.drawable.ic_star_white_18dp);
            else {
                favouriteMenuItem.setIcon(R.drawable.ic_star_outline_white_18dp);
            }
        }
        return true;
    }
}

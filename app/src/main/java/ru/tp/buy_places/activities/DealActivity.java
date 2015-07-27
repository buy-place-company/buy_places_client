package ru.tp.buy_places.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.tp.buy_places.InfoListAdapter;
import ru.tp.buy_places.R;
import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.ServiceHelper;
import ru.tp.buy_places.service.resourses.Deal;
import ru.tp.buy_places.utils.AccountManagerHelper;

/**
 * Created by Ivan on 19.07.2015.
 */
public class DealActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_DEAL_ROW_ID = "EXTRA_DEAL_ROW_ID";
    private static final String ARG_DEAL_ROW_ID = "ARG_DEAL_ROW_ID";
    private static final int DEAL_LOADER_ID = 0;

    private Toolbar mToolbar;
    private FrameLayout mButtonsContainer;
    private Deal mDeal;
    private RecyclerView mInfoList;
    private InfoListAdapter mInfoListAdapter;


    public static void start(Context context, long dealRowId) {
        Intent intent = new Intent(context, DealActivity.class);
        intent.putExtra(EXTRA_DEAL_ROW_ID, dealRowId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mButtonsContainer = (FrameLayout) findViewById(R.id.button_container);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        final long dealRowId = getIntent().getLongExtra(EXTRA_DEAL_ROW_ID, 0);
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_DEAL_ROW_ID, dealRowId);
        getLoaderManager().initLoader(DEAL_LOADER_ID, arguments, this);
        mInfoList = (RecyclerView) findViewById(R.id.recycler_view_info);
        mInfoList.setLayoutManager(new LinearLayoutManager(this));
        mInfoListAdapter = new InfoListAdapter(this);
        mInfoList.setAdapter(mInfoListAdapter);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final long dealRowId = args.getLong(ARG_DEAL_ROW_ID);
        return new CursorLoader(this, BuyPlacesContract.Deals.CONTENT_URI, BuyPlacesContract.Deals.WITH_RELATED_ENTITIES_PROJECTION, BuyPlacesContract.Deals.WITH_SPECIFIED_ROW_ID_SELECTION, new String[]{Long.toString(dealRowId)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            mDeal = Deal.fromCursor(data);
            long playerId = AccountManagerHelper.getPlayerId(this);
            Deal.DealType dealType = playerId == mDeal.getPlayerFrom().getId()? Deal.DealType.OUTGOING:playerId==mDeal.getPlayerTo().getId()? Deal.DealType.INCOMING: Deal.DealType.ILLEGAL_STATE;
            final Deal.DealState dealState = mDeal.getState();
            LayoutInflater inflater = LayoutInflater.from(this);
            mButtonsContainer.removeAllViews();

            List<InfoListAdapter.InfoItem> infoItems = new ArrayList<>();
            if (mDeal.getPlayerFrom() != null) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_player_from), InfoListAdapter.InfoType.PLAYER, mDeal.getPlayerFrom().getUsername(), mDeal.getPlayerFrom()));
            }
            if (mDeal.getPlayerTo() != null) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_player_to), InfoListAdapter.InfoType.PLAYER, mDeal.getPlayerTo().getUsername(), mDeal.getPlayerTo()));
            }
            if (mDeal.getVenue() != null) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_venue), InfoListAdapter.InfoType.VENUE, mDeal.getVenue().getName(), mDeal.getVenue()));
            }

            if (mDeal.getAmount() >= 0) {
                infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_amount), InfoListAdapter.InfoType.PRICE, Long.toString(mDeal.getAmount())));
            }

            if (mDeal.getDateAdded() != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateAdded;
                String stringDateAdded;
                try {
                    dateAdded = format.parse(mDeal.getDateAdded());
                    stringDateAdded = new SimpleDateFormat("dd/mm/yyyy").format(dateAdded);
                } catch (ParseException e) {
                    stringDateAdded = null;
                    e.printStackTrace();
                }
                if (stringDateAdded != null)
                    infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_date_added), InfoListAdapter.InfoType.DATE, stringDateAdded));
            }

            if (mDeal.getDateExpired() != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateExpired;
                String stringDateExpired;
                try {
                    dateExpired = format.parse(mDeal.getDateExpired());
                    stringDateExpired = new SimpleDateFormat("dd/mm/yyyy").format(dateExpired);
                } catch (ParseException e) {
                    stringDateExpired = null;
                    e.printStackTrace();
                }
                if (stringDateExpired != null)
                    infoItems.add(new InfoListAdapter.InfoItem(getString(R.string.statistics_date_expires), InfoListAdapter.InfoType.DATE, stringDateExpired));
            }

            mInfoListAdapter.setInfoItems(infoItems);
            switch (dealType) {
                case INCOMING:

                    switch (dealState) {
                        case COMPLETED:
                            break;
                        case UNCOMPLETED:
                            final View incomingDealButtons = inflater.inflate(R.layout.buttons_incoming_deal, mButtonsContainer);
                            setIncomingButtonsOnClickListeners(incomingDealButtons);
                            break;
                        case REJECTED:
                            break;
                        case REVOKED:
                            break;
                    }
                    break;
                case OUTGOING:
                    switch (dealState) {
                        case COMPLETED:
                            break;
                        case UNCOMPLETED:
                            final View outgoingDealButtons = inflater.inflate(R.layout.buttons_outcoming_deal, mButtonsContainer);
                            setOutgoingButtonsOnClickListeners(outgoingDealButtons);
                            break;
                        case REJECTED:
                            break;
                        case REVOKED:
                            break;
                    }

                    break;
                case ILLEGAL_STATE:
                    throw new IllegalStateException();
            }
        }
    }

    private void setOutgoingButtonsOnClickListeners(View outgoingDealButtons) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final Button revokeDealButton = (Button) outgoingDealButtons.findViewById(R.id.button_revoke_deal);
        revokeDealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        ServiceHelper.get(DealActivity.this).revokeDeal(mDeal.getId());
                        Toast.makeText(DealActivity.this, "Запрос на заключение сделки отправлен", Toast.LENGTH_LONG).show();
                        DealActivity.this.finish();
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

                dialogBuilder.setMessage("Вы действительно хотите отменить сделку с игроком " + mDeal.getPlayerTo().getUsername() + "?");
                dialogBuilder.show();

            }
        });

    }

    private void setIncomingButtonsOnClickListeners(View incomingDealButtons) {
        final AlertDialog.Builder acceptDealDialogBuilder = new AlertDialog.Builder(this);
        final Button acceptDealButton = (Button) incomingDealButtons.findViewById(R.id.button_accept_deal);
        acceptDealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptDealDialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        ServiceHelper.get(DealActivity.this).acceptDeal(mDeal.getId());
                        Toast.makeText(DealActivity.this, "Запрос на заключение сделки отправлен", Toast.LENGTH_LONG).show();
                        DealActivity.this.finish();
                    }
                });
                acceptDealDialogBuilder.setNegativeButton(R.string.dialog_negative_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                    }
                });
                acceptDealDialogBuilder.setCancelable(true);
                acceptDealDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                    }
                });

                acceptDealDialogBuilder.setMessage("Вы действительно хотите принять предложение игрока " + mDeal.getPlayerFrom().getUsername() + "?");
                acceptDealDialogBuilder.show();

            }
        });

        final AlertDialog.Builder rejectDealDialogBuilder = new AlertDialog.Builder(this);
        final Button rejectDealButton = (Button) incomingDealButtons.findViewById(R.id.button_reject_deal);
        rejectDealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectDealDialogBuilder.setPositiveButton(R.string.dialog_positive_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        ServiceHelper.get(DealActivity.this).rejectDeal(mDeal.getId());
                        Toast.makeText(DealActivity.this, "Запрос на заключение сделки отправлен", Toast.LENGTH_LONG).show();
                        DealActivity.this.finish();
                    }
                });
                rejectDealDialogBuilder.setNegativeButton(R.string.dialog_negative_button_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                    }
                });
                rejectDealDialogBuilder.setCancelable(true);
                rejectDealDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                    }
                });

                rejectDealDialogBuilder.setMessage("Вы действительно хотите отклонить предложение игрока " + mDeal.getPlayerFrom().getUsername() + "?");
                rejectDealDialogBuilder.show();

            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package ru.tp.buy_places.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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
    private TextView mUser;
    private TextView mVenue;
    private TextView mAmount;
    private TextView mDate;
    private TextView mType;
    private TextView venueBuyPrice;
    private TextView venueSellPrice;
    private TextView venueLootAmount;


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

        final long dealRowId = getIntent().getLongExtra(EXTRA_DEAL_ROW_ID, 0);

        Bundle arguments = new Bundle();
        arguments.putLong(ARG_DEAL_ROW_ID, dealRowId);
        getLoaderManager().initLoader(DEAL_LOADER_ID, arguments, this);
        venueBuyPrice = (TextView)findViewById(R.id.tv_buy_amount);
        venueSellPrice = (TextView)findViewById(R.id.tv_sell_amount);
        venueLootAmount = (TextView)findViewById(R.id.tv_loot_amount);

        mUser = (TextView) findViewById(R.id.tv_user);
        mVenue = (TextView) findViewById(R.id.tv_venue);
        mAmount = (TextView) findViewById(R.id.tv_amount);
        mType = (TextView) findViewById(R.id.tv_type);
        mDate = (TextView) findViewById(R.id.tv_date);
        mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DealActivity.this, UserActivity.class);
                intent.putExtra("EXTRA_USER_ID", mDeal.getPlayerFrom().getRowId());
                startActivity(intent);
            }
        });
        mVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final long venuesRowId = mDeal.getVenue().getRowId();
                final LatLng venuesLocation = new LatLng(mDeal.getVenue().getLatitude(), mDeal.getVenue().getLongitude());
                final VenueActivity.VenueType venuesType = VenueActivity.VenueType.fromVenue(mDeal.getVenue());
                VenueActivity.start(DealActivity.this, venuesRowId, venuesLocation, venuesType);
            }
        });

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
            switch (dealType) {
                case INCOMING:
                    mUser.setText(mDeal.getPlayerFrom().getUsername());
                    mVenue.setText(mDeal.getVenue().getName());
                    mAmount.setText(Long.toString(mDeal.getAmount()));
                    venueBuyPrice.setText(Long.toString(mDeal.getVenue().getBuyPrice()));
                    venueSellPrice.setText(Long.toString(mDeal.getVenue().getSellPrice()));
                    venueLootAmount.setText(Long.toString(mDeal.getVenue().getMaxLoot()));
                    mType.setText(R.string.wantbuy);
                    switch (dealState) {
                        case COMPLETED:
                            mDate.setText(mDeal.getDateExpired());
                        //    mType.setText(R.string.complited);
                            break;
                        case UNCOMPLETED:
                            final View incomingDealButtons = inflater.inflate(R.layout.buttons_incoming_deal, mButtonsContainer);
                            setIncomingButtonsOnClickListeners(incomingDealButtons);
                            mDate.setText(mDeal.getDateAdded());
                            break;
                        case REJECTED:
                            break;
                        case REVOKED:
                            break;
                    }
                    break;
                case OUTGOING:
                    //venueBuyPrice.setText(Long.toString(mDeal.getVenue().getBuyPrice()));
                    mUser.setText(mDeal.getPlayerTo().getUsername());
                    mVenue.setText(mDeal.getVenue().getName());
                    mAmount.setText(Long.toString(mDeal.getAmount()));
                    venueLootAmount.setVisibility(View.GONE);
                    venueSellPrice.setVisibility(View.GONE);
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

                dialogBuilder.setMessage("Вы действительно хотите отменить сделку с игроком " + mDeal.getPlayerTo().getUsername()+ "?");
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
}

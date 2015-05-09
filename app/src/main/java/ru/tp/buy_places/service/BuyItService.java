package ru.tp.buy_places.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.google.android.gms.maps.model.LatLng;

import ru.tp.buy_places.service.places.PlacesProcessorCreator;


public class BuyItService extends IntentService implements Processor.OnProcessorResultListener {
    public static final String EXTRA_ORIGINAL_INTENT = "EXTRA_ORIGINAL_INTENT";

    private static final String EXTRA_SERVICE_CALLBACK = "EXTRA_SERVICE_CALLBACK";
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";

    private static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    private static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    private static final String EXTRA_POSITION = "EXTRA_POSITION";
    private static final String EXTRA_OBJECTS_REQUEST_MODE = "EXTRA_OBJECTS_REQUEST_MODE";

    private static final int REQUEST_INVALID = -1;
    private Intent mOriginalRequestIntent;
    private ResultReceiver mCallback;

    public BuyItService() {
        super("BuyItService");
    }


    private static final String ACTION_GET_OBJECTS = "ru.mail.buy_it.service.ACTION_GET_NEAREST_PLACES";
    private static final String ACTION_GET_PROFILE = "ru.mail.buy_it.service.ACTION_GET_PROFILE";
    private static final String ACTION_GET_USERS = "ru.mail.buy_it.service.ACTION_GET_USERS";
    private static final String ACTION_GET_DEALS = "ru.mail.buy_it.service.ACTION_GET_DEALS";

    private static final String ACTION_SELL_OBJECT = "ru.mail.buy_it.service.ACTION_SELL_PLACE";
    private static final String ACTION_UPGRADE_OBJECT = "ru.mail.buy_it.service.ACTION_SELL_PLACE";
    private static final String ACTION_BUY_OBJECT = "ru.mail.buy_it.service.ACTION_BUY_OBJECT";
    private static final String ACTION_COLLECT_LOOT = "ru.mail.buy_it.service.ACTION_COLLECT_LOOT";

    private static final String ACTION_SUGGEST_DEAL = "ru.mail.buy_it.service.ACTION_SUGGEST_DEAL";
    private static final String ACTION_REJECT_DEAL = "ru.mail.buy_it.service.ACTION_REJECT_DEAL";



    public static void startGetPlacesAroundThePlayerService(Context context, ResultReceiver serviceCallback, long requestId, LatLng position) {
        Intent intent = new Intent(ACTION_GET_OBJECTS, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_OBJECTS_REQUEST_MODE, ObjectsRequestMode.AROUND_THE_PLAYER);
        intent.putExtra(EXTRA_POSITION, position);
        context.startService(intent);
    }

    public static void startGetPlacesAroundThePointService(Context context, ResultReceiver serviceCallback, long requestId, LatLng position) {
        Intent intent = new Intent(ACTION_GET_OBJECTS, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_OBJECTS_REQUEST_MODE, ObjectsRequestMode.AROUND_THE_POINT);
        intent.putExtra(EXTRA_POSITION, position);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mOriginalRequestIntent = intent;
        mCallback = intent.getParcelableExtra(EXTRA_SERVICE_CALLBACK);
        String action = intent.getAction();
        switch (action) {
            case ACTION_GET_OBJECTS:
                final LatLng position = intent.getParcelableExtra(EXTRA_POSITION);
                final ObjectsRequestMode objectsRequestMode = (ObjectsRequestMode) intent.getSerializableExtra(EXTRA_OBJECTS_REQUEST_MODE);
                Processor placesProcessor = new PlacesProcessorCreator(this, this, position, objectsRequestMode).createProcessor();
                placesProcessor.process();
                break;
            default:
                mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
                break;
        }
    }

    @Override
    public void send(int resultCode) {
        if (mCallback != null) {
            mCallback.send(resultCode, getOriginalIntentBundle());
        }
    }


    protected Bundle getOriginalIntentBundle() {
        Bundle originalRequest = new Bundle();
        originalRequest.putParcelable(EXTRA_ORIGINAL_INTENT, mOriginalRequestIntent);
        return originalRequest;
    }


    public enum ObjectsRequestMode {
        AROUND_THE_PLAYER,
        AROUND_THE_POINT
    }
}
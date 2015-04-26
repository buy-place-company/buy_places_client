package ru.tp.buy_places.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import ru.tp.buy_places.service.places.PlacesProcessorCreator;


public class BuyItService extends IntentService implements Processor.OnProcessorResultListener {
    public static final String EXTRA_ORIGINAL_INTENT = "EXTRA_ORIGINAL_INTENT";

    private static final String EXTRA_SERVICE_CALLBACK = "EXTRA_SERVICE_CALLBACK";
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";

    private static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    private static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";

    private static final int REQUEST_INVALID = -1;
    private Intent mOriginalRequestIntent;
    private ResultReceiver mCallback;

    public BuyItService() {
        super("BuyItService");
    }


    private static final String ACTION_GET_NEAREST_OBJECTS = "ru.mail.buy_it.service.ACTION_GET_NEAREST_PLACES";
    private static final String ACTION_GET_OBJECTS = "ru.mail.buy_it.service.ACTION_GET_PLACES";
    private static final String ACTION_GET_PROFILE = "ru.mail.buy_it.service.ACTION_GET_PROFILE";
    private static final String ACTION_GET_USERS = "ru.mail.buy_it.service.ACTION_GET_USERS";
    private static final String ACTION_GET_DEALS = "ru.mail.buy_it.service.ACTION_GET_DEALS";

    private static final String ACTION_SELL_OBJECT = "ru.mail.buy_it.service.ACTION_SELL_PLACE";
    private static final String ACTION_UPGRADE_OBJECT = "ru.mail.buy_it.service.ACTION_SELL_PLACE";
    private static final String ACTION_BUY_OBJECT = "ru.mail.buy_it.service.ACTION_BUY_OBJECT";
    private static final String ACTION_COLLECT_LOOT = "ru.mail.buy_it.service.ACTION_COLLECT_LOOT";

    private static final String ACTION_SUGGEST_DEAL = "ru.mail.buy_it.service.ACTION_SUGGEST_DEAL";
    private static final String ACTION_REJECT_DEAL = "ru.mail.buy_it.service.ACTION_REJECT_DEAL";



    public static void startGetNearestObjectsService(Context context, ResultReceiver serviceCallback, long requestId) {
        Intent intent = new Intent(ACTION_GET_NEAREST_OBJECTS, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        context.startService(intent);
    }

    public static void startGetObjectsService(Context context, ResultReceiver serviceCallback, long requestId, double latitude, double longitude) {
        Intent intent = new Intent(ACTION_GET_OBJECTS, null, context, BuyItService.class);
        intent.putExtra(EXTRA_LATITUDE, latitude);
        intent.putExtra(EXTRA_LONGITUDE, longitude);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        mOriginalRequestIntent = intent;
        mCallback = intent.getParcelableExtra(EXTRA_SERVICE_CALLBACK);
        String action = intent.getAction();
        switch (action) {
            case ACTION_GET_NEAREST_OBJECTS:
                Processor nearestPlacesProcessor = new PlacesProcessorCreator(this, this, 33.2, 55.3).createProcessor();
                nearestPlacesProcessor.process();
                break;
            case ACTION_GET_OBJECTS:
                double latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0.);
                double longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.);
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

}

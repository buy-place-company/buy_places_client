package ru.tp.buy_places.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.google.android.gms.maps.model.LatLng;

import ru.tp.buy_places.service.action_with_place.ActionWithPlaceProcessorCreator;
import ru.tp.buy_places.service.authentication.AuthenticationProcessor;
import ru.tp.buy_places.service.authentication.AuthenticationProcessorCreator;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.places.PlacesProcessorCreator;
import ru.tp.buy_places.service.profile.GetProfileProcessorCreator;
import ru.tp.buy_places.service.resourses.AuthenticationResult;
import ru.tp.buy_places.utils.AccountManagerHelper;


public class BuyItService extends IntentService implements Processor.OnProcessorResultListener {
    public static final String EXTRA_ORIGINAL_INTENT = "EXTRA_ORIGINAL_INTENT";

    private static final String EXTRA_SERVICE_CALLBACK = "EXTRA_SERVICE_CALLBACK";
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";

    private static final String EXTRA_POSITION = "EXTRA_POSITION";
    private static final String EXTRA_OBJECTS_REQUEST_MODE = "EXTRA_OBJECTS_REQUEST_MODE";
    private static final String EXTRA_ACTION_WITH_OBJECT = "EXTRA_ACTION_WITH_OBJECT";
    private static final String EXTRA_OBJECT_ID = "EXTRA_OBJECT_ID";
    private static final String EXTRA_CODE = "EXTRA_CODE";

    private static final int REQUEST_INVALID = -1;
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_USERNAME = "EXTRA_USERNAME";
    private Intent mOriginalRequestIntent;
    private ResultReceiver mCallback;

    public BuyItService() {
        super("BuyItService");
    }


    private static final String ACTION_GET_OBJECTS = "ru.mail.buy_it.service.ACTION_GET_NEAREST_PLACES";
    private static final String ACTION_POST_OBJECT = "ru.mail.buy_it.service.ACTION_POST_OBJECT";
    private static final String ACTION_AUTHENTICATE = "ru.mail.buy_it.service.ACTION_AUTHENTICATE";

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

    public static void startGetMyPlacesService(Context context, ResultReceiver serviceCallback, long requestId) {
        Intent intent = new Intent(ACTION_GET_OBJECTS, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_OBJECTS_REQUEST_MODE, ObjectsRequestMode.IN_OWNERSHIP);
        context.startService(intent);
    }

    public static void startBuyPlaceService(Context context, ResultReceiver serviceCallback, long requestId, String id) {
        Intent intent = new Intent(ACTION_POST_OBJECT, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_ACTION_WITH_OBJECT, ActionWithPlace.BUY);
        intent.putExtra(EXTRA_OBJECT_ID, id);
        context.startService(intent);
    }

    public static void startSellPlaceService(Context context, ResultReceiver serviceCallback, long requestId, String id) {
        Intent intent = new Intent(ACTION_POST_OBJECT, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_ACTION_WITH_OBJECT, ActionWithPlace.SELL);
        intent.putExtra(EXTRA_OBJECT_ID, id);
        context.startService(intent);
    }

    public static void startUpgradePlaceService(Context context, ResultReceiver serviceCallback, long requestId, String id) {
        Intent intent = new Intent(ACTION_POST_OBJECT, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_ACTION_WITH_OBJECT, ActionWithPlace.UPGRADE);
        intent.putExtra(EXTRA_OBJECT_ID, id);
        context.startService(intent);
    }

    public static void startCollectLootFromPlaceService(Context context, ResultReceiver serviceCallback, long requestId, String id) {
        Intent intent = new Intent(ACTION_POST_OBJECT, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_ACTION_WITH_OBJECT, ActionWithPlace.COLLECT_LOOT);
        intent.putExtra(EXTRA_OBJECT_ID, id);
        context.startService(intent);
    }

    public static void startGetProfileService(Context context, ResultReceiver serviceCallback, long requestId) {
        Intent intent = new Intent(ACTION_GET_PROFILE, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        context.startService(intent);
    }

    public static void startAuthenticateService(Context context, ResultReceiver serviceCallback, long requestId, String code) {
        Intent intent = new Intent(ACTION_AUTHENTICATE, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_CODE, code);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mOriginalRequestIntent = intent;
        mCallback = intent.getParcelableExtra(EXTRA_SERVICE_CALLBACK);
        String action = intent.getAction();
        switch (action) {
            case ACTION_AUTHENTICATE:
                final String code = intent.getStringExtra(EXTRA_CODE);
                Processor authenticationProcessor = new AuthenticationProcessorCreator(this, new Processor.OnProcessorResultListener() {
                    @Override
                    public void send(Response response) {
                        AuthenticationResult result = (AuthenticationResult) response.getData().get(AuthenticationProcessor.KET_AUTHENTICATION_RESULT);
                        long id = result.getId();
                        String username = result.getUsername();
                        Bundle data = new Bundle();
                        data.putLong(EXTRA_ID, id);
                        data.putString(EXTRA_USERNAME, username);
                        mCallback.send(response.getStatus(), data);
                    }
                }, code).createProcessor();
                authenticationProcessor.process();
                break;
            case ACTION_GET_OBJECTS:
                final LatLng position = intent.getParcelableExtra(EXTRA_POSITION);
                final ObjectsRequestMode objectsRequestMode = (ObjectsRequestMode) intent.getSerializableExtra(EXTRA_OBJECTS_REQUEST_MODE);
                Processor placesProcessor = new PlacesProcessorCreator(this, this, position, objectsRequestMode).createProcessor();
                placesProcessor.process();
                break;
            case ACTION_POST_OBJECT:
                final String id = intent.getStringExtra(EXTRA_OBJECT_ID);
                final ActionWithPlace actionWithPlace = (ActionWithPlace) intent.getSerializableExtra(EXTRA_ACTION_WITH_OBJECT);
                Processor actionWithPlaceProcessor = new ActionWithPlaceProcessorCreator(this, this, id, actionWithPlace).createProcessor();
                actionWithPlaceProcessor.process();
                break;
            case ACTION_GET_PROFILE:
                final long playerId = AccountManagerHelper.getPlayerId(this);
                Processor getProfileProcessor = new GetProfileProcessorCreator(this, this).createProcessor();
                getProfileProcessor.process();
                break;
            default:
                mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
                break;
        }
    }

    @Override
    public void send(Response response) {
        if (mCallback != null) {
            mCallback.send(response.getStatus(), getOriginalIntentBundle());
        }
    }


    protected Bundle getOriginalIntentBundle() {
        Bundle originalRequest = new Bundle();
        originalRequest.putParcelable(EXTRA_ORIGINAL_INTENT, mOriginalRequestIntent);
        return originalRequest;
    }

    public enum ObjectsRequestMode {
        AROUND_THE_PLAYER,
        AROUND_THE_POINT,
        IN_OWNERSHIP
    }

    public enum ActionWithPlace {
        BUY,
        SELL,
        UPGRADE,
        COLLECT_LOOT
    }
}
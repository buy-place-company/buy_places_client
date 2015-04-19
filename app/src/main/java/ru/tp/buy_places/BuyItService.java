package ru.tp.buy_places;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;


public class BuyItService extends IntentService {
    public static final String EXTRA_RESOURCE_TYPE = "ru.tp.buy_places.service.EXTRA_RESOURCE_TYPE";
    public static final String EXTRA_METHOD = "ru.tp.buy_places.service.EXTRA_METHOD";
    public static final String EXTRA_SERVICE_CALLBACK = "ru.tp.buy_places.service.EXTRA_SERVICE_CALLBACK";
    public static final String EXTRA_ORIGINAL_INTENT = "ru.tp.buy_places.service.EXTRA_ORIGINAL_INTENT";
    private static final int REQUEST_INVALID = -1;
    private Intent mOriginalRequestIntent;
    private ResultReceiver mCallback;

    public BuyItService() {
        super("BuyItService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mOriginalRequestIntent = intent;

        Method method = (Method) intent.getSerializableExtra(EXTRA_METHOD);
        ResourceType resourceType = (ResourceType) intent.getSerializableExtra(EXTRA_RESOURCE_TYPE);
        mCallback = intent.getParcelableExtra(EXTRA_SERVICE_CALLBACK);

        switch (resourceType) {
            case NEAREST_OBJECTS:
                if (method.equals(Method.GET)) {
                    NearestPlacesProcessor processor = new NearestPlacesProcessor(getApplicationContext());
                    processor.getNearestPlaces(makeNearestPlacesProcessorCallback());
                } else {
                    mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
                }
            default:
                mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
                break;
        }
    }

    private NearestPlacesProcessorCallback makeNearestPlacesProcessorCallback() {
        NearestPlacesProcessorCallback callback = new NearestPlacesProcessorCallback() {

            @Override
            public void send(int resultCode) {
                if (mCallback != null) {
                    mCallback.send(resultCode, getOriginalIntentBundle());
                }
            }
        };
        return callback;
    }




    public enum Method {
        GET,
        POST
    }


    public enum ResourceType {
        NEAREST_OBJECTS
    }

    protected Bundle getOriginalIntentBundle() {
        Bundle originalRequest = new Bundle();
        originalRequest.putParcelable(EXTRA_ORIGINAL_INTENT, mOriginalRequestIntent);
        return originalRequest;
    }
}

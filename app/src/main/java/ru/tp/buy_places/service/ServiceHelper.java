package ru.tp.buy_places.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Ivan on 19.04.2015.
 */
public class ServiceHelper {

    private static final String NEAREST_OBJECTS = "NEAREST_OBJECTS";
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private static final String EXTRA_RESULT_CODE = "EXTRA_RESULT_CODE";
    private static final String ACTION_REQUEST_RESULT = "ACTION_REQUEST_RESULT";


    private static ServiceHelper instance;
    private final Context mContext;
    private AtomicLong mRequestIdGenerator = new AtomicLong();
    private Map<String, Long> mPendingRequests = new HashMap<>();

    private ServiceHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    public static ServiceHelper get(Context context) {
        if (instance == null)
            instance = new ServiceHelper(context);
        return instance;
    }

    public boolean isRequestPending(long requestId){
        return mPendingRequests.containsValue(requestId);
    }


    public long getObjectsAroundThePoint(LatLng position) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        mPendingRequests.put(NEAREST_OBJECTS, requestId);
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleGetObjectsAroundThePointResponse(resultCode, resultData);
            }
        };
        BuyItService.startGetPlacesAroundThePointService(mContext, serviceCallback, requestId, position);
        return requestId;
    }

    public long getObjectsAroundThePlayer(LatLng position) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        mPendingRequests.put(NEAREST_OBJECTS, requestId);
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleGetObjectsAroundThePlayerResponse(resultCode, resultData);
            }
        };
        BuyItService.startGetPlacesAroundThePlayerService(mContext, serviceCallback, requestId, position);
        return requestId;
    }

    private void handleGetObjectsAroundThePlayerResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(NEAREST_OBJECTS);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(originalRequestIntent);
        }
    }

    private void handleGetObjectsAroundThePointResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(NEAREST_OBJECTS);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(originalRequestIntent);
        }
    }
}

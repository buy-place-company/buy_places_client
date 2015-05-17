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

    private static final String OBJECTS = "OBJECTS";
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

    public boolean isRequestPending(long requestId) {
        return mPendingRequests.containsValue(requestId);
    }


    public long getPlacesAroundThePoint(LatLng position) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        mPendingRequests.put(OBJECTS, requestId);
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleGetObjectsAroundThePointResponse(resultCode, resultData);
            }
        };
        BuyItService.startGetPlacesAroundThePointService(mContext, serviceCallback, requestId, position);
        return requestId;
    }

    public long getPlacesAroundThePlayer(LatLng position) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        mPendingRequests.put(OBJECTS, requestId);
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleGetObjectsAroundThePlayerResponse(resultCode, resultData);
            }
        };
        BuyItService.startGetPlacesAroundThePlayerService(mContext, serviceCallback, requestId, position);
        return requestId;
    }

    public long getMyPlaces() {
        return 0;
    }

    public long getPlayersPlaces(long id) {
        return 0;
    }

    public long buyPlace(String id) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        mPendingRequests.put(OBJECTS, requestId);
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleBuyPlaceResponse(resultCode, resultData);
            }
        };
        BuyItService.startBuyPlaceService(mContext, serviceCallback, requestId, id);
        return requestId;
    }

    public long sellPlace(String id) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        mPendingRequests.put(OBJECTS, requestId);
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleSellPlaceResponse(resultCode, resultData);
            }
        };
        BuyItService.startSellPlaceService(mContext, serviceCallback, requestId, id);
        return requestId;
    }

    public long upgradePlace(String id) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        mPendingRequests.put(OBJECTS, requestId);
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleUpgradePlaceResponse(resultCode, resultData);
            }
        };
        BuyItService.startUpgradePlaceService(mContext, serviceCallback, requestId, id);
        return requestId;
    }

    public long collectLootFromPlace(String id) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        mPendingRequests.put(OBJECTS, requestId);
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleCollectLootFormPlaceResponse(resultCode, resultData);
            }
        };
        BuyItService.startCollectLootFromPlaceService(mContext, serviceCallback, requestId, id);
        return requestId;
    }

    public long getProfile() {
        return 0;
    }

    public long getRating() {
        return 0;
    }

    public long getDeals() {
        return 0;
    }

    public long acceptDeal(long id) {
        return 0;
    }

    public long rejectDeal(long id) {
        return 0;
    }

    private void handleUpgradePlaceResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(OBJECTS);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }

    private void handleCollectLootFormPlaceResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(OBJECTS);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }

    private void handleSellPlaceResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(OBJECTS);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }

    private void handleBuyPlaceResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(OBJECTS);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }

    private void handleGetObjectsAroundThePlayerResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(OBJECTS);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }

    private void handleGetObjectsAroundThePointResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(OBJECTS);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }
}

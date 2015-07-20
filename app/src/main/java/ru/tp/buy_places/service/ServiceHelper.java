package ru.tp.buy_places.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Ivan on 19.04.2015.
 */
public class ServiceHelper {
    private static final String LOG_TAG = ServiceHelper.class.getSimpleName();

    private static final String GET_VENUES = "GET_VENUES";
    private static final String PLAYERS = "PLAYERS";
    private static final String VENUES_AROUND_THE_POINT = "VENUES_AROUND_THE_POINT";
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private static final String EXTRA_RESULT_CODE = "EXTRA_RESULT_CODE";
    public static final String ACTION_REQUEST_RESULT = "ACTION_REQUEST_RESULT";

    private static final String DEALS = "DEALS";
    private static final String GET_VENUES_AROUND_THE_PLAYER = "GET_VENUES_AROUND_THE_PLAYER";
    private static final String GET_MY_VENUES = "GET_MY_VENUES";
    private static final String VENUES_AROUND_THE_PLAYER = "VENUES_AROUND_THE_PLAYER";
    private static final String MY_VENUES = "MY_VENUES";
    private static final String PLAYER_VENUES = "PLAYER_VENUES";
    private static final String POST_VENUE = "POST_VENUE";
    private static final String POST_AUTHENTICATE = "POST_AUTHENTICATE";
    private static final String RATING = "RATING";

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


    public long getVenuesAroundThePoint(LatLng pointPosition) {
        long requestId;
        if (mPendingRequests.containsKey(VENUES_AROUND_THE_POINT)) {
            requestId = mPendingRequests.get(VENUES_AROUND_THE_POINT);
        } else {
            requestId = mRequestIdGenerator.incrementAndGet();
            mPendingRequests.put(VENUES_AROUND_THE_POINT, requestId);
            ResultReceiver serviceCallback = new GetVenuesResultReceiver(null, VENUES_AROUND_THE_POINT);
            BuyItService.startGetVenuesAroundThePointService(mContext, serviceCallback, requestId, pointPosition);
        }
        return requestId;
    }

    public long getVenuesAroundThePlayer(LatLng playerPosition) {
        long requestId;
        if (mPendingRequests.containsKey(VENUES_AROUND_THE_PLAYER)) {
            return mPendingRequests.get(VENUES_AROUND_THE_PLAYER);
        } else {
            requestId = mRequestIdGenerator.incrementAndGet();
            mPendingRequests.put(GET_VENUES_AROUND_THE_PLAYER, requestId);
            ResultReceiver serviceCallback = new GetVenuesResultReceiver(null, VENUES_AROUND_THE_PLAYER);
            BuyItService.startGetVenuesAroundThePlayerService(mContext, serviceCallback, requestId, playerPosition);
        }
        return requestId;
    }

    public long getMyVenues() {
        long requestId;
        if (mPendingRequests.containsKey(MY_VENUES)) {
            requestId = mPendingRequests.get(MY_VENUES);
        } else {
            requestId = mRequestIdGenerator.incrementAndGet();
            mPendingRequests.put(GET_VENUES, requestId);
            ResultReceiver serviceCallback = new GetVenuesResultReceiver(null, MY_VENUES);
            BuyItService.startGetMyVenuesService(mContext, serviceCallback, requestId);
        }
        return requestId;
    }

    public long getPlayerVenues(long playerId) {
        long requestId;
        if (mPendingRequests.containsKey(PLAYER_VENUES)) {
            requestId = mPendingRequests.get(PLAYER_VENUES);
        } else {
            requestId = mRequestIdGenerator.incrementAndGet();
            mPendingRequests.put(GET_VENUES, requestId);
            ResultReceiver serviceCallback = new GetVenuesResultReceiver(null, PLAYER_VENUES);
            BuyItService.startGetPlayerVenuesService(mContext, serviceCallback, requestId, playerId);
        }
        return requestId;
    }

    public long authenticate(String code) {
        long requestId;
        if (mPendingRequests.containsKey(POST_AUTHENTICATE)) {
            requestId = mPendingRequests.get(POST_AUTHENTICATE);
        } else {
            requestId = mRequestIdGenerator.incrementAndGet();
            mPendingRequests.put(POST_AUTHENTICATE, requestId);
            ResultReceiver serviceCallback = new AuthenticationResultReceiver(null);
            BuyItService.startAuthenticateService(mContext, serviceCallback, requestId, code);
        }
        return requestId;
    }

    public long buyVenue(String id) {
        long requestId;
        if (mPendingRequests.containsKey(POST_VENUE)) {
            requestId = mPendingRequests.get(POST_VENUE);
        } else {
            requestId = mRequestIdGenerator.incrementAndGet();
            mPendingRequests.put(POST_VENUE, requestId);
            ResultReceiver serviceCallback = new VenueResultReceiver(null, POST_VENUE);
            BuyItService.startBuyVenueService(mContext, serviceCallback, requestId, id);
        }
        return requestId;
    }

    public long sellVenue(String id) {
        long requestId;
        if (mPendingRequests.containsKey(POST_VENUE)) {
            requestId = mPendingRequests.get(POST_VENUE);
        } else {
            requestId = mRequestIdGenerator.incrementAndGet();
            mPendingRequests.put(POST_VENUE, requestId);
            ResultReceiver serviceCallback = new VenueResultReceiver(null, POST_VENUE);
            BuyItService.startSellVenueService(mContext, serviceCallback, requestId, id);
        }
        return requestId;
    }

    public long upgradeVenue(String id) {
        long requestId;
        if (mPendingRequests.containsKey(POST_VENUE)) {
            requestId = mPendingRequests.get(POST_VENUE);
        } else {
            requestId = mRequestIdGenerator.incrementAndGet();
            mPendingRequests.put(POST_VENUE, requestId);
            ResultReceiver serviceCallback = new VenueResultReceiver(null, POST_VENUE);
            BuyItService.startUpgradeVenueService(mContext, serviceCallback, requestId, id);
        }
        return requestId;
    }

    public long collectLootFromPlace(String id) {
        long requestId;
        if (mPendingRequests.containsKey(POST_VENUE)) {
            requestId = mPendingRequests.get(POST_VENUE);
        } else {
            requestId = mRequestIdGenerator.incrementAndGet();
            mPendingRequests.put(POST_VENUE, requestId);
            ResultReceiver serviceCallback = new VenueResultReceiver(null, POST_VENUE);
            BuyItService.startUpgradeVenueService(mContext, serviceCallback, requestId, id);
        }
        return requestId;
    }

    public long acceptDeal(long id) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleAcceptDealResponse(resultCode, resultData);
            }
        };
        BuyItService.startAcceptDealService(mContext, serviceCallback, requestId, id);
        return requestId;
    }

    public long rejectDeal(long id) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleRejectDealResponse(resultCode, resultData);
            }
        };
        BuyItService.startRejectDealService(mContext, serviceCallback, requestId, id);
        return requestId;
    }

    public long suggestDeal(String venueId, long amount) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleSuggestDealResponse(resultCode, resultData);
            }
        };
        BuyItService.startSuggestDealService(mContext, serviceCallback, requestId, venueId, amount);
        return requestId;
    }

    public long revokeDeal(long id) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleSuggestDealResponse(resultCode, resultData);
            }
        };
        BuyItService.startRevokeDealService(mContext, serviceCallback, requestId, id);
        return requestId;
    }

    public long getProfile() {
        long requestId = mRequestIdGenerator.incrementAndGet();
        mPendingRequests.put(PLAYERS, requestId);
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleGetProfileResponse(resultCode, resultData);
            }
        };
        BuyItService.startGetProfileService(mContext, serviceCallback, requestId);
        return requestId;
    }

    public long getRating(long limit, long offset) {
        long requestId = mRequestIdGenerator.incrementAndGet();
        mPendingRequests.put(DEALS, requestId);
        ResultReceiver serviceCallback = new GetRatingResultReceiver(null, RATING);
        BuyItService.startGetRatingService(mContext, serviceCallback, requestId, limit, offset);
        return requestId;
    }

    public long getDeals() {
        long requestId = mRequestIdGenerator.incrementAndGet();
        mPendingRequests.put(DEALS, requestId);
        ResultReceiver serviceCallback = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleGetDealsResponse(resultCode, resultData);
            }
        };
        BuyItService.startGetDealsService(mContext, serviceCallback, requestId);
        return requestId;
    }

    private void handleAcceptDealResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(GET_VENUES);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }

    private void handleRejectDealResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(GET_VENUES);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }

    private void handleSuggestDealResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(GET_VENUES);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }

    private void handleRevokeDealResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(GET_VENUES);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }


    private void handleGetDealsResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(GET_VENUES);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }

    private void handleGetProfileResponse(int resultCode, Bundle resultData) {
        Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
        if (originalRequestIntent != null) {
            long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
            mPendingRequests.remove(PLAYERS);
            Intent result = new Intent(ACTION_REQUEST_RESULT);
            result.putExtra(EXTRA_REQUEST_ID, requestId);
            result.putExtra(EXTRA_RESULT_CODE, resultCode);
            mContext.sendBroadcast(result);
        }
    }

    private class GetVenuesResultReceiver extends ResultReceiver {

        private final String mResource;

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public GetVenuesResultReceiver(Handler handler, String resource) {
            super(handler);
            mResource = resource;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
            if (originalRequestIntent != null) {
                long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
                mPendingRequests.remove(mResource);
                Intent result = new Intent(ACTION_REQUEST_RESULT);
                result.putExtra(EXTRA_REQUEST_ID, requestId);
                result.putExtra(EXTRA_RESULT_CODE, resultCode);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(result);
            }
        }
    }

    private class AuthenticationResultReceiver extends ResultReceiver {

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public AuthenticationResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
            long id = resultData.getLong(BuyItService.EXTRA_ID);
            String username = resultData.getString(BuyItService.EXTRA_USERNAME);
            if (originalRequestIntent != null) {
                long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
                mPendingRequests.remove(GET_VENUES);
                Intent result = new Intent(ACTION_REQUEST_RESULT);
                result.putExtra(EXTRA_REQUEST_ID, requestId);
                result.putExtra(EXTRA_RESULT_CODE, resultCode);
                result.putExtra(BuyItService.EXTRA_ID, id);
                result.putExtra(BuyItService.EXTRA_USERNAME, username);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(result);
            }
        }
    }

    private class VenueResultReceiver extends ResultReceiver {
        private final String mResource;

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public VenueResultReceiver(Handler handler, String resource) {
            super(handler);
            mResource = resource;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
            if (originalRequestIntent != null) {
                long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
                mPendingRequests.remove(mResource);
                Intent result = new Intent(ACTION_REQUEST_RESULT);
                result.putExtra(EXTRA_REQUEST_ID, requestId);
                result.putExtra(EXTRA_RESULT_CODE, resultCode);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(result);
            }
        }
    }

    private class GetRatingResultReceiver extends ResultReceiver {

        private final String mResource;

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public GetRatingResultReceiver(Handler handler, String resource) {
            super(handler);
            mResource = resource;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Intent originalRequestIntent = resultData.getParcelable(BuyItService.EXTRA_ORIGINAL_INTENT);
            if (originalRequestIntent != null) {
                long requestId = originalRequestIntent.getLongExtra(EXTRA_REQUEST_ID, 0);
                mPendingRequests.remove(mResource);
                Intent result = new Intent(ACTION_REQUEST_RESULT);
                result.putExtra(EXTRA_REQUEST_ID, requestId);
                result.putExtra(EXTRA_RESULT_CODE, resultCode);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(result);
            }
        }
    }
}

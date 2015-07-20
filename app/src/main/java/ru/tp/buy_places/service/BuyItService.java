package ru.tp.buy_places.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.google.android.gms.maps.model.LatLng;

import ru.tp.buy_places.service.action_with_deal.ActionWithDealProcessorCreator;
import ru.tp.buy_places.service.action_with_deal.SuggestDealProcessorCreator;
import ru.tp.buy_places.service.action_with_place.ActionWithPlaceProcessorCreator;
import ru.tp.buy_places.service.authentication.AuthenticationProcessor;
import ru.tp.buy_places.service.authentication.AuthenticationProcessorCreator;
import ru.tp.buy_places.service.deals.DealsProcessorCreator;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.places.GetMyVenuesProcessorCreator;
import ru.tp.buy_places.service.places.GetPlayerVenuesProcessorCreator;
import ru.tp.buy_places.service.places.GetVenuesAroundThePlayerProcessorCreator;
import ru.tp.buy_places.service.places.GetVenuesAroundThePointProcessorCreator;
import ru.tp.buy_places.service.profile.GetProfileProcessorCreator;
import ru.tp.buy_places.service.rating.RatingProcessorCreator;
import ru.tp.buy_places.service.resourses.AuthenticationResult;


public class BuyItService extends IntentService {
    public static final String EXTRA_ORIGINAL_INTENT = "EXTRA_ORIGINAL_INTENT";

    private static final String EXTRA_SERVICE_CALLBACK = "EXTRA_SERVICE_CALLBACK";
    public static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";

    private static final String EXTRA_POSITION = "EXTRA_POSITION";
    private static final String EXTRA_ACTION_WITH_OBJECT = "EXTRA_ACTION_WITH_OBJECT";
    private static final String EXTRA_DEAL_ACTION = "EXTRA_DEAL_ACTION";
    private static final String EXTRA_OBJECT_ID = "EXTRA_OBJECT_ID";
    private static final String EXTRA_CODE = "EXTRA_CODE";

    private static final int REQUEST_INVALID = -1;
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_USERNAME = "EXTRA_USERNAME";
    private static final String EXTRA_AMOUNT = "EXTRA_AMOUNT";
    private static final String EXTRA_PLAYER_ID = "EXTRA_PLAYER_ID";
    private static final String ACTION_GET_VENUES_AROUND_THE_PLAYER = "ru.mail.buy_it.service.ACTION_GET_VENUES_AROUND_THE_PLAYER";
    private static final String ACTION_GET_VENUES_AROUND_THE_POINT = "ru.mail.buy_it.service.ACTION_GET_VENUES_AROUND_THE_POINT";
    private static final String ACTION_GET_MY_VENUES = "ru.mail.buy_it.service.ACTION_GET_MY_VENUES";
    private static final String ACTION_GET_PLAYER_VENUES = "ru.mail.buy_it.service.ACTION_GET_PLAYER_VENUES";
    private static final String ACTION_GET_RATING = "ru.mail.buy_it.service.ACTION_GET_RATING";
    private static final String EXTRA_RATING_LIMIT = "EXTRA_LIMIT";
    private static final String EXTRA_RATING_OFFSET = "EXTRA_OFFSET";

    public BuyItService() {
        super("BuyItService");
    }

    private static final String ACTION_POST_OBJECT = "ru.mail.buy_it.service.ACTION_POST_OBJECT";
    private static final String ACTION_AUTHENTICATE = "ru.mail.buy_it.service.ACTION_AUTHENTICATE";
    private static final String ACTION_DEAL = "ru.mail.buy_it.service.ACTION_DEAL";
    private static final String ACTION_GET_PROFILE = "ru.mail.buy_it.service.ACTION_GET_PROFILE";
    private static final String ACTION_GET_USERS = "ru.mail.buy_it.service.ACTION_GET_USERS";
    private static final String ACTION_GET_DEALS = "ru.mail.buy_it.service.ACTION_GET_DEALS";

    private static final String ACTION_SELL_OBJECT = "ru.mail.buy_it.service.ACTION_SELL_PLACE";
    private static final String ACTION_UPGRADE_OBJECT = "ru.mail.buy_it.service.ACTION_SELL_PLACE";
    private static final String ACTION_BUY_OBJECT = "ru.mail.buy_it.service.ACTION_BUY_OBJECT";
    private static final String ACTION_COLLECT_LOOT = "ru.mail.buy_it.service.ACTION_COLLECT_LOOT";

    private static final String ACTION_SUGGEST_DEAL = "ru.mail.buy_it.service.ACTION_SUGGEST_DEAL";
    private static final String ACTION_REJECT_DEAL = "ru.mail.buy_it.service.ACTION_REJECT_DEAL";



    public static void startGetVenuesAroundThePlayerService(Context context, ResultReceiver serviceCallback, long requestId, LatLng position) {
        Intent intent = new Intent(ACTION_GET_VENUES_AROUND_THE_PLAYER, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_POSITION, position);
        context.startService(intent);
    }

    public static void startGetVenuesAroundThePointService(Context context, ResultReceiver serviceCallback, long requestId, LatLng position) {
        Intent intent = new Intent(ACTION_GET_VENUES_AROUND_THE_POINT, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_POSITION, position);
        context.startService(intent);
    }

    public static void startGetMyVenuesService(Context context, ResultReceiver serviceCallback, long requestId) {
        Intent intent = new Intent(ACTION_GET_MY_VENUES, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        context.startService(intent);
    }

    public static void startGetPlayerVenuesService(Context context, ResultReceiver serviceCallback, long requestId, long playerId) {
        Intent intent = new Intent(ACTION_GET_PLAYER_VENUES, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_PLAYER_ID, playerId);
        context.startService(intent);
    }

    public static void startBuyVenueService(Context context, ResultReceiver serviceCallback, long requestId, String id) {
        Intent intent = new Intent(ACTION_POST_OBJECT, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_ACTION_WITH_OBJECT, ActionWithPlace.BUY);
        intent.putExtra(EXTRA_OBJECT_ID, id);
        context.startService(intent);
    }

    public static void startSellVenueService(Context context, ResultReceiver serviceCallback, long requestId, String id) {
        Intent intent = new Intent(ACTION_POST_OBJECT, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_ACTION_WITH_OBJECT, ActionWithPlace.SELL);
        intent.putExtra(EXTRA_OBJECT_ID, id);
        context.startService(intent);
    }

    public static void startUpgradeVenueService(Context context, ResultReceiver serviceCallback, long requestId, String id) {
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

    public static void startGetRatingService(Context context, ResultReceiver serviceCallback, long requestId, long limit, long offset) {
        Intent intent = new Intent(ACTION_GET_RATING, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_RATING_LIMIT, limit);
        intent.putExtra(EXTRA_RATING_OFFSET, offset);
        context.startService(intent);
    }

    public static void startAcceptDealService(Context context, ResultReceiver serviceCallback, long requestId, long id) {
        Intent intent = new Intent(ACTION_DEAL, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_DEAL_ACTION, DealAction.ACCEPT);
        context.startService(intent);
    }

    public static void startSuggestDealService(Context context, ResultReceiver serviceCallback, long requestId, String venueId, long amount) {
        Intent intent = new Intent(ACTION_SUGGEST_DEAL, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_ID, venueId);
        intent.putExtra(EXTRA_AMOUNT, amount);
        context.startService(intent);
    }

    public static void startRejectDealService(Context context, ResultReceiver serviceCallback, long requestId, long id) {
        Intent intent = new Intent(ACTION_DEAL, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_DEAL_ACTION, DealAction.REJECT);
        context.startService(intent);
    }

    public static void startRevokeDealService(Context context, ResultReceiver serviceCallback, long requestId, long id) {
        Intent intent = new Intent(ACTION_DEAL, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_DEAL_ACTION, DealAction.REVOKE);
        context.startService(intent);
    }

    public static void startGetDealsService(Context context, ResultReceiver serviceCallback, long requestId) {
        Intent intent = new Intent(ACTION_GET_DEALS, null, context, BuyItService.class);
        intent.putExtra(EXTRA_SERVICE_CALLBACK, serviceCallback);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_SERVICE_CALLBACK);
        final String action = intent.getAction();
        switch (action) {
            case ACTION_GET_VENUES_AROUND_THE_PLAYER:
                final LatLng playerPosition = intent.getParcelableExtra(EXTRA_POSITION);
                Processor getVenuesAroundThePlayerProcessor = new GetVenuesAroundThePlayerProcessorCreator(this, new DefaultProcessorResultListener(intent, resultReceiver), playerPosition).createProcessor();
                getVenuesAroundThePlayerProcessor.process();
                break;
            case ACTION_GET_VENUES_AROUND_THE_POINT:
                final LatLng pointPosition = intent.getParcelableExtra(EXTRA_POSITION);
                Processor getVenuesAroundThePointProcessor = new GetVenuesAroundThePointProcessorCreator(this, new DefaultProcessorResultListener(intent, resultReceiver), pointPosition).createProcessor();
                getVenuesAroundThePointProcessor.process();
                break;
            case ACTION_GET_MY_VENUES:
                Processor getMyVenuesProcessor = new GetMyVenuesProcessorCreator(this, new DefaultProcessorResultListener(intent, resultReceiver)).createProcessor();
                getMyVenuesProcessor.process();
                break;
            case ACTION_GET_PLAYER_VENUES:
                long playerId = intent.getLongExtra(EXTRA_PLAYER_ID, 0);
                Processor getPlayerVenuesProcessor = new GetPlayerVenuesProcessorCreator(this, new DefaultProcessorResultListener(intent, resultReceiver), playerId).createProcessor();
                getPlayerVenuesProcessor.process();
                break;
            case ACTION_GET_PROFILE:
                Processor getProfileProcessor = new GetProfileProcessorCreator(this, new DefaultProcessorResultListener(intent, resultReceiver)).createProcessor();
                getProfileProcessor.process();
                break;
            case ACTION_AUTHENTICATE:
                final String code = intent.getStringExtra(EXTRA_CODE);
                Processor authenticationProcessor = new AuthenticationProcessorCreator(this, new AuthenticationProcessorResultListener(intent, resultReceiver), code).createProcessor();
                authenticationProcessor.process();
                break;
            case ACTION_POST_OBJECT:
                final String id = intent.getStringExtra(EXTRA_OBJECT_ID);
                final ActionWithPlace actionWithPlace = (ActionWithPlace) intent.getSerializableExtra(EXTRA_ACTION_WITH_OBJECT);
                Processor actionWithPlaceProcessor = new ActionWithPlaceProcessorCreator(this, new DefaultProcessorResultListener(intent, resultReceiver), id, actionWithPlace).createProcessor();
                actionWithPlaceProcessor.process();
                break;
            case ACTION_GET_RATING:
                final long offset = intent.getLongExtra(EXTRA_RATING_OFFSET, 0);
                final long limit = intent.getLongExtra(EXTRA_RATING_LIMIT, 0);
                Processor processor = new RatingProcessorCreator(this, new DefaultProcessorResultListener(intent, resultReceiver), limit, offset).createProcessor();
                processor.process();
                break;
            case ACTION_DEAL:
                final long dealId = intent.getLongExtra(EXTRA_ID, 0);
                final DealAction dealAction = (DealAction) intent.getSerializableExtra(EXTRA_DEAL_ACTION);
                Processor dealProcessor = new ActionWithDealProcessorCreator(this, new DefaultProcessorResultListener(intent, resultReceiver), dealAction, dealId).createProcessor();
                dealProcessor.process();
                break;
            case ACTION_SUGGEST_DEAL:
                final String venueId = intent.getStringExtra(EXTRA_ID);
                final long amount = intent.getLongExtra(EXTRA_AMOUNT, 0);
                Processor suggestDealProcessor = new SuggestDealProcessorCreator(this, new DefaultProcessorResultListener(intent, resultReceiver), venueId, amount).createProcessor();
                suggestDealProcessor.process();
                break;
            case ACTION_GET_DEALS:
                Processor getDealsProcessor = new DealsProcessorCreator(this, new DefaultProcessorResultListener(intent, resultReceiver)).createProcessor();
                getDealsProcessor.process();
                break;
            default:
                new DefaultProcessorResultListener(intent, resultReceiver);
                break;
        }
    }

    public enum ActionWithPlace {
        BUY,
        SELL,
        UPGRADE,
        COLLECT_LOOT
    }

    public enum DealAction {
        SUGGEST,
        ACCEPT,
        REJECT,
        REVOKE;

        public String toPath() {
            switch (this) {
                case SUGGEST:
                    return "/new";
                case ACCEPT:
                    return "/accept";
                case REJECT:
                case REVOKE:
                    return "/cancel";
                default:
                    throw new IllegalStateException();
            }
        }
    }

    private static class DefaultProcessorResultListener implements Processor.OnProcessorResultListener {

        private final Intent mOriginalRequestIntent;
        private final ResultReceiver mResultReceiver;

        public DefaultProcessorResultListener(Intent originalRequestIntent, ResultReceiver resultReceiver) {
            mOriginalRequestIntent = originalRequestIntent;
            mResultReceiver = resultReceiver;
        }

        @Override
        public void send(Response response) {
            if (mResultReceiver != null) {
                final Bundle result = new Bundle();
                result.putParcelable(EXTRA_ORIGINAL_INTENT, mOriginalRequestIntent);
                mResultReceiver.send(response.getStatus(), result);
            }
        }
    }

    private static class AuthenticationProcessorResultListener implements Processor.OnProcessorResultListener {
        private final Intent mOriginalRequestIntent;
        private final ResultReceiver mResultReceiver;
        public AuthenticationProcessorResultListener(Intent originalRequestIntent, ResultReceiver resultReceiver) {
            mOriginalRequestIntent = originalRequestIntent;
            mResultReceiver = resultReceiver;
        }

        @Override
        public void send(Response response) {
            if (mResultReceiver != null) {
                AuthenticationResult authenticationResult = (AuthenticationResult) response.getData().get(AuthenticationProcessor.KET_AUTHENTICATION_RESULT);
                long id = authenticationResult.getId();
                String username = authenticationResult.getUsername();
                final Bundle result = new Bundle();
                result.putParcelable(EXTRA_ORIGINAL_INTENT, mOriginalRequestIntent);
                result.putLong(EXTRA_ID, id);
                result.putString(EXTRA_USERNAME, username);
                mResultReceiver.send(response.getStatus(), result);
            }
        }

        protected Bundle getOriginalIntentBundle() {
            Bundle originalRequest = new Bundle();
            originalRequest.putParcelable(EXTRA_ORIGINAL_INTENT, mOriginalRequestIntent);
            return originalRequest;
        }

    }
}
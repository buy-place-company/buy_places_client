package ru.tp.buy_places.service.action_with_place;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tp.buy_places.content_provider.BuyPlacesContract;
import ru.tp.buy_places.service.Processor;
import ru.tp.buy_places.service.network.Request;
import ru.tp.buy_places.service.network.Response;
import ru.tp.buy_places.service.resourses.Place;
import ru.tp.buy_places.service.resourses.Player;
import ru.tp.buy_places.service.resourses.Resource;

import static ru.tp.buy_places.service.BuyItService.VenueAction;

/**
 * Created by Ivan on 10.05.2015.
 */
public class ActionWithPlaceProcessor extends Processor {
    private static final String KEY_VENUE = "KEY_VENUE";
    private static final String KEY_PLAYER = "KEY_PLAYER";
    private final String mId;
    private final VenueAction mVenueAction;

    public ActionWithPlaceProcessor(Context context, OnProcessorResultListener listener, String id, VenueAction venueAction) {
        super(context, listener);
        mId = id;
        mVenueAction = venueAction;
    }

    @Override
    protected Response parseResponseJSONObject(JSONObject responseJSONObject) {
        int status = responseJSONObject.optInt("status");
        String message = responseJSONObject.optString("message", null);
        JSONObject userJSONObject = responseJSONObject.optJSONObject("user");
        JSONObject venueJSONObject = responseJSONObject.optJSONObject("venue");
        Place place = Place.fromJSONObject(venueJSONObject);
        Player user = Player.fromJSONObject(userJSONObject);
        Map<String, Resource> data = new HashMap<>();
        data.put(KEY_VENUE, place);
        data.put(KEY_PLAYER, user);
        return new Response(status, message, data);
    }

    @Override
    protected Request prepareRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("venue_id", mId);
        params.put("action", mVenueAction.name().toLowerCase());
        return new Request(mContext, "/venue/action", Request.RequestMethod.POST, params);
    }

    @Override
    protected void updateContentProviderBeforeExecutingRequest(Request request) {
        ContentValues stateUpdatingIsTrue = new ContentValues();
        stateUpdatingIsTrue.put(BuyPlacesContract.Places.COLUMN_STATE_UPDATING, true);
        mContext.getContentResolver().update(BuyPlacesContract.Places.CONTENT_URI, stateUpdatingIsTrue, BuyPlacesContract.Places.WITH_SPECIFIED_ID_SELECTION, new String[]{mId});
    }

    @Override
    protected void updateContentProviderAfterExecutingRequest(Response response) {
        Place place = (Place) response.getData().get(KEY_VENUE);
        Player player = (Player) response.getData().get(KEY_PLAYER);
        place.setStateUpdating(false);
        place.setIsInOwnership(Place.checkIsInOwnership(mContext, place.getOwner()));
        player.writeToDatabase(mContext);
        place.writeToDatabase(mContext);

        mContext.getContentResolver().notifyChange(BuyPlacesContract.Places.CONTENT_URI, null);
    }
}
